package es.service;

import es.reporitory.Event;

public class CatAddressAddEvent extends Event {

    private String city;
    private String state;
    private String postcode;

    public static CatAddressAddEvent create(String city, String state, String postcode) {
        return new CatAddressAddEvent(city, state, postcode);
    }

    private CatAddressAddEvent(String city, String state, String postcode) {
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
