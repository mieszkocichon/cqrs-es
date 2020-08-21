package domain.entities.address;

public class City {
    private final String value;

    public static City city(String value) {
        if (value.length() == 0) {
            throw new IllegalArgumentException("Address city is empty.");
        }
        return new City(value);
    }

    private City(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
