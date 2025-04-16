package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;

import java.util.List;

/**
 * replace_if_greater command: Replaces a value by key if the new value is greater than the old one.
 */
public class ReplaceIfGreaterCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;

    public ReplaceIfGreaterCommand(CollectionManager<HumanBeing> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        List<String> args = request.args();
        if (args.isEmpty()) {
            return new Response("Error: you must provide the old id argument for replace_if_greater.");
        }
        String oldIdStr = args.get(0);
        long oldId;
        try {
            oldId = Long.parseLong(oldIdStr);
        } catch (NumberFormatException e) {
            return new Response("Error: the old id must be a number.");
        }

        HumanBeing oldHuman = collectionManager.getCollection().get(oldId);
        if (oldHuman == null) {
            return new Response("No element with id=" + oldId + " found in the collection.");
        }

        HumanBeing newHuman = request.data();
        if (newHuman == null) {
            return new Response("Error: no HumanBeing object provided for replace_if_greater.");
        }

        if (newHuman.compareTo(oldHuman) > 0) {
            // remove old
            collectionManager.getCollection().remove(oldId);
            // insert new by newHuman's id
            collectionManager.getCollection().put(newHuman.getId(), newHuman);
            return new Response("Element replaced: new id=" + newHuman.getId()
                    + " > old id=" + oldHuman.getId());
        } else {
            return new Response("New element's id=" + newHuman.getId()
                    + " is not greater than old id=" + oldHuman.getId());
        }
    }

    @Override
    public String getName() {
        return "replace_if_greater";
    }

    @Override
    public String getDescription() {
        return "Replaces the old element if the newly-provided object is greater. Requires an oldId argument and a new HumanBeing in request.data().";
    }
}