package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;
import app.util.InputReader;

import java.util.List;

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
        List<String> args = request.args();
        String idStr;
        if (!args.isEmpty()) {
            idStr = args.get(0);
        } else {
            idStr = reader.prompt("Enter id to remove: ");
        }
        try {
            Long id = Long.parseLong(idStr);
            HumanBeing removed = collectionManager.removeById(id);
            if (removed != null) {
                return new Response("Item removed");
            } else {
                return new Response("Element with this id not found");
            }
        } catch (NumberFormatException e) {
            return new Response("The id must be a number");
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

