package app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Car class contains a sign of the presence of a “cool” car.
 */
public class Car {

    private final boolean cool;

    public Car(@JsonProperty("cool")boolean cool) {
        this.cool = cool;
    }

    public boolean isCool() {
        return cool;
    }

    @Override
    public String toString() {
        return "{" +
                "cool=" + cool +
                '}';
    }
}