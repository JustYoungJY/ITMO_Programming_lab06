package app.commands;

import app.collection.CollectionManager;
import app.factory.HumanBeingFactory;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;
import app.util.InputReader;

import java.util.List;

/**
 * insert command: adds a new element with the given key.
 * After entering the command, the user sequentially enters the fields of the new object.
 */
public class InsertCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;

    public InsertCommand(CollectionManager<HumanBeing> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        HumanBeing human = request.data();
        if (human == null) {
            return new Response("Error: no HumanBeing object provided for insert.");
        }
        collectionManager.insert(human);
        return new Response("Item added (or replaced if id already existed).");
    }

    @Override
    public String getName() {
        return "insert";
    }

    @Override
    public String getDescription() {
        return "Insert a new element with the provided HumanBeing in request.data";
    }
}