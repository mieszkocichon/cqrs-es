package es.service;

import domain.Address;
import domain.Cat;
import domain.Contact;
import es.events.CatAddressRemoveEvent;
import es.events.CatCreatedEvent;
import es.reporitory.EventStore;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CatService {

    private EventStore repository;

    public static CatService create(EventStore repository) {
        return new CatService(repository);
    }

    private CatService() {
    }

    private CatService(EventStore repository) {
        this.repository = repository;
    }

    public void createCat(String catId, String name) {
        this.repository.addEvent(catId, CatCreatedEvent.create(catId, name));
    }

    public void updateCat(String catId, Set<Contact> contacts, Set<Address> addresses) throws Exception {
        Cat cat = CatUtility.recreateCatState(this.repository, catId);

        Optional.ofNullable(cat)
                .orElseThrow(() -> new Exception("Cat does not exist."))
                .getAddresses()
                .stream()
                .filter(addresses::contains)
                .forEach(a -> this.repository.addEvent(catId, CatAddressRemoveEvent.create(a.getCity(), a.getState(), a.getPostcode())));

        addresses.stream()
                .filter(address -> !cat.getAddresses().contains(address))
                .forEach(a -> repository.addEvent(catId, CatAddressAddEvent.create(a.getCity(), a.getState(), a.getPostcode())));
    }

    public Set<Address> getAddressByRegion(String catId, String state) throws Exception {
        Cat cat = CatUtility.recreateCatState(this.repository, catId);

        Optional.ofNullable(cat).orElseThrow(() -> new Exception("Cat doesn't exist."));

        return cat.getAddresses().stream().filter(address -> address.getState().equals(state)).collect(Collectors.toSet());
    }
}
