package escqrs;

import cqrs.commands.UpdateCatCommand;
import cqrs.queries.AddressByRegionQuery;
import domain.Address;
import domain.Contact;
import domain.entities.address.City;
import domain.entities.address.PostCode;
import domain.entities.address.State;
import domain.entities.contact.ContactType;
import domain.entities.contact.Detail;
import domain.entities.contact.Type;
import escqrs.aggregates.CatAggregate;
import cqrs.commands.CreateCatCommand;
import cqrs.projections.CatProjection;
import escqrs.projectors.CatProjector;
import cqrs.repository.CatReadRepository;
import es.reporitory.Event;
import es.reporitory.EventStore;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ApplicationUnitTest {

    private EventStore writeRepository;
    private CatReadRepository catReadRepository;
    private CatProjector catProjector;
    private CatAggregate catAggregate;
    private CatProjection catProjection;

    @Before
    public void setUp() {
        this.writeRepository = EventStore.empty();
        this.catReadRepository = CatReadRepository.empty();
        this.catProjector = CatProjector.create(this.catReadRepository);
        this.catAggregate = CatAggregate.create(this.writeRepository);
        this.catProjection = CatProjection.create(this.catReadRepository);
    }

    @Test
    public void givenCQRSApplication_whenCommandRun_thenQueryShouldReturnResult() throws Exception {
        String catId = UUID.randomUUID().toString();

        List<Event> events = null;
        CreateCatCommand createCatCommand = CreateCatCommand.create(catId, "Kitek");
        events = this.catAggregate.handleCreateCatCommand(createCatCommand);

        catProjector.project(catId, events);

        UpdateCatCommand updateCatCommand = UpdateCatCommand.create(
                catId,

                Stream.of(
                        Address.create(City.city("New York"),
                                State.state("NY"),
                                PostCode.postCode("10001")),

                        Address.create(City.city("Los Angeles"),
                                State.state("CA"),
                                PostCode.postCode("90001")))

                        .collect(Collectors.toSet()),

                Stream.of(
                        Contact.create(Type.type(ContactType.EMAIL),
                                Detail.detail("john.smith@o2.com")),

                        Contact.create(Type.type(ContactType.EMAIL),
                                Detail.detail("john.smith@jiu.com")))

                        .collect(Collectors.toSet())
        );
        events = catAggregate.handleUpdateCatCommand(updateCatCommand);
        catProjector.project(catId, events);

        updateCatCommand = UpdateCatCommand.create(
                catId,

                Stream.of(
                        Address.create(City.city("New York2"),
                                State.state("NY"),
                                PostCode.postCode("10002")),

                        Address.create(City.city("Las Vegas"),
                                State.state("LV"),
                                PostCode.postCode("30001"))

                ).collect(Collectors.toSet()),

                Stream.of(
                        Contact.create(Type.type(ContactType.EMAIL),
                                Detail.detail("john.smith@o2.com")),
                        Contact.create(Type.type(ContactType.EMAIL),
                                Detail.detail("john.smith@out.com"))
                ).collect(Collectors.toSet())
        );
        events = catAggregate.handleUpdateCatCommand(updateCatCommand);
        catProjector.project(catId, events);

//        ContactByTypeQuery contactByTypeQuery = new ContactByTypeQuery(catId, "EMAIL");

        AddressByRegionQuery addressByRegionQuery = AddressByRegionQuery.create(catId, "LV");
        Iterator<Address> addressIterator = catProjection.handle(addressByRegionQuery).iterator();
        Address address = addressIterator.next();
        assertNotEquals(address.getCity(), "New York");
        assertEquals(address.getCity(), "Las Vegas");
        assertEquals(address.getState(), "LV");
        assertEquals(address.getPostcode(), "30001");
    }
}