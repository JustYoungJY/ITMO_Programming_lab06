package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;
import app.util.InputReader;

import java.util.List;

/**
 * remove_lower_key command: Removes from the collection all elements whose key is less than the specified one.
 */
public class RemoveLowerKeyCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;
    private final InputReader reader;

    public RemoveLowerKeyCommand(CollectionManager<HumanBeing> collectionManager, InputReader reader) {
        this.collectionManager = collectionManager;
        this.reader = reader;
    }

    @Override
    public Response execute(Request request) {
        List<String> args = request.args();
        String thresholdStr;
        if (!args.isEmpty()) {
            thresholdStr = args.get(0);
        } else {
            thresholdStr = reader.prompt("Enter threshold key: ");
        }
        try {
            Long threshold = Long.parseLong(thresholdStr);
            int initialSize = collectionManager.getCollection().size();
            collectionManager.getCollection().keySet().removeIf(key -> key < threshold);
            int removed = initialSize - collectionManager.getCollection().size();
            return new Response(removed + "items removed", null, null);
        } catch (NumberFormatException e) {
            return new Response("The key must be a number", null, null);
        }
    }

    @Override
    public String getName() {
        return "remove_lower_key";
    }

    @Override
    public String getDescription() {
        return "Remove from the collection all elements whose key is less than a given one";
    }
}
