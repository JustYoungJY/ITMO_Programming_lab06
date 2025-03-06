package app.model;

/**
 * Enumeration of sentiments.
 */
public enum Mood {
    SORROW,
    CALM,
    RAGE;

    public static String getAllNames() {
        StringBuilder sb = new StringBuilder();
        for (Mood md : values()) {
            sb.append(md.name()).append(" ");
        }
        return sb.toString().trim();
    }
}