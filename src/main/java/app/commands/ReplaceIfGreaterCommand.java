package app.commands;

import app.collection.CollectionManager;
import app.factory.HumanBeingFactory;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;
import app.util.InputReader;

import java.util.List;

/**
 * replace_if_greater command: Replaces a value by key if the new value is greater than the old one.
 */
public class ReplaceIfGreaterCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;
    private final HumanBeingFactory factory;
    private final InputReader reader;

    public ReplaceIfGreaterCommand(CollectionManager<HumanBeing> collectionManager, HumanBeingFactory factory, InputReader reader) {
        this.collectionManager = collectionManager;
        this.factory = factory;
        this.reader = reader;
    }

    @Override
    public Response execute(Request request) {
        List<String> args = request.args();
        String oldIdStr;
        if (!args.isEmpty()) {
            oldIdStr = args.get(0);
        } else {
            oldIdStr = reader.prompt("Enter id of the old element to replace: ");
        }
        try {
            Long oldId = Long.parseLong(oldIdStr);
            HumanBeing oldHuman = collectionManager.getCollection().get(oldId);
            if (oldHuman == null) {
                return new Response("Element with id=" + oldId + " not found");
            }
            HumanBeing newHuman = factory.createHumanBeing();

            if (newHuman.getId() > oldHuman.getId()) {
                collectionManager.getCollection().remove(oldId);
                collectionManager.getCollection().put(newHuman.getId(), newHuman);
                return new Response("Element replaced successfully, because new id "
                        + newHuman.getId() + " > old id " + oldHuman.getId());
            } else {
                return new Response("The new element's id (" + newHuman.getId()
                        + ") is not greater than the old id (" + oldHuman.getId() + ")");
            }
        } catch (NumberFormatException e) {
            return new Response("The old id must be a number");
        }
    }

    @Override
    public String getName() {
        return "replace_if_greater";
    }

    @Override
    public String getDescription() {
        return "Replace the element if the new element is greater than the current one";
    }
}