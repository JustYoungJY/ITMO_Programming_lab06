package app.server;

import app.collection.CollectionManager;
import app.commands.*;
import app.factory.HumanBeingFactory;
import app.model.HumanBeing;
import app.network.NetworkUtil;
import app.transfer.Request;
import app.transfer.Response;
import app.util.FileManager;
import app.util.InputReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * The server runs in single-threaded mode and uses blocking I/O.
 */
public class ServerMain {
    public static final int PORT = 12345;

    public static void main(String[] args) {
        String fileName = System.getenv("FILE");
        if (fileName == null || fileName.isEmpty()) {
            System.err.println("Environment variable FILE is not set. Shutdown...");
            System.exit(1);
        }

        // XML FileManager
        FileManager fileManager = new FileManager(fileName);
        CollectionManager<HumanBeing> collectionManager = new CollectionManager<>();
        try {
            collectionManager.setCollection(fileManager.loadCollection());
            System.out.println("Collection loaded successfully.");
        } catch (Exception e) {
            System.err.println("Error loading collection: " + e.getMessage());
        }

        // Setup commands
        InputReader reader = new InputReader(new Scanner(System.in));
        HumanBeingFactory factory = new HumanBeingFactory(reader);
        CommandInvoker invoker = new CommandInvoker();
        invoker.register(new HelpCommand(invoker));
        invoker.register(new InfoCommand(collectionManager));
        invoker.register(new ShowCommand(collectionManager));
        invoker.register(new InsertCommand(collectionManager));
        invoker.register(new UpdateCommand(collectionManager));
        invoker.register(new RemoveKeyCommand(collectionManager));
        invoker.register(new ClearCommand(collectionManager));
        invoker.register(new SaveCommand(collectionManager, fileManager));
        invoker.register(new ExecuteScriptCommand(invoker, new InputReader(new Scanner(System.in))));
        invoker.register(new ExitCommand());
        invoker.register(new PrintDescendingCommand(collectionManager));
        invoker.register(new RemoveLowerCommand(collectionManager));
        invoker.register(new RemoveLowerKeyCommand(collectionManager));
        invoker.register(new ReplaceIfGreaterCommand(collectionManager));
        invoker.register(new AverageOfImpactSpeedCommand(collectionManager));
        invoker.register(new CountLessThanWeaponTypeCommand(collectionManager));

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                handleClient(clientSocket, invoker);
            }
        } catch (IOException ex) {
            System.err.println("Server exception: " + ex.getMessage());
        }
    }

    private static void handleClient(Socket socket, CommandInvoker invoker) {
        try (socket;
             var in = socket.getInputStream();
             var out = socket.getOutputStream();
             var scanner = new Scanner(in);
             var writer = new java.io.PrintWriter(out, true)) {

            while (scanner.hasNextLine()) {
                String requestJson = scanner.nextLine().trim();
                if (requestJson.isEmpty()) {
                    continue;
                }

                // Deserialize JSON to Request
                Request request = NetworkUtil.fromJson(requestJson, Request.class);

                Response response = invoker.executeCommand(request);
                String responseJson = NetworkUtil.toJson(response);
                writer.println(responseJson);
            }
            System.out.println("Client disconnected");
        } catch (Exception ex) {
            System.err.println("Client handling exception: " + ex.getMessage());
        }
    }
}