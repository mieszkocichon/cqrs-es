package domain;

import domain.entities.contact.ContactType;
import domain.entities.contact.Detail;
import domain.entities.contact.Type;

public class Contact {

    private ContactType type;
    private String detail;

    public static Contact create(Type type, Detail detail) {
        return new Contact(type, detail);
    }

    private Contact() {
    }

    private Contact(Type type, Detail detail) {
        this.type = type.getValue();
        this.detail = detail.getValue();
    }

    public ContactType getType() {
        return type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
