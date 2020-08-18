package es.service;

import domain.Address;
import domain.Cat;
import domain.CatAddress;
import domain.Contact;
import es.events.CatAddressRemoveEvent;
import es.events.CatCreatedEvent;
import es.reporitory.EventStore;

import java.util.Set;
import java.util.stream.Collectors;

public class CatService {

    EventStore repository;

    private CatService() {
    }

    public CatService(EventStore repository) {
        this.repository = repository;
    }

    public void createCat(String catId, String name) {
        this.repository.addEvent(catId, new CatCreatedEvent(catId, name));
    }

    public void updateCat(String catId, Set<Contact> contacts, Set<Address> addresses) throws Exception {
        Cat cat = CatUtility.recreateCatState(this.repository, catId);

        if (cat == null) {
            throw new Exception("Cat does not exist.");
        }

        cat.getAddresses()
                .stream()
                .filter(addresses::contains)
                .forEach(a -> this.repository.addEvent(catId, new CatAddressRemoveEvent(a.getCity(), a.getState(), a.getPostcode())));

        addresses.stream()
                .filter(a -> !cat.getAddresses().contains(a))
                .forEach(a -> repository.addEvent(catId, new CatAddressAddEvent(a.getCity(), a.getState(), a.getPostcode())));
    }

    public Set<Address> getAddressByRegion(String catId, String state) throws Exception {
        Cat cat = CatUtility.recreateCatState(this.repository, catId);
        if (cat == null) {
            throw new Exception("Cat doesn't exist.");
        }
        return cat.getAddresses().stream().filter(address -> address.getState().equals(state)).collect(Collectors.toSet());
    }
}
