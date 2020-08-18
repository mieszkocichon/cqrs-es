package es.service;

import domain.Address;
import domain.Cat;
import es.events.CatCreatedEvent;
import es.events.CatUpdateCatCommand;
import es.reporitory.Event;
import es.reporitory.EventStore;

import java.util.List;
import java.util.UUID;

public class CatUtility {
    public static Cat recreateCatState(EventStore repository, String catId) {
        Cat cat = null;

        List<Event> events = repository.getEvents(catId);
        for (Event event : events) {
            if (event instanceof CatCreatedEvent) {
                CatCreatedEvent catCreatedEvent = (CatCreatedEvent) event;
                cat = new Cat(UUID.randomUUID().toString(), catCreatedEvent.getName());
            }
            if (event instanceof CatAddressAddEvent) {
                CatAddressAddEvent catAddressAddEvent = (CatAddressAddEvent) event;
                if (cat != null) {
                    cat.getAddresses().add(
                            new Address(catAddressAddEvent.getCity(),
                                    catAddressAddEvent.getState(), catAddressAddEvent.getPostcode())
                    );
                }
            }
        }
        return cat;
    }
}
