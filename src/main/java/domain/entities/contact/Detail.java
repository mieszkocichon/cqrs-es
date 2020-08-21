package domain.entities.contact;

public class Detail {
    private final String value;

    public static Detail detail(String value) {
        if (value.length() == 0) {
            throw new IllegalArgumentException("Contact detail is empty.");
        }
        return new Detail(value);
    }

    private Detail(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
