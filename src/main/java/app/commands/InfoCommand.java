package app.commands;

import app.collection.CollectionManager;
import app.transfer.Request;
import app.transfer.Response;

/**
 * The info: command displays information about the collection.
 */
public class InfoCommand implements Command {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        return new Response(collectionManager.info(), null, null);
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Display information about a collection";
    }
}