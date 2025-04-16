package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;

import java.util.List;

/**
 * remove_lower_key command: Removes from the collection all elements whose key is less than the specified one.
 */
public class RemoveLowerKeyCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;

    public RemoveLowerKeyCommand(CollectionManager<HumanBeing> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        List<String> args = request.args();
        if (args.isEmpty()) {
            return new Response("You must provide a numeric threshold for remove_lower_key.");
        }
        String thresholdStr = args.get(0);
        try {
            long threshold = Long.parseLong(thresholdStr);
            int initialSize = collectionManager.getCollection().size();
            collectionManager.getCollection().keySet().removeIf(key -> key < threshold);
            int removed = initialSize - collectionManager.getCollection().size();
            return new Response(removed + " items removed");
        } catch (NumberFormatException e) {
            return new Response("The threshold must be a number.");
        }
    }

    @Override
    public String getName() {
        return "remove_lower_key";
    }

    @Override
    public String getDescription() {
        return "Remove all elements whose key < the given threshold. Requires one numeric argument.";
    }
}