package domain;

import java.util.HashSet;
import java.util.Set;

public class Cat {
    private String catId;
    private String name;
    private Set<Contact> contacts;
    private Set<Address> addresses;

    public static Cat create(String catId, String name) {
        return new Cat(catId, name);
    }

    private Cat() {
    }

    private Cat(String catId, String name) {
        this.catId = catId;
        this.name = name;
        this.contacts = new HashSet<>();
        this.addresses = new HashSet<>();
    }

    public String getCatId() {
        return catId;
    }

    public String getName() {
        return this.name;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }
}
