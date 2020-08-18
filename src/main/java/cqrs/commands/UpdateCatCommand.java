package cqrs.commands;

import domain.Address;
import domain.Contact;

import java.util.Set;

public class UpdateCatCommand {
    private String catId;
    private Set<Address> addresses;
    private Set<Contact> contacts;

    private UpdateCatCommand() {
    }

    public UpdateCatCommand(String catId, Set<Address> addresses, Set<Contact> contacts) {
        this.catId = catId;
        this.addresses = addresses;
        this.contacts = contacts;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }
}
