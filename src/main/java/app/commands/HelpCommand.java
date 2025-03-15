package app.commands;

import app.transfer.Request;
import app.transfer.Response;

import java.util.StringJoiner;

/**
 * Help command: Displays a list of available commands.
 */
public class HelpCommand implements Command {
    private final CommandInvoker invoker;

    public HelpCommand(CommandInvoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Response execute(Request request) {
        StringJoiner sj = new StringJoiner("\n");
        sj.add("list of available commands");
        for (Command command : invoker.getCommands()) {
            sj.add(" - " + command.getName() + ": " + command.getDescription());
        }
        return new Response(sj.toString());
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Display help on available commands";
    }
}