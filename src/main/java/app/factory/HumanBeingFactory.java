package app.factory;

import app.model.*;
import app.util.InputReader;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * A class for creating objects of the HumanBeing type.
 * When called, createHumanBeing() prompts the user for fields.
 */
public class HumanBeingFactory {
    private final InputReader reader;

    public HumanBeingFactory(InputReader reader) {
        this.reader = reader;
    }

    private <T> T readField(String prompt, Function<String, T> parser, boolean allowNull) {
        while (true) {
            String input = reader.prompt(prompt + ": ");
            if (input.isBlank() && allowNull) {
                return null;
            }
            try {
                return parser.apply(input);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + ". Please try again.");
            }
        }
    }

    public HumanBeing createHumanBeing() {
        HumanBeing human = new HumanBeing();

        Long userId = readField("Enter id (long, must be > 0)", Long::valueOf, false);
        if (userId <= 0) {
            throw new IllegalArgumentException("id must be > 0");
        }
        human.setId(userId);

        human.setCreationDate(ZonedDateTime.now());

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