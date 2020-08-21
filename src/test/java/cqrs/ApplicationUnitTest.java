package cqrs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import cqrs.queries.AddressByRegionQuery;
import domain.entities.address.City;
import domain.entities.address.PostCode;
import domain.entities.address.State;
import domain.entities.contact.ContactType;
import domain.entities.contact.Detail;
import domain.entities.contact.Type;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cqrs.aggregates.CatAggregate;
import cqrs.commands.CreateCatCommand;
import cqrs.commands.UpdateCatCommand;
import domain.Address;
import domain.Cat;
import domain.Contact;
import cqrs.projections.CatProjection;
import cqrs.queries.ContactByTypeQuery;

public class ApplicationUnitTest {

    private CatAggregate catAggregate;
    private CatProjection catProjection;

    @Before
    public void SetUp() {
        this.catAggregate = CatAggregate.empty();
        this.catProjection = CatProjection.empty();
    }

    @Test
    public void givenCQRSApplication_whenCommandRun_thenQueryShouldReturnResult() throws Exception {
        String catId = UUID.randomUUID().toString();
        CreateCatCommand createCatCommand = CreateCatCommand.create(catId, "kitus");
        Cat cat = catAggregate.handleCreateCatCommand(createCatCommand);
        catProjection.project(cat);

        UpdateCatCommand updateCatCommand = UpdateCatCommand.create(cat.getCatId(),
                Stream.of(Address.create(City.city("New York"),
                                State.state("NY"),
                                PostCode.postCode("10001")),
                        Address.create(City.city("Los Angeles"),
                                State.state("CA"),
                                PostCode.postCode("90001"))
                ).collect(Collectors.toSet()),

                Stream.of(Contact.create(Type.type(ContactType.EMAIL), Detail.detail("john.smith@jiu.com")),
                        Contact.create(Type.type(ContactType.EMAIL), Detail.detail("john.smith@o2.com")))
                        .collect(Collectors.toSet()));
        cat = catAggregate.handleUpdateCatCommand(updateCatCommand);
        catProjection.project(cat);

        ContactByTypeQuery contactByTypeQuery = ContactByTypeQuery.create(catId, ContactType.EMAIL);

        Iterator<Contact> contactIterator = catProjection.handle(contactByTypeQuery).iterator();
        Contact contact1 = contactIterator.next();
        assertEquals(contact1.getType(), ContactType.EMAIL);
        assertEquals(contact1.getDetail(), "john.smith@o2.com");
        Contact contact2 = contactIterator.next();
        assertEquals(contact2.getType(), ContactType.EMAIL);
        assertEquals(contact2.getDetail(), "john.smith@jiu.com");

        AddressByRegionQuery addressByRegionQuery = AddressByRegionQuery.create(catId, "NY");
        Set<Address> addressesSet = catProjection.handle(addressByRegionQuery);
        Iterator<Address> addressIterator = addressesSet.iterator();

        assertNotEquals(addressesSet.size(), 2);
        assertEquals(addressesSet.size(), 1);
        Address address1 = addressIterator.next();

        assertNotEquals(address1.getCity(), "Las Vegas");
        assertEquals(address1.getCity(), "New York");
        assertEquals(address1.getPostcode(), "10001");
        assertEquals(address1.getState(), "NY");
    }
}
