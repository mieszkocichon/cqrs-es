package es.events;

import domain.Address;
import domain.Contact;
import es.reporitory.Event;

import java.util.Set;

public class CatUpdateCatCommand extends Event {

    private String catId;
    private Set<Address> addresses;
    private Set<Contact> contacts;

    private CatUpdateCatCommand() {
    }

    public CatUpdateCatCommand(String catId, Set<Address> addresses, Set<Contact> contacts) {
        this.catId = catId;
        this.addresses = addresses;
        this.contacts = contacts;
    }
}
