package escqrs;


import cqrs.commands.UpdateCatCommand;
import cqrs.queries.AddressByRegionQuery;
import domain.Address;
import domain.Contact;
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
        this.writeRepository = new EventStore();
        this.catReadRepository = new CatReadRepository();
        this.catProjector = new CatProjector(this.catReadRepository);
        this.catAggregate = new CatAggregate(this.writeRepository);
        this.catProjection = new CatProjection(this.catReadRepository);
    }

    @Test
    public void givenCQRSApplication_whenCommandRun_thenQueryShouldReturnResult() throws Exception {
        String catId = UUID.randomUUID().toString();

        List<Event> events = null;
        CreateCatCommand createCatCommand = new CreateCatCommand(catId, "Kitek");
        events = this.catAggregate.handleCreateCatCommand(createCatCommand);

        catProjector.project(catId, events);

        UpdateCatCommand updateCatCommand = new UpdateCatCommand(
                catId,

                Stream.of(
                        new Address("New York", "NY", "10001"),
                        new Address("Los Angeles", "CA", "90001")).collect(Collectors.toSet()),

                Stream.of(
                        new Contact("EMAIL", "john.smith@o2.com"),
                        new Contact("EMAIL", "john.smith@jiu.com")).collect(Collectors.toSet())
        );
        events = catAggregate.handleUpdateCatCommand(updateCatCommand);
        catProjector.project(catId, events);

        updateCatCommand = new UpdateCatCommand(
                catId,

                Stream.of(
                        new Address("New York2", "NY", "10002"),
                        new Address("Las Vegas", "LV", "30001")
                ).collect(Collectors.toSet()),

                Stream.of(
                        new Contact("EMAIL", "john.smith@o2.com"),
                        new Contact("EMAIL", "john.smith@out.com")
                ).collect(Collectors.toSet())
        );
        events = catAggregate.handleUpdateCatCommand(updateCatCommand);
        catProjector.project(catId, events);

//        ContactByTypeQuery contactByTypeQuery = new ContactByTypeQuery(catId, "EMAIL");

        AddressByRegionQuery addressByRegionQuery = new AddressByRegionQuery(catId, "LV");
        Iterator<Address> addressIterator = catProjection.handle(addressByRegionQuery).iterator();
        Address address = addressIterator.next();
        assertNotEquals(address.getCity(), "New York");
        assertEquals(address.getCity(), "Las Vegas");
        assertEquals(address.getState(), "LV");
        assertEquals(address.getPostcode(), "30001");
    }
}