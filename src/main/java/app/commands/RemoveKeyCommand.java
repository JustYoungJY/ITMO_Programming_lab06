package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;
import app.util.InputReader;

/**
 * remove_key command: removes an element from a collection based on the given key.
 */
public class RemoveKeyCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;
    private final InputReader reader;

    public RemoveKeyCommand(CollectionManager<HumanBeing> collectionManager, InputReader reader) {
        this.collectionManager = collectionManager;
        this.reader = reader;
    }

    @Override
    public Response execute(Request request) {
        String keyStr;
        if (request.args().isEmpty()) {
            keyStr = request.args().get(0);
        } else {
            keyStr = reader.prompt("Enter key to remove: ");
        }
        try {
            Long key = Long.parseLong(keyStr);
            HumanBeing removed = collectionManager.removeKey(key);
            if (removed != null) {
                return new Response("Item removed", null, null);
            } else {
                return new Response("Element with this key not found", null, null);
            }
        } catch (NumberFormatException e) {
            return new Response("The key must be a number", null, null);
        }
    }

    @Override
    public String getName() {
        return "remove_key";
    }

    @Override
    public String getDescription() {
        return "remove an element from a collection by its key";
    }
}

