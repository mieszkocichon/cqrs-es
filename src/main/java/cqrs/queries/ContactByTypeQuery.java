package cqrs.queries;

public class ContactByTypeQuery {

    private String catId;
    private String contactType;

    public static ContactByTypeQuery create(String catId, String contactType) {
        return new ContactByTypeQuery(catId, contactType);
    }

    private ContactByTypeQuery() {
    }

    private ContactByTypeQuery(String catId, String contactType) {
        this.catId = catId;
        this.contactType = contactType;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }
}
