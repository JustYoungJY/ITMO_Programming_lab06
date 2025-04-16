package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;

/**
 * clear command: clears the collection.
 */
public class ClearCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;

    public ClearCommand(CollectionManager<HumanBeing> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        collectionManager.clear();
        return new Response("Collection cleared");
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "Clear collection";
    }
}
