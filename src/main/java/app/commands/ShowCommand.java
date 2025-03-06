package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;

/**
 * show command: Displays all the elements of a collection in string representation.
 */
public class ShowCommand implements Command {
    CollectionManager<HumanBeing> collectionManager;

    public ShowCommand(CollectionManager<HumanBeing> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (collectionManager.getElements().isEmpty()) {
            return new Response("Collection is empty", null, null);
        }
        StringBuffer sb = new StringBuffer();
        for (HumanBeing human : collectionManager.getElements()) {
            sb.append(human).append("\n");
        }
        return new Response(sb.toString(), null, null);
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