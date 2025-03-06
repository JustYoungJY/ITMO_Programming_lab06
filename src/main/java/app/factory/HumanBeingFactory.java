package app.factory;

import app.model.*;
import app.util.InputReader;

import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * A class for creating objects of the HumanBeing type.
 * When called, createHumanBeing() prompts the user for fields.
 */
public class HumanBeingFactory {
    private static final AtomicLong idGenerator = new AtomicLong(1);
    private final InputReader reader;

    public HumanBeingFactory(InputReader reader) {
        this.reader = reader;
    }

    private <T> T readField(String prompt, Function<String, T> function, boolean allowNull) {
        while (true) {
            String input = reader.prompt(prompt + ": ");
            if (allowNull && input.isEmpty()) {
                return null;
            }
            try {
                return function.apply(input);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + ". Please try again.");
            }
        }
    }

    public HumanBeing createHumanBeing() {
        HumanBeing human = new HumanBeing();
        // Automatically generated fields
        human.setId(idGenerator.getAndIncrement());
        human.setCreationDate(ZonedDateTime.now());

        // Read required fields with validation
        human.setName(readField("Enter name", s -> {
            if (s.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
            return s;
        }, false));

        System.out.println("Enter coordinates:");
        Double x = readField("Enter x (Double)", Double::valueOf, false);
        long y = readField("Enter y (long, max 207)", s -> {
            long value = Long.parseLong(s);
            if (value > 207) throw new IllegalArgumentException("y must not exceed 207");
            return value;
        }, false);
        human.setCoordinates(new Coordinates(x, y));

        human.setRealHero(readField("Is real hero (true/false)", Boolean::valueOf, false));
        human.setHasToothpick(readField("Has toothpick (true/false, leave empty for null)", Boolean::valueOf, true));
        human.setImpactSpeed(readField("Enter impactSpeed (integer)", Integer::valueOf, false));
        human.setSoundtrackName(readField("Enter soundtrackName", s -> {
            if (s.trim().isEmpty()) throw new IllegalArgumentException("soundtrackName cannot be empty");
            return s;
        }, false));

        System.out.println("Available weapon types: " + WeaponType.getAllNames());
        human.setWeaponType(readField("Enter weaponType (leave empty for null)", s -> {
            if (s.trim().isEmpty()) return null;
            return WeaponType.valueOf(s.toUpperCase());
        }, true));

        System.out.println("Available moods: " + Mood.getAllNames());
        human.setMood(readField("Enter mood", s -> Mood.valueOf(s.toUpperCase()), false));

        human.setCar(readField("Is car cool? (true/false, leave empty for null)", s -> {
            if (s.trim().isEmpty()) return null;
            return new Car(Boolean.parseBoolean(s));
        }, true));

        return human;
    }
}