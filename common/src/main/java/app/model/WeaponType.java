package app.model;

/**
 * Listing of weapons
 */
public enum WeaponType {
    HAMMER,
    AXE,
    SHOTGUN,
    RIFLE,
    KNIFE;

    public static String getAllNames() {
        StringBuilder sb = new StringBuilder();
        for (WeaponType wt : values()) {
            sb.append(wt.name()).append(" ");
        }
        return sb.toString().trim();
    }
}