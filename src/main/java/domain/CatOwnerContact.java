package domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CatOwnerContact {

    private Map<String, Set<Contact>> contactByType;

    private CatOwnerContact() {
    }

    public static CatOwnerContact create() {
        return new CatOwnerContact(new HashMap<>());
    }

    private CatOwnerContact(Map<String, Set<Contact>> contactByType) {
        this.contactByType = contactByType;
    }

    public Map<String, Set<Contact>> getContactByType() {
        return contactByType;
    }

    public void setContactByType(Map<String, Set<Contact>> contactByType) {
        this.contactByType = contactByType;
    }
}
