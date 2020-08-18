package cqrs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import cqrs.queries.AddressByRegionQuery;
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
        this.catAggregate = new CatAggregate();
        this.catProjection = new CatProjection();
    }

    @Test
    public void givenCQRSApplication_whenCommandRun_thenQueryShouldReturnResult() throws Exception {
        String catId = UUID.randomUUID().toString();
        CreateCatCommand createCatCommand = new CreateCatCommand(catId, "kitus");
        Cat cat = catAggregate.handleCreateCatCommand(createCatCommand);
        catProjection.project(cat);

        UpdateCatCommand updateCatCommand = new UpdateCatCommand(cat.getCatId(),
                Stream.of(new Address("New York", "NY", "10001"),
                        new Address("Los Angeles", "CA", "90001")).collect(Collectors.toSet()),
                Stream.of(new Contact("EMAIL", "john.smith@o2.com"),
                        new Contact("EMAIL", "john.smith@jiu.com"))
                        .collect(Collectors.toSet()));
        cat = catAggregate.handleUpdateCatCommand(updateCatCommand);
        catProjection.project(cat);

        ContactByTypeQuery contactByTypeQuery = new ContactByTypeQuery(catId, "EMAIL");

        Iterator<Contact> contactIterator = catProjection.handle(contactByTypeQuery).iterator();
        Contact contact1 = contactIterator.next();
        assertEquals(contact1.getType(), "EMAIL");
        assertEquals(contact1.getDetail(), "john.smith@jiu.com");
        Contact contact2 = contactIterator.next();
        assertEquals(contact2.getType(), "EMAIL");
        assertEquals(contact2.getDetail(), "john.smith@o2.com");

        AddressByRegionQuery addressByRegionQuery = new AddressByRegionQuery(catId, "NY");
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
