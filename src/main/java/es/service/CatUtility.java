package es.service;

import domain.Address;
import domain.Cat;
import domain.entities.address.City;
import domain.entities.address.PostCode;
import domain.entities.address.State;
import es.events.CatCreatedEvent;
import es.reporitory.Event;
import es.reporitory.EventStore;

import java.util.List;
import java.util.UUID;

public class CatUtility {

    public static CatUtility empty() {
        return new CatUtility();
    }

    private CatUtility() {
    }

    public static Cat recreateCatState(EventStore repository, String catId) {
        return CatUtility.recreate(repository.getEvents(catId));
    }

    private static Cat recreate(List<Event> events) {
        Cat cat = null;

        for (Event event : events) {
            if (event instanceof CatCreatedEvent) {
                CatCreatedEvent catCreatedEvent = (CatCreatedEvent) event;
                cat = Cat.create(UUID.randomUUID().toString(), catCreatedEvent.getName());
            }
            if (event instanceof CatAddressAddEvent) {
                CatAddressAddEvent catAddressAddEvent = (CatAddressAddEvent) event;
                if (cat != null) {
                    cat.getAddresses().add(
                            Address.create(City.city(catAddressAddEvent.getCity()),
                                    State.state(catAddressAddEvent.getState()),
                                    PostCode.postCode(catAddressAddEvent.getPostcode()))
                    );
                }
            }
        }
        return cat;
    }
}
