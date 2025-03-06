package app.commands;

import app.collection.CollectionManager;
import app.factory.HumanBeingFactory;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;
import app.util.InputReader;

/**
 * remove_lower command: Removes from the collection all elements less than the given one.
 */
public class RemoveLowerCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;
    private final HumanBeingFactory factory;
    private final InputReader reader;

    public RemoveLowerCommand(CollectionManager<HumanBeing> collectionManager, HumanBeingFactory factory, InputReader reader) {
        this.collectionManager = collectionManager;
        this.factory = factory;
        this.reader = reader;
    }

    @Override
    public Response execute(Request request) {
        HumanBeing reference = factory.createHumanBeing();
        int initialSize = collectionManager.getCollection().size();
        collectionManager.getCollection().values().removeIf(human -> human.compareTo(reference) < 0);
        int removed = initialSize - collectionManager.getCollection().size();
        return new Response(removed + "items removed", null, null);
    }

    @Override
    public String getName() {
        return "remove_lower";
    }

    @Override
    public String getDescription() {
        return "Remove all elements from a collection smaller than a given one";
    }
}