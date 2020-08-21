package domain;

import domain.entities.address.City;
import domain.entities.address.PostCode;
import domain.entities.address.State;

public class Address {

    private String city;
    private String state;
    private String postcode;

    public static Address create(City city, State state, PostCode postcode) {
        return new Address(city, state, postcode);
    }

    private Address(City city, State state, PostCode postcode) {
        this.city = city.getValue();
        this.state = state.getValue();
        this.postcode = postcode.getValue();
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
