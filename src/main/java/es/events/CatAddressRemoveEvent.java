package es.events;

import es.reporitory.Event;

public class CatAddressRemoveEvent extends Event {

    private String city;
    private String state;
    private String postcode;

    public static CatAddressRemoveEvent create(String city, String state, String postcode) {
        return new CatAddressRemoveEvent(city, state, postcode);
    }

    private CatAddressRemoveEvent() {
    }

    private CatAddressRemoveEvent(String city, String state, String postcode) {
        super();

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
