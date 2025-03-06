package app.commands;

import app.transfer.Request;
import app.transfer.Response;

/**
 * exit command: ends the program.
 */
public class ExitCommand implements Command {

    @Override
    public Response execute(Request request) {
        System.exit(0);
        return null; // Reaching this line is impossible
    }

    @Override
    public String getName() {
        return "Exit";
    }

    @Override
    public String getDescription() {
        return "End the program";
    }
}