package app.client;

import app.factory.HumanBeingFactory;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;
import app.util.InputReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;

/**
 * Client Script Executor: Reads commands line by line from a local file (on the client),
 * collects Human Being objects, and sends requests to the server.
 */
public class ClientScriptExecutor {
    private final Client client;
    private final HumanBeingFactory factory;
    private final InputReader baseReader;

    public ClientScriptExecutor(Client client, HumanBeingFactory factory, InputReader baseReader) {
        this.client = client;
        this.factory = factory;
        this.baseReader = baseReader;
    }

    public void runScript(String fileName) {
        File scriptFile = new File(fileName);
        if (!scriptFile.exists()) {
            System.out.println("Script file not found: " + fileName);
            return;
        }
        try (Scanner scriptScanner = new Scanner(scriptFile)) {
            InputReader originalReader = baseReader.cloneScanner();

            baseReader.setScanner(scriptScanner);

            while (scriptScanner.hasNextLine()) {
                String cmdLine = scriptScanner.nextLine().trim();
                if (cmdLine.isEmpty()) continue;

                if (cmdLine.equalsIgnoreCase("insert") || cmdLine.equalsIgnoreCase("update")) {
                    HumanBeing human = factory.createHumanBeing();
                    Request req = new Request(cmdLine, Collections.emptyList(), human);
                    sendRequestAndPrint(req);
                } else {
                    Request req = new Request(cmdLine, Collections.emptyList(), null);
                    sendRequestAndPrint(req);
                }
            }

            baseReader.setScanner(originalReader.getScanner());
        } catch (FileNotFoundException e) {
            System.out.println("Script file not found: " + e.getMessage());
        }
    }

    private void sendRequestAndPrint(Request req) {
        try {
            Response response = client.sendRequest(req);
            System.out.println("Server response: " + response.message());
        } catch (IOException e) {
            System.err.println("Error communicating with server: " + e.getMessage());
        }
    }
}
