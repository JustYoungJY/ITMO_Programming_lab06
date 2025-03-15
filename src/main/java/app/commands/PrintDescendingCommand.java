package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;

/**
 * The print_descending command: Prints the elements of a collection in descending order.
 */
public class PrintDescendingCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;

    public PrintDescendingCommand(CollectionManager<HumanBeing> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (collectionManager.getCollection().isEmpty()) {
            return new Response("Collection is empty");
        }
        StringBuilder sb = new StringBuilder();
        for (HumanBeing human : collectionManager.getElementsDescending()) {
            sb.append(human).append("\n");
        }
        return new Response(sb.toString());
    }

    @Override
    public String getName() {
        return "print_descending";
    }

    @Override
    public String getDescription() {
        return "Display collection elements in descending order";
    }
}