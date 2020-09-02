package escqrs.aggregates;

import cqrs.commands.CreateCatCommand;
import cqrs.commands.UpdateCatCommand;
import domain.Address;
import domain.Cat;
import es.events.CatCreatedEvent;
import es.reporitory.Event;
import es.reporitory.EventStore;
import es.service.CatAddressAddEvent;
import es.service.CatUtility;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CatAggregate {

    private EventStore writeRepository;

    public static CatAggregate empty() {
        return new CatAggregate();
    }

    public static CatAggregate create(EventStore eventStore) {
        return new CatAggregate(eventStore);
    }

    private CatAggregate() {
        this.writeRepository = EventStore.empty();
    }

    private CatAggregate(EventStore eventStore) {
        this.writeRepository = eventStore;
    }

    public List<Event> handleCreateCatCommand(CreateCatCommand createCatCommand) {
        CatCreatedEvent catCreatedEvent = CatCreatedEvent.create(createCatCommand.getCatId(), createCatCommand.getName());

        this.writeRepository.addEvent(catCreatedEvent.getCatId(), catCreatedEvent);

        return Collections.singletonList(catCreatedEvent);
    }

    public List<Event> handleUpdateCatCommand(UpdateCatCommand updateCatCommand) {
        Cat cat = CatUtility.recreateCatState(writeRepository, updateCatCommand.getCatId());
        return updateCatCommand(updateCatCommand, cat);
    }

    private List<Event> updateCatCommand(UpdateCatCommand updateCatCommand, Cat cat) {
        List<Event> events = new LinkedList<>();

        List<Address> addresses = updateCatCommand.getAddresses().stream()
                .filter(address -> !cat.getAddresses().contains(address))
                .collect(Collectors.toList());
        for (Address address : addresses) {
            CatAddressAddEvent catAddressAddEvent = CatAddressAddEvent.create(address.getCity(), address.getState(), address.getPostcode());
            events.add(catAddressAddEvent);
            writeRepository.addEvent(updateCatCommand.getCatId(), catAddressAddEvent);
        }

        return events;
    }
}
