package app.client;

import app.network.NetworkUtil;
import app.transfer.Request;
import app.transfer.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * The client uses non-blocking mode to send a request and receive a response.
 */
public class Client {
    private final String host;
    private final int port;
    private final Gson gson;
    private SocketChannel socketChannel;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        this.gson = new GsonBuilder().create();

        // connecting to the server or throwing an error
        try {
            connect();
        } catch (IOException e) {
            throw new RuntimeException("Failed to connect to server initially", e);
        }
    }

    private void connect() throws IOException {
        // If the old channel is not closed, then we will close it.
        if (socketChannel != null && socketChannel.isOpen()) {
            socketChannel.close();
        }
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(host, port));

        while (!socketChannel.finishConnect()) {
            //...
        }
        System.out.println("Connected to server at " + host + ":" + port);
    }

    public Response sendRequest(Request request) throws IOException {
        String jsonRequest = NetworkUtil.toJson(request) + "\n";
        ByteBuffer writeBuffer = ByteBuffer.wrap(jsonRequest.getBytes(StandardCharsets.UTF_8));

        try {
            // Request to socket
            while (writeBuffer.hasRemaining()) {
                socketChannel.write(writeBuffer);
            }

            ByteBuffer readBuffer = ByteBuffer.allocate(4096);
            int bytesRead = socketChannel.read(readBuffer);

            while (bytesRead == 0) {
                bytesRead = socketChannel.read(readBuffer);
            }
            // If bytesRead < 0 the connection is terminated.
            if (bytesRead < 0) {
                throw new IOException("Server closed the connection");
            }

            readBuffer.flip();
            String jsonResponse = StandardCharsets.UTF_8.decode(readBuffer).toString().trim();
            return NetworkUtil.fromJson(jsonResponse, Response.class);

        } catch (IOException e) {
            System.err.println("Connection lost or server unavailable: " + e.getMessage());
            // Reconnecting...
            try {
                System.out.println("Trying to reconnect...");
                connect();
                System.out.println("Reconnected. Resending the request...");

                return resendRequest(request);
            } catch (IOException re) {
                throw new IOException("Failed to reconnect to server.", re);
            }
        }
    }

    private Response resendRequest(Request request) throws IOException {
        String jsonRequest = NetworkUtil.toJson(request) + "\n";
        ByteBuffer writeBuffer = ByteBuffer.wrap(jsonRequest.getBytes(StandardCharsets.UTF_8));

        while (writeBuffer.hasRemaining()) {
            socketChannel.write(writeBuffer);
        }

        ByteBuffer readBuffer = ByteBuffer.allocate(4096);
        int bytesRead = socketChannel.read(readBuffer);
        while (bytesRead == 0) {
            bytesRead = socketChannel.read(readBuffer);
        }
        if (bytesRead < 0) {
            throw new IOException("Server closed the connection after reconnect");
        }

        readBuffer.flip();
        String jsonResponse = StandardCharsets.UTF_8.decode(readBuffer).toString().trim();
        return NetworkUtil.fromJson(jsonResponse, Response.class);
    }

    public void close() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            System.err.println("Error closing client: " + e.getMessage());
        }
    }
}