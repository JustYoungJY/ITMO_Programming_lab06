package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.model.WeaponType;
import app.transfer.Request;
import app.transfer.Response;
import app.util.InputReader;

import java.util.List;

/**
 * count_less_than_weapon_type command: displays the number of elements
 * whose weaponType field value is less than the specified one.
 */
public class CountLessThanWeaponTypeCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;
    private final InputReader reader;

    public CountLessThanWeaponTypeCommand(CollectionManager<HumanBeing> collectionManager, InputReader reader) {
        this.collectionManager = collectionManager;
        this.reader = reader;
    }

    @Override
    public Response execute(Request request) {
        List<String> args = request.args();
        String wtStr;
        if (!args.isEmpty()) {
            wtStr = args.get(0);
        } else {
            wtStr = reader.prompt("Enter weaponType: ");
        }
        try {
            WeaponType wt = WeaponType.valueOf(wtStr.toUpperCase());
            long count = collectionManager.getCollection().values().stream()
                    .filter(human -> human.getWeaponType() != null && human.getWeaponType().compareTo(wt) < 0)
                    .count();
            return new Response("Number of elements with weaponType less than " + wt + ": " + count);
        } catch (IllegalArgumentException e) {
            return new Response("Invalid weaponType value");
        }
    }

    @Override
    public String getName() {
        return "count_less_than_weapon_type";
    }

    @Override
    public String getDescription() {
        return "Display the number of elements whose weaponType field value is less than the specified one";
    }
}
