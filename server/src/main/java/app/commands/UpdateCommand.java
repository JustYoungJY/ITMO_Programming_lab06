package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;

/**
 * update command: updates the value of a collection element whose id is equal to the specified one.
 */
public class UpdateCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;

    public UpdateCommand(CollectionManager<HumanBeing> cm) {
        this.collectionManager = cm;
    }

    @Override
    public Response execute(Request request) {
        HumanBeing newHuman = request.data();
        if (newHuman == null) {
            return new Response("Error: no HumanBeing provided for update.");
        }
        long id = newHuman.getId();
        boolean updated = collectionManager.updateById(id, newHuman);
        if (updated) {
            return new Response("Element with id=" + id + " updated.");
        } else {
            return new Response("No element with id=" + id + " found.");
        }
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "Update an existing element from request.data()";
    }
}