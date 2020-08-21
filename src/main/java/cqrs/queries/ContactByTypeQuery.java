package cqrs.queries;

import domain.entities.contact.ContactType;

public class ContactByTypeQuery {

    private String catId;
    private ContactType contactType;

    public static ContactByTypeQuery create(String catId, ContactType contactType) {
        return new ContactByTypeQuery(catId, contactType);
    }

    private ContactByTypeQuery() {
    }

    private ContactByTypeQuery(String catId, ContactType contactType) {
        this.catId = catId;
        this.contactType = contactType;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }
}
