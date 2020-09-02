package es;

import domain.Address;
import domain.Contact;
import domain.entities.address.City;
import domain.entities.address.PostCode;
import domain.entities.address.State;
import domain.entities.contact.ContactType;
import domain.entities.contact.Detail;
import domain.entities.contact.Type;
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
        this.repository = EventStore.empty();
        this.catService = CatService.create(this.repository);
    }

    @Test
    public void givenCRUDApplication_whenDataCreated_thenDataCanBeFetched() throws Exception {
        String catId = UUID.randomUUID().toString();

        this.catService.createCat(catId, "Kitek");
        this.catService.updateCat(catId,
                Stream.of(Contact.create(Type.type(ContactType.EMAIL),
                                Detail.detail("john.smith@o2.com")),
                        Contact.create(Type.type(ContactType.EMAIL),
                                Detail.detail("joh.smith@ijk.com")))
                        .collect(Collectors.toSet()),
                Stream.of(Address.create(City.city("New York"),
                                State.state("NY"),
                                PostCode.postCode("10001")),
                        Address.create(City.city("Las Vegas"),
                                State.state("CA"),
                                PostCode.postCode("90001"))).collect(Collectors.toSet())
                );
        this.catService.updateCat(catId,
                Stream.of(Contact.create(Type.type(ContactType.EMAIL),
                                Detail.detail("john.smith@o2.com")),
                        Contact.create(Type.type(ContactType.EMAIL),
                                Detail.detail("joh.smith@ijk.com")))
                        .collect(Collectors.toSet()),
                Stream.of(Address.create(City.city("Krakow"),
                                State.state("KR"),
                                PostCode.postCode("10100")),
                        Address.create(City.city("Krakow"),
                                State.state("KR"),
                                PostCode.postCode("10100"))).collect(Collectors.toSet())
        );

        assertNotEquals(this.catService.getAddressByRegion(catId, "NY").size(), 2);
        assertEquals(this.catService.getAddressByRegion(catId, "NY").size(), 1);
        assertEquals(this.catService.getAddressByRegion(catId, "KR").size(), 2);
    }

    @Test(expected = Throwable.class)
    public void givenCRUDApplication_whenCatIsNull_thenThrowException() throws Exception {
        CatService.create(EventStore.empty()).getAddressByRegion(null, null);
    }
}
