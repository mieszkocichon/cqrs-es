package domain.entities.address;

public class PostCode {
    private static String value;

    public static PostCode postCode(String value) {
        if (value.length() == 0) {
            throw new IllegalArgumentException("Post code is empty.");
        }
        return new PostCode(value);
    }

    public PostCode(String value) {
        this.value = value;
    }

    public static String getValue() {
        return value;
    }
}
