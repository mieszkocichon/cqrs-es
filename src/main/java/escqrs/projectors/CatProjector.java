package escqrs.projectors;

import cqrs.repository.CatReadRepository;
import domain.Address;
import domain.CatAddress;
import es.events.CatUpdateCatCommand;
import es.reporitory.Event;
import es.service.CatAddressAddEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CatProjector {

    CatReadRepository catReadRepository;

    public CatProjector() {
        this.catReadRepository = new CatReadRepository();
    }

    public CatProjector(CatReadRepository catReadRepository) {
        this.catReadRepository = catReadRepository;
    }

    public void project(String catId, List<Event> events) {
        for (Event event : events) {
            if (event instanceof CatAddressAddEvent) {
                apply(catId, (CatAddressAddEvent) event);
            }
        }
    }

    public void apply(String catId, CatAddressAddEvent catAddressAddEvent) {
        Address address = new Address(
                catAddressAddEvent.getCity(),
                catAddressAddEvent.getState(),
                catAddressAddEvent.getPostcode());

        CatAddress catAddress = Optional.ofNullable(catReadRepository.getCatAddress(catId))
                .orElse(new CatAddress());

        Set<Address> addresses = Optional.ofNullable(catAddress.getAddressByType()
            .get(address.getState())).orElse(new HashSet<>());
        addresses.add(address);
        catAddress.getAddressByType().put(address.getState(), addresses);
        catReadRepository.addCatAddress(catId, catAddress);
    }
}
