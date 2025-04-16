package app.commands;

import app.collection.CollectionManager;
import app.model.HumanBeing;
import app.model.WeaponType;
import app.transfer.Request;
import app.transfer.Response;

import java.util.List;

/**
 * count_less_than_weapon_type command: displays the number of elements
 * whose weaponType field value is less than the specified one.
 */
public class CountLessThanWeaponTypeCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;

    public CountLessThanWeaponTypeCommand(CollectionManager<HumanBeing> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        List<String> args = request.args();
        if (args.isEmpty()) {
            return new Response("You must provide a weaponType argument (HAMMER, AXE, etc).");
        }
        String wtStr = args.get(0);
        try {
            WeaponType wt = WeaponType.valueOf(wtStr.toUpperCase());
            long count = collectionManager.getCollection().values().stream()
                    .filter(h -> h.getWeaponType() != null && h.getWeaponType().compareTo(wt) < 0)
                    .count();
            return new Response("Number of elements with weaponType < " + wt + ": " + count);
        } catch (IllegalArgumentException e) {
            return new Response("Invalid weaponType value. (Possible values: HAMMER, AXE, SHOTGUN, RIFLE, KNIFE)");
        }
    }

    @Override
    public String getName() {
        return "count_less_than_weapon_type";
    }

    @Override
    public String getDescription() {
        return "Displays how many elements have weaponType less than the specified one. Requires one argument.";
    }
}