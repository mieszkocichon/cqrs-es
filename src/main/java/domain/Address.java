package domain;

import domain.entities.address.City;
import domain.entities.address.PostCode;
import domain.entities.address.State;

import java.time.Instant;

public class Address {

    private String city;
    private String state;
    private String postcode;
    private Instant occuredAt;

    public static Address create(City city, State state, PostCode postcode) {
        return new Address(city, state, postcode, Instant.now());
    }

    public static Address createByTime(City city, State state, PostCode postcode, Instant occuredAd)
    {
        return new Address(city, state, postcode, occuredAd);
    }

    private Address(City city, State state, PostCode postcode, Instant occuredAt) {
        this.city = city.getValue();
        this.state = state.getValue();
        this.postcode = PostCode.getValue();
        this.occuredAt = occuredAt;
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

    public Instant getOccuredAt() {
        return occuredAt;
    }

    public void setOccuredAt(Instant occuredAt) {
        this.occuredAt = occuredAt;
    }
}
