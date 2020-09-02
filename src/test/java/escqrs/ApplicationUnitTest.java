package escqrs;

import cqrs.commands.CreateCatCommand;
import cqrs.commands.UpdateCatCommand;
import cqrs.projections.CatProjection;
import cqrs.queries.AddressByRegionQuery;
import cqrs.repository.CatReadRepository;
import domain.Address;
import domain.Contact;
import domain.entities.address.City;
import domain.entities.address.PostCode;
import domain.entities.address.State;
import domain.entities.contact.ContactType;
import domain.entities.contact.Detail;
import domain.entities.contact.Type;
import es.reporitory.Event;
import es.reporitory.EventStore;
import escqrs.aggregates.CatAggregate;
import escqrs.projectors.CatProjector;
import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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
    private String catId;

    @Before
    public void setUp() {
        this.writeRepository = EventStore.empty();
        this.catReadRepository = CatReadRepository.empty();
        this.catProjector = CatProjector.create(this.catReadRepository);
        this.catAggregate = CatAggregate.create(this.writeRepository);
        this.catProjection = CatProjection.create(this.catReadRepository);
        this.catId = UUID.randomUUID().toString();
    }

    @Test
    public void givenCQRSApplication_whenCommandRun_thenQueryShouldReturnResult() throws Exception {
        List<Event> events;
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

    @Test
    public void givenCQRSApplication_whenCommandRunByTime_thenQueryShouldReturnResult() {
        List<Event> events;
        CreateCatCommand createCatCommand = CreateCatCommand.create(this.catId, "Kitek");
        events = this.catAggregate.handleCreateCatCommand(createCatCommand);

        catProjector.project(catId, events);

        UpdateCatCommand updateCatCommand = UpdateCatCommand.create(
                catId,

                Stream.of(
                        Address.create(
                                City.city("Alabama"),
                                State.state("AL"),
                                PostCode.postCode("1000")
                        ),
                        Address.create(
                                City.city("Hawai"),
                                State.state("HA"),
                                PostCode.postCode("1000"))
                ).collect(Collectors.toSet()),

                Stream.of(
                        Contact.create(
                                Type.type(ContactType.EMAIL),
                                Detail.detail("1@1.en")
                        ),
                        Contact.create(
                                Type.type(ContactType.EMAIL),
                                Detail.detail("2@2.en")
                        )
                ).collect(Collectors.toSet())
        );
        events = catAggregate.handleUpdateCatCommand(updateCatCommand);
        catProjector.project(catId, events);

        updateCatCommand = UpdateCatCommand.create(
                catId,

                Stream.of(
                        Address.create(
                                City.city("Massachusetts"),
                                State.state("MA"),
                                PostCode.postCode("2000")
                        )
                ).collect(Collectors.toSet()),

                Stream.of(
                        Contact.create(
                                Type.type(ContactType.EMAIL),
                                Detail.detail("3@3.en"))
                ).collect(Collectors.toSet())
        );
        events = catAggregate.handleUpdateCatCommand(updateCatCommand);
        catProjector.project(catId, events);

        updateCatCommand = UpdateCatCommand.create(
                catId,

                Stream.of(
                        Address.create(
                                City.city("Massachusetts"),
                                State.state("MA"),
                                PostCode.postCode("3000")
                        )
                ).collect(Collectors.toSet()),

                Stream.of(
                        Contact.create(
                                Type.type(ContactType.EMAIL),
                                Detail.detail("4@4.en")
                        )
                ).collect(Collectors.toSet())
        );
        events = catAggregate.handleUpdateCatCommand(updateCatCommand);
        catProjector.project(catId, events);

        final long lastTime = System.currentTimeMillis();

        Awaitility.await().atMost(10, TimeUnit.SECONDS);

        updateCatCommand = UpdateCatCommand.create(
                catId,

                Stream.of(
                        Address.create(
                                City.city("Massachusetts"),
                                State.state("MA"),
                                PostCode.postCode("4000")
                        )
                ).collect(Collectors.toSet()),

                Stream.of(
                        Contact.create(
                                Type.type(ContactType.EMAIL),
                                Detail.detail("5@5.en")
                        )
                ).collect(Collectors.toSet())
        );
        events = catAggregate.handleUpdateCatCommand(updateCatCommand);
        catProjector.project(catId, events);

        updateCatCommand = UpdateCatCommand.create(
                catId,

                Stream.of(
                        Address.create(
                                City.city("Kassachusetts"),
                                State.state("KA"),
                                PostCode.postCode("5000")
                        )
                ).collect(Collectors.toSet()),

                Stream.of(
                        Contact.create(
                                Type.type(ContactType.EMAIL),
                                Detail.detail("6@6.en")
                        )
                ).collect(Collectors.toSet())
        );

//        events = catAggregate.handleUpdateCatCommandByTime(updateCatCommand, Instant.now().minusMillis(System.currentTimeMillis() - lastTime));
        events = catAggregate.handleUpdateCatCommand(updateCatCommand);
        catProjector.project(catId, events);

        AddressByRegionQuery addressByRegionQuery = AddressByRegionQuery.create(catId, "MA");

        Awaitility.await().atMost(10, TimeUnit.SECONDS)
                .until(() -> {
                    Iterator<Address> addressIterator = catProjection
                            .handleByTime(addressByRegionQuery, Instant.now().minusMillis(System.currentTimeMillis() - lastTime))
                            .iterator();
                    Address address = addressIterator.next();

                    return address.getPostcode().equals("3000");
                });
    }
}