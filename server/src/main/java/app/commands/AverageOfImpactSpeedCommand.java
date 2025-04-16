package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;

/**
 * average_of_impact_speed command: Displays the average value of the impactSpeed field for all items in the collection.
 */
public class AverageOfImpactSpeedCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;

    public AverageOfImpactSpeedCommand(CollectionManager<HumanBeing> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (collectionManager.getCollection().isEmpty()) {
            return new Response("Collection is empty.");
        }
        double avg = collectionManager.getCollection().values().stream()
                .mapToInt(HumanBeing::getImpactSpeed)
                .average().orElse(0);
        return new Response("Average impactSpeed: " + avg);
    }

    @Override
    public String getName() {
        return "average_of_impact_speed";
    }

    @Override
    public String getDescription() {
        return "Displays the average impactSpeed of all elements in the collection. No arguments required.";
    }
}