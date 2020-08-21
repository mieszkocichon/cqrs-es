package cqrs.projections;

import domain.*;
import cqrs.queries.AddressByRegionQuery;
import cqrs.queries.ContactByTypeQuery;
import cqrs.repository.CatReadRepository;
import domain.entities.contact.ContactType;

import java.util.*;

public class CatProjection {
    private CatReadRepository catReadRepository;

    public static CatProjection empty() {
        return new CatProjection();
    }

    public static CatProjection create(CatReadRepository catReadRepository) {
        return new CatProjection(catReadRepository);
    }

    private CatProjection() {
        this.catReadRepository = CatReadRepository.empty();
    }

    private CatProjection(CatReadRepository catReadRepository) {
        this.catReadRepository = catReadRepository;
    }

    public void project(Cat cat) {
        CatOwnerContact catOwnerContact = Optional.ofNullable(
                this.catReadRepository.getCatContact(cat.getCatId())
        ).orElse(CatOwnerContact.create());
        EnumMap<ContactType, Set<Contact>> contactByType = new EnumMap<ContactType, Set<Contact>>(ContactType.class);
        for (Contact contact : cat.getContacts()) {
            Set<Contact> contacts = Optional.ofNullable(contactByType.get(contact.getType()))
                    .orElse(new HashSet<>());
            contacts.add(contact);
            contactByType.put(contact.getType(), contacts);
        }
        catOwnerContact.setContactByType(contactByType);
        this.catReadRepository.addCatOwnerContact(cat.getCatId(), catOwnerContact);

        CatAddress catAddress = Optional.ofNullable(catReadRepository.getCatAddress(cat.getCatId()))
                .orElse(CatAddress.create());
        Map<String, Set<Address>> addressByRegion = new HashMap<>();
        for (Address address : cat.getAddresses()) {
            Set<Address> addresses = Optional.ofNullable(addressByRegion.get(address.getState()))
                    .orElse(new HashSet<>());
            addresses.add(address);
            addressByRegion.put(address.getState(), addresses);
        }
        catAddress.setAddressByRegion(addressByRegion);
        catReadRepository.addCatAddress(cat.getCatId(), catAddress);
    }

    public Set<Contact> handle(ContactByTypeQuery contactByTypeQuery) throws Exception {
        CatOwnerContact catOwnerContact = catReadRepository.getCatContact(contactByTypeQuery.getCatId());

        if (catOwnerContact == null) {
            throw new Exception("Cat owner not exists.");
        }

        return catOwnerContact.getContactByType().get(contactByTypeQuery.getContactType());
    }

    public Set<Address> handle(AddressByRegionQuery addressByRegionQuery) throws Exception {
        CatAddress catAddress = catReadRepository.getCatAddress(addressByRegionQuery.getCatId());

        if (catAddress == null) {
            throw new Exception("Cat not exists.");
        }

        return catAddress.getAddressByType().get(addressByRegionQuery.getState());
    }
}
