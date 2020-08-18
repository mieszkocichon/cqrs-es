package domain;

public class Address {

    private String city;
    private String state;
    private String postcode;

    public Address(String city, String state, String postcode) {
        this.city = city;
        this.state = state;
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
