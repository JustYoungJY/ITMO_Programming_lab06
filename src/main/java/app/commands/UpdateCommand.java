package app.commands;

import app.collection.CollectionManager;
import app.factory.HumanBeingFactory;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;
import app.util.InputReader;

/**
 * update command: updates the value of a collection element whose id is equal to the specified one.
 */
public class UpdateCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;
    private final InputReader reader;
    private final HumanBeingFactory factory;

    public UpdateCommand(CollectionManager<HumanBeing> collectionManager, HumanBeingFactory factory, InputReader reader) {
        this.collectionManager = collectionManager;
        this.reader = reader;
        this.factory = factory;
    }

    @Override
    public Response execute(Request request) {
        String idStr;
        if (!request.args().isEmpty()) {
            idStr = request.args().get(0);
        } else {
            idStr = reader.prompt("Enter id of element to update: ");
        }
        try {
            long id = Long.parseLong(idStr);
            HumanBeing newHuman = factory.createHumanBeing();
            boolean update = collectionManager.updateById(id, newHuman);
            if (update) {
                return new Response("Element updated successfully", null, null);
            } else {
                return new Response("Element with this id not found", null, null);
            }
        } catch (NumberFormatException e) {
            return new Response("Id must be a number", null, null);
        }
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "update the value of a collection element whose id is equal to a given one";
    }
}