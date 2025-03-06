package app.model;

import java.time.ZonedDateTime;

/**
 * The HumanBeing class describes an entity with a given set of fields.
 */
public class HumanBeing implements Comparable<HumanBeing> {
    private long id; //The field value must be greater than 0
    private String name; //Field cannot be null, String cannot be empty
    private Coordinates coordinates; //Field cannot be null
    private java.time.ZonedDateTime creationDate; //Field cannot be null
    private Boolean realHero; //Field cannot be null
    private Boolean hasToothpick; //Field may be null
    private int impactSpeed;
    private String soundtrackName; //Field cannot be null
    private WeaponType weaponType; //Field may be null
    private Mood mood; //Field cannot be null
    private Car car; //Field may be null


    // Getters and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (id <= 0) throw new IllegalArgumentException("id must be greater than 0");
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("The name cannot be empty");
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) throw new IllegalArgumentException("Coordinates cannot be null");
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        if (creationDate == null) throw new IllegalArgumentException("creationDate cannot be null");
        this.creationDate = creationDate;
    }

    public Boolean getRealHero() {
        return realHero;
    }

    public void setRealHero(Boolean realHero) {
        if (realHero == null) throw new IllegalArgumentException("realHero cannot be null");
        this.realHero = realHero;
    }

    public Boolean getHasToothpick() {
        return hasToothpick;
    }

    public void setHasToothpick(Boolean hasToothpick) {
        this.hasToothpick = hasToothpick;
    }

    public int getImpactSpeed() {
        return impactSpeed;
    }

    public void setImpactSpeed(int impactSpeed) {
        this.impactSpeed = impactSpeed;
    }

    public String getSoundtrackName() {
        return soundtrackName;
    }

    public void setSoundtrackName(String soundtrackName) {
        if (soundtrackName == null || soundtrackName.trim().isEmpty())
            throw new IllegalArgumentException("soundtrackName cannot be empty");
        this.soundtrackName = soundtrackName;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        if (mood == null) throw new IllegalArgumentException("mood cannot be null");
        this.mood = mood;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public int compareTo(HumanBeing object) {
        return Long.compare(this.id, object.id);
    }

    @Override
    public String toString() {
        return "HumanBeing{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", realHero=" + realHero +
                ", hasToothpick=" + hasToothpick +
                ", impactSpeed=" + impactSpeed +
                ", soundtrackName='" + soundtrackName + '\'' +
                ", weaponType=" + weaponType +
                ", mood=" + mood +
                ", car=" + car +
                '}';
    }
}