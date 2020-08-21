package escqrs.projectors;

import cqrs.repository.CatReadRepository;
import domain.Address;
import domain.CatAddress;
import domain.entities.address.City;
import domain.entities.address.PostCode;
import domain.entities.address.State;
import es.reporitory.Event;
import es.service.CatAddressAddEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CatProjector {

    private CatReadRepository catReadRepository;

    public static CatProjector empty() {
        return new CatProjector();
    }

    public static CatProjector create(CatReadRepository catReadRepository) {
        return new CatProjector(catReadRepository);
    }

    private CatProjector() {
        this.catReadRepository = CatReadRepository.empty();
    }

    private CatProjector(CatReadRepository catReadRepository) {
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
        Address address = Address.create(
                City.city(catAddressAddEvent.getCity()),
                State.state(catAddressAddEvent.getState()),
                PostCode.postCode(catAddressAddEvent.getPostcode()));

        CatAddress catAddress = Optional.ofNullable(catReadRepository.getCatAddress(catId))
                .orElse(CatAddress.create());

        Set<Address> addresses = Optional.ofNullable(catAddress.getAddressByType()
            .get(address.getState())).orElse(new HashSet<>());
        addresses.add(address);
        catAddress.getAddressByType().put(address.getState(), addresses);
        catReadRepository.addCatAddress(catId, catAddress);
    }
}
