package app.commands;

import app.collection.CollectionManager;
import app.factory.HumanBeingFactory;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;
import app.util.InputReader;

import java.util.List;

/**
 * update command: updates the value of a collection element whose id is equal to the specified one.
 */
public class UpdateCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;

    public UpdateCommand(CollectionManager<HumanBeing> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        HumanBeing newHuman = request.data();
        if (newHuman == null) {
            return new Response("Error: no HumanBeing object provided for update.");
        }
        long id = newHuman.getId();
        boolean updated = collectionManager.updateById(id, newHuman);
        if (updated) {
            return new Response("Element updated successfully");
        } else {
            return new Response("Element with id " + id + " not found");
        }
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "Update the element in the collection with the provided HumanBeing";
    }
}