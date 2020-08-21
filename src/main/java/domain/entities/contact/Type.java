package domain.entities.contact;

public class Type {
    private final String value;

    public static Type type(String value) {
        if (value.length() == 0) {
            throw new IllegalArgumentException("Contact type is empty.");
        }
        return new Type(value);
    }

    private Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
