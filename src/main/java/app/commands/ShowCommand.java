package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;

/**
 * show command: Displays all the elements of a collection in string representation.
 */
public class ShowCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;

    public ShowCommand(CollectionManager<HumanBeing> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (collectionManager.getElements().isEmpty()) {
            return new Response("Collection is empty");
        }
        StringBuilder sb = new StringBuilder();
        for (HumanBeing human : collectionManager.getElements()) {
            sb.append(human).append("\n");
        }
        return new Response(sb.toString());
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescription() {
        return "Display all elements of a collection in string representation";
    }
}