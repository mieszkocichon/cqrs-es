package domain.entities.contact;

public class Type {
    private final ContactType value;

    public static Type type(ContactType value) {
        return new Type(value);
    }

    private Type(ContactType value) {
        this.value = value;
    }

    public ContactType getValue() {
        return value;
    }
}
