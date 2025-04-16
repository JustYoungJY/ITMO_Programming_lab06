package app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Coordinates class contains coordinates.
 */
public class Coordinates {
    private final Double x;
    private final long y;

    public Coordinates(@JsonProperty("x") Double x, @JsonProperty("y") long y) {
        if (x == null) throw new IllegalArgumentException("x cannot be null");
        if (y > 207) throw new IllegalArgumentException("y must not exceed 207");
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}