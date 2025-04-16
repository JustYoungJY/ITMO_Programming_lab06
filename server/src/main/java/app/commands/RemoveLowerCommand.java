package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;

import java.util.List;

/**
 * remove_lower command: Removes from the collection all elements less than the given one.
 */
public class RemoveLowerCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;

    public RemoveLowerCommand(CollectionManager<HumanBeing> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        List<String> args = request.args();
        if (args.isEmpty()) {
            return new Response("You must provide an ID argument for remove_lower.");
        }
        String idStr = args.get(0);
        try {
            long refId = Long.parseLong(idStr);
            HumanBeing reference = new HumanBeing();
            reference.setId(refId);

            int initialSize = collectionManager.getCollection().size();
            collectionManager.getCollection().values().removeIf(h -> h.compareTo(reference) < 0);
            int removed = initialSize - collectionManager.getCollection().size();
            return new Response(removed + " items removed");
        } catch (NumberFormatException e) {
            return new Response("ID argument must be a number.");
        }
    }

    @Override
    public String getName() {
        return "remove_lower";
    }

    @Override
    public String getDescription() {
        return "Remove all elements smaller than the element with the given ID. Requires one numeric argument.";
    }
}