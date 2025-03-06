package app.commands;

import app.transfer.Request;
import app.transfer.Response;
import app.util.InputReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collections;

/**
 * execute_script command: Reads and executes a script from the specified file.
 */
public class ExecuteScriptCommand implements Command {
    private final CommandInvoker invoker;
    private final InputReader reader;

    public ExecuteScriptCommand(CommandInvoker invoker, InputReader reader) {
        this.invoker = invoker;
        this.reader = reader;
    }

    @Override
    public Response execute(Request request) {
        String fileName;
        if (!request.args().isEmpty()) {
            fileName = request.args().get(0);
        } else {
            fileName = reader.prompt("Enter script file name: ");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                invoker.executeCommand(new Request(line.trim(), Collections.emptyList(), Collections.emptyList()));
            }
            return new Response("Script executed", null, null);
        } catch (Exception e) {
            return new Response("Error executing script: " + e.getMessage(), null, null);
        }
    }

    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String getDescription() {
        return "Read and execute a script from the specified file";
    }
}