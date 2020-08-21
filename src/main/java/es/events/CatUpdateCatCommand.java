package es.events;

import domain.Address;
import domain.Contact;
import es.reporitory.Event;

import java.util.Set;

public class CatUpdateCatCommand extends Event {

    private String catId;
    private Set<Address> addresses;
    private Set<Contact> contacts;

    public static CatUpdateCatCommand create(String catId, Set<Address> addresses, Set<Contact> contacts) {
        return new CatUpdateCatCommand(catId, addresses, contacts);
    }

    private CatUpdateCatCommand() {
    }

    private CatUpdateCatCommand(String catId, Set<Address> addresses, Set<Contact> contacts) {
        this.catId = catId;
        this.addresses = addresses;
        this.contacts = contacts;
    }
}
