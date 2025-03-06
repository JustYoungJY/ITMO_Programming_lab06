package app;

import app.collection.CollectionManager;
import app.commands.*;
import app.factory.HumanBeingFactory;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;
import app.util.FileManager;
import app.util.InputReader;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


/**
 * Main application class.
 * Reads the FILE environment variable, loads the collection, and starts interactive mode.
 */
public class Main {
    public static void main(String[] args) {

        // Reading an environment variable
        String fileName = System.getenv("FILE");
        if (fileName == null || fileName.isEmpty()) {
            System.err.println("The environment variable is not set. Shutdown...");
            System.exit(1);
        }

        // Initializing Components
        FileManager fileManager = new FileManager(fileName);
        CollectionManager<HumanBeing> collectionManager = new CollectionManager<>();
        InputReader inputReader = new InputReader(new Scanner(System.in));
        HumanBeingFactory factory = new HumanBeingFactory(inputReader);

        // Loading a collection from a file
        try {
            collectionManager.setCollection(fileManager.loadCollection());
            System.out.println("Collection successfully loaded from file");
        } catch (Exception e) {
            System.err.println("Error loading collection: " + e.getMessage());
        }

        // Registering commands
        CommandInvoker invoker = new CommandInvoker();
        invoker.register(new HelpCommand(invoker));
        invoker.register(new InfoCommand(collectionManager));
        invoker.register(new ShowCommand(collectionManager));
        invoker.register(new InsertCommand(collectionManager, factory, inputReader));
        invoker.register(new UpdateCommand(collectionManager, factory, inputReader));
        invoker.register(new RemoveKeyCommand(collectionManager, inputReader));
        invoker.register(new ClearCommand(collectionManager));
        invoker.register(new SaveCommand(collectionManager, fileManager));
        invoker.register(new ExecuteScriptCommand(invoker, inputReader));
        invoker.register(new ExitCommand());
        invoker.register(new RemoveLowerCommand(collectionManager, factory, inputReader));
        invoker.register(new ReplaceIfGreaterCommand(collectionManager, factory, inputReader));
        invoker.register(new RemoveLowerKeyCommand(collectionManager, inputReader));
        invoker.register(new AverageOfImpactSpeedCommand(collectionManager));
        invoker.register(new CountLessThanWeaponTypeCommand(collectionManager, inputReader));
        invoker.register(new PrintDescendingCommand(collectionManager));


        // Command loop
        System.out.println("Enter command:");
        while (true) {
            System.out.print("> ");
            String inputLine = inputReader.readLine();
            if (inputLine == null || inputLine.trim().isEmpty()) continue;
            String[] parts = inputLine.trim().split("\\s+", 2);
            String commandName = parts[0];
            String[] argsArray = parts.length > 1 ? parts[1].split("\\s+") : new String[0];
            Request request = new Request(commandName, Arrays.asList(argsArray), List.of());
            try {
                Response response = invoker.executeCommand(request);
                System.out.println(response.message());
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}