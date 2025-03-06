package app.commands;

import app.transfer.Request;
import app.transfer.Response;

/**
 * Interface for commands that interact with the collection.
 */
public interface Command {
    Response execute(Request request) throws Exception;

    String getName();

    String getDescription();
}