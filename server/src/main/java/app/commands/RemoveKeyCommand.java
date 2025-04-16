package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;

import java.util.List;

/**
 * remove_key command: removes an element from a collection based on the given key.
 */
public class RemoveKeyCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;

    public RemoveKeyCommand(CollectionManager<HumanBeing> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        List<String> args = request.args();
        if (args.isEmpty()) {
            return new Response("You must provide a numeric key argument for remove_key.");
        }
        String keyStr = args.get(0);
        try {
            Long key = Long.parseLong(keyStr);
            HumanBeing removed = collectionManager.removeById(key);
            if (removed != null) {
                return new Response("Item with id=" + key + " removed.");
            } else {
                return new Response("No element with id=" + key + " found.");
            }
        } catch (NumberFormatException e) {
            return new Response("Key must be a numeric value.");
        }
    }

    @Override
    public String getName() {
        return "remove_key";
    }

    @Override
    public String getDescription() {
        return "Removes an element from the collection by its key (long). Requires one argument.";
    }
}