package es;

import domain.Address;
import domain.Contact;
import es.reporitory.EventStore;
import es.service.CatService;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ApplicationUnitTest {

    private EventStore repository;
    private CatService catService;

    @Before
    public void SetUp() {
        this.repository = new EventStore();
        this.catService = new CatService(this.repository);
    }

    @Test
    public void givenCRUDApplication_whenDataCreated_thenDataCanBeFetched() throws Exception {
        String catId = UUID.randomUUID().toString();

        this.catService.createCat(catId, "Kitek");
        this.catService.updateCat(catId,
                Stream.of(new Contact("Email", "john.smith@o2.com"),
                        new Contact("Email", "joh.smith@ijk.com")).collect(Collectors.toSet()),
                Stream.of(new Address("New York", "NY", "10001"),
                        new Address("Las Vegas", "CA", "90001")).collect(Collectors.toSet())
                );
        this.catService.updateCat(catId,
                Stream.of(new Contact("Email", "john.smith@o2.com"),
                        new Contact("Email", "joh.smith@ijk.com")).collect(Collectors.toSet()),
                Stream.of(new Address("Krakow", "KR", "10100"),
                        new Address("Krakow", "KR", "10100")).collect(Collectors.toSet())
        );

        assertNotEquals(this.catService.getAddressByRegion(catId, "NY").size(), 2);
        assertEquals(this.catService.getAddressByRegion(catId, "NY").size(), 1);
        assertEquals(this.catService.getAddressByRegion(catId, "KR").size(), 2);
    }
}
