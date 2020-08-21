package domain.entities.address;

public class State {
    private final String value;

    public static State state(String value) {
        if (value.length() == 0) {
            throw new IllegalArgumentException("Address state is empty.");
        }

        return new State(value);
    }

    private State(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
