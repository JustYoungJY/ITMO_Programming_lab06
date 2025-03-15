package app.transfer;

/**
 * Response record encapsulates the result of executing a command.
 */
public record Response(String message) {
    public static Response empty() {
        return new Response(null);
    }

}
