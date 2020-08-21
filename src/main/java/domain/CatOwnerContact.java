package domain;

import domain.entities.contact.ContactType;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class CatOwnerContact {

    private EnumMap<ContactType, Set<Contact>> contactByType;

    private CatOwnerContact() {
    }

    public static CatOwnerContact create() {
        return new CatOwnerContact(new EnumMap<ContactType, Set<Contact>>(ContactType.class));
    }

    private CatOwnerContact(EnumMap<ContactType, Set<Contact>> contactByType) {
        this.contactByType = contactByType;
    }

    public Map<ContactType, Set<Contact>> getContactByType() {
        return contactByType;
    }

    public void setContactByType(EnumMap<ContactType, Set<Contact>> contactByType) {
        this.contactByType = contactByType;
    }
}
