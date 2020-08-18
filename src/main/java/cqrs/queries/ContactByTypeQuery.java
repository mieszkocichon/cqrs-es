package cqrs.queries;

public class ContactByTypeQuery {

    private String catId;
    private String contactType;

    private ContactByTypeQuery() {
    }

    public ContactByTypeQuery(String catId, String contactType) {
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
