package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;

/**
 * insert command: adds a new element with the given key.
 * After entering the command, the user sequentially enters the fields of the new object.
 */
public class InsertCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;

    public InsertCommand(CollectionManager<HumanBeing> cm) {
        this.collectionManager = cm;
    }

    @Override
    public Response execute(Request request) {
        HumanBeing human = request.data();
        if (human == null) {
            return new Response("Error: no HumanBeing object provided for insert.");
        }
        collectionManager.insert(human);
        return new Response("Inserted object with id=" + human.getId());
    }

    @Override
    public String getName() {
        return "insert";
    }

    @Override
    public String getDescription() {
        return "Insert a new element from request.data()";
    }
}