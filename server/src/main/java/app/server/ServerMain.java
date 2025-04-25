package app.server;

import app.collection.CollectionManager;
import app.commands.*;
import app.factory.HumanBeingFactory;
import app.model.HumanBeing;
import app.network.NetworkUtil;
import app.transfer.Request;
import app.transfer.Response;
import app.util.FileManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * The server runs in single-threaded mode and uses blocking I/O.
 */
public class ServerMain {
    public static final int PORT = 12345;

    // per-client context
    private static final class ClientCtx {
        ByteBuffer lenBuf = ByteBuffer.allocate(4);
        ByteBuffer dataBuf = null;
    }

    public static void main(String[] args) {
        String fileName = System.getenv("FILE");
        if (fileName == null || fileName.isEmpty()) {
            System.err.println("env FILE not set");
            System.exit(1);
        }
        FileManager fm = new FileManager(fileName);
        CollectionManager<HumanBeing> cm = new CollectionManager<>();
        try {
            cm.setCollection(fm.loadCollection());
            System.out.println("Collection loaded.");
        } catch (Exception e) {
            System.err.println("Load error: " + e.getMessage());
        }

        // command registry
        CommandInvoker inv = new CommandInvoker();
        inv.register(new HelpCommand(inv));
        inv.register(new InfoCommand(cm));
        inv.register(new ShowCommand(cm));
        inv.register(new InsertCommand(cm));
        inv.register(new UpdateCommand(cm));
        inv.register(new RemoveKeyCommand(cm));
        inv.register(new ClearCommand(cm));
        inv.register(new PrintDescendingCommand(cm));
        inv.register(new RemoveLowerCommand(cm));
        inv.register(new RemoveLowerKeyCommand(cm));
        inv.register(new ReplaceIfGreaterCommand(cm));
        inv.register(new AverageOfImpactSpeedCommand(cm));
        inv.register(new CountLessThanWeaponTypeCommand(cm));

        // NIO bootstrap
        try (ServerSocketChannel ssc = ServerSocketChannel.open();
             Selector selector       = Selector.open()) {

            ssc.bind(new InetSocketAddress(PORT));
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server listening on " + PORT);

            // console reader – non-blocking
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            boolean running = true;
            while (running) {
                selector.select(200); // 200 ms timeout

                // handle IO events
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next(); it.remove();

                    if (key.isAcceptable()) {
                        SocketChannel ch = ssc.accept();
                        if (ch != null) {
                            ch.configureBlocking(false);
                            SelectionKey k = ch.register(selector, SelectionKey.OP_READ);
                            k.attach(new ClientCtx());
                            System.out.println("Client connected: " + ch.getRemoteAddress());
                        }
                    } else if (key.isReadable()) {
                        SocketChannel ch = (SocketChannel) key.channel();
                        ClientCtx ctx = (ClientCtx) key.attachment();
                        if (!readFromClient(ch, ctx, inv)) {
                            key.cancel();
                            ch.close();
                            System.out.println("Client disconnected");
                        }
                    }
                }

                // handle console (non-blocking)
                while (console.ready()) {
                    String line = console.readLine();
                    if (line == null) continue;
                    line = line.trim().toLowerCase();
                    switch (line) {
                        case "save":
                            safeSave(fm, cm);
                            break;
                        case "exit":
                            safeSave(fm, cm);
                            running = false;
                            break;
                        default:
                            System.out.println("Unknown cmd. Use: save or exit");
                    }
                }
            }
            System.out.println("Shutting down server…");
        } catch (IOException e) {
            System.err.println("Server I/O error: " + e.getMessage());
        }
    }

    private static void safeSave(FileManager fm, CollectionManager<HumanBeing> cm) {
        try {
            fm.saveCollection(cm.getCollection());
            System.out.println("Collection saved.");
        } catch (Exception e) {
            System.err.println("Save error: " + e.getMessage());
        }
    }

    private static boolean readFromClient(SocketChannel ch,
                                          ClientCtx ctx,
                                          CommandInvoker inv) {
        try {
            // read length
            if (ctx.dataBuf == null) {
                if (ch.read(ctx.lenBuf) == -1) return false;
                if (ctx.lenBuf.hasRemaining()) return true;
                ctx.lenBuf.flip();
                int len = ctx.lenBuf.getInt();
                ctx.dataBuf = ByteBuffer.allocate(len);
            }

            // read body
            if (ch.read(ctx.dataBuf) == -1) return false;
            if (ctx.dataBuf.hasRemaining()) return true;

            // process full packet
            ctx.dataBuf.flip();
            String jsonReq = StandardCharsets.UTF_8.decode(ctx.dataBuf).toString();
            Request req = NetworkUtil.fromJson(jsonReq, Request.class);
            Response resp = inv.executeCommand(req);
            byte[] respBytes = NetworkUtil.toJson(resp).getBytes(StandardCharsets.UTF_8);

            ByteBuffer out = ByteBuffer.allocate(4 + respBytes.length);
            out.putInt(respBytes.length).put(respBytes).flip();
            while (out.hasRemaining()) ch.write(out);

            // reset context for next message
            ctx.lenBuf.clear();
            ctx.dataBuf = null;
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}