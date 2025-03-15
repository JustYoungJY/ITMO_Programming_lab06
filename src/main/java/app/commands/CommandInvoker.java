package app.commands;

import app.transfer.Request;
import app.transfer.Response;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for adding and executing commands.
 */
public class CommandInvoker {
    private final Map<String, Command> commands = new HashMap<>();

    public void register(Command command) {
        commands.put(command.getName().toLowerCase(), command);
    }

    public Response executeCommand(Request request) {
        String commandName = request.command().toLowerCase();
        Command command = commands.get(commandName);
        if (command == null) {
            return new Response("Unknown command");
        }
        try {
            return command.execute(request);
        } catch (Exception e) {
            return new Response("Error executing command: " + e.getMessage());
        }
    }

    public Collection<Command> getCommands() {
        return commands.values();
    }
}