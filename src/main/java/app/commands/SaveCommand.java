package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;
import app.util.FileManager;

/**
 * save command: saves the collection to a file.
 */
public class SaveCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;
    private final FileManager fileManager;

    public SaveCommand(CollectionManager<HumanBeing> collectionManager, FileManager fileManager) {
        this.collectionManager = collectionManager;
        this.fileManager = fileManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            fileManager.saveCollection(collectionManager.getCollection());
            return new Response("Collection saved to file");
        } catch (Exception e) {
            return new Response("Error saving collection: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String getDescription() {
        return "Save collection to file";
    }
}