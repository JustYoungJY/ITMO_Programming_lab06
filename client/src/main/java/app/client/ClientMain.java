package app.client;

import app.factory.HumanBeingFactory;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;
import app.util.InputReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Client Main: the main client class, supports manual command input,
 * as well as the execute_script command, which will execute the script input.
 */
public class ClientMain {
    public static void main(String[] args) {
        Client client;
        try {
            client = new Client("localhost", 12345);
        } catch (RuntimeException e) {
            System.err.println("Cannot connect to server right now: " + e.getMessage());
            System.out.println("You can try commands, but likely they'll fail until server is up...");
            client = new Client("localhost", 12345);
        }

        Scanner consoleScanner = new Scanner(System.in);
        InputReader reader = new InputReader(consoleScanner);
        HumanBeingFactory factory = new HumanBeingFactory(reader);

        System.out.println("Client started. Enter commands (type 'exit' to quit).");

        while (true) {
            System.out.print("> ");
            if (!consoleScanner.hasNextLine()) {
                System.out.println("No more input. Exiting...");
                break;
            }
            String inputLine = consoleScanner.nextLine().trim();
            if (inputLine.equalsIgnoreCase("exit")) {
                break;
            }

            String[] parts = inputLine.split("\\s+", 2);
            String commandName = parts[0];
            List<String> argsList = (parts.length > 1)
                    ? Arrays.asList(parts[1].split("\\s+"))
                    : Collections.emptyList();

            if (commandName.equalsIgnoreCase("save")) {
                System.out.println("Error: 'save' can be executed only on the server console.");
                continue;
            }

            // script command
            if (commandName.equalsIgnoreCase("execute_script")) {
                if (argsList.isEmpty()) {
                    System.out.println("Error: no script file provided.");
                    continue;
                }
                String scriptFileName = argsList.get(0);
                ClientScriptExecutor scriptExec = new ClientScriptExecutor(client, factory, reader);
                scriptExec.runScript(scriptFileName);
                continue;
            }

            // build request
            Request request;
            if (commandName.equalsIgnoreCase("insert")
                    || commandName.equalsIgnoreCase("update")) {
                HumanBeing human = factory.createHumanBeing();
                request = new Request(commandName, argsList, human);
            } else if (commandName.equalsIgnoreCase("replace_if_greater")) {
                if (argsList.isEmpty()) {
                    System.out.println("Error: you must specify old id for replace_if_greater");
                    continue;
                }
                HumanBeing newHuman = factory.createHumanBeing();
                request = new Request(commandName, argsList, newHuman);
            } else {
                request = new Request(commandName, argsList, null);
            }

            // Sending a request
            try {
                Response response = client.sendRequest(request);
                System.out.println("Server response: " + response.message());
            } catch (IOException e) {
                System.err.println("Failed to communicate with server even after re-try: " + e.getMessage());
            }
        }

        client.close();
        consoleScanner.close();
    }
}
