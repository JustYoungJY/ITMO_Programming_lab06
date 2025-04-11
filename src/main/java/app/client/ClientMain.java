package app.client;

import app.factory.HumanBeingFactory;
import app.model.HumanBeing;
import app.network.NetworkUtil;
import app.transfer.Request;
import app.transfer.Response;
import app.util.InputReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class ClientMain {
    public static void main(String[] args) {
        Client client = new Client("localhost", 12345);
        Scanner consoleScanner = new Scanner(System.in);
        InputReader reader = new InputReader(consoleScanner);
        HumanBeingFactory factory = new HumanBeingFactory(reader);

        System.out.println("Client started. Enter commands (type 'exit' to quit):");
        while (true) {
            System.out.print("> ");
            String inputLine = consoleScanner.nextLine();
            if (inputLine.trim().equalsIgnoreCase("exit")) {
                break;
            }
            String[] parts = inputLine.trim().split("\\s+", 2);
            String commandName = parts[0];
            List<String> argsList = parts.length > 1 ? Arrays.asList(parts[1].split("\\s+")) : Collections.emptyList();

            Request request;
            if (commandName.equalsIgnoreCase("insert") || commandName.equalsIgnoreCase("update")) {
                // Собираем объект HumanBeing на клиенте
                HumanBeing human = factory.createHumanBeing();
                request = new Request(commandName, argsList, human);
            } else {
                request = new Request(commandName, argsList, null);
            }
            try {
                Response response = client.sendRequest(request);
                System.out.println("Server response: " + response.message());
            } catch (IOException e) {
                System.err.println("Error communicating with server: " + e.getMessage());
            }
        }
        client.close();
        consoleScanner.close();
    }
}