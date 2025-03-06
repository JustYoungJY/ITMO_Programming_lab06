package app.transfer;

import app.model.HumanBeing;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Response record encapsulates the result of executing a command.
 */
public record Response(String message, List<HumanBeing> persons, String script) {
    public Response(final String message, final List<HumanBeing> persons) {
        this(message, persons, null);
    }

    public Response(final String message, final HumanBeing... persons) {
        this(message, Arrays.asList(persons), null);
    }

    public Response(final String message) {
        this(message, Collections.emptyList(), null);
    }

    public static Response empty() {
        return new Response(null);
    }

}
