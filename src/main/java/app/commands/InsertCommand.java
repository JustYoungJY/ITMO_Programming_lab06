package app.commands;

import app.collection.CollectionManager;
import app.factory.HumanBeingFactory;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;
import app.util.InputReader;

/**
 * insert command: adds a new element with the given key.
 * After entering the command, the user sequentially enters the fields of the new object.
 */
public class InsertCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;
    private final HumanBeingFactory factory;
    private final InputReader reader;

    public InsertCommand(CollectionManager<HumanBeing> collectionManager, HumanBeingFactory factory, InputReader reader) {
        this.collectionManager = collectionManager;
        this.factory = factory;
        this.reader = reader;
    }

    @Override
    public Response execute(Request request) {
        HumanBeing human = factory.createHumanBeing();
        collectionManager.insert(human);  // ключ = human.getId()
        return new Response("Item added (or replaced if id already existed)");
    }

    @Override
    public String getName() {
        return "insert";
    }

    @Override
    public String getDescription() {
        return "Add a new element with a given key";
    }

}