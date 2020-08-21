package es.events;

import es.reporitory.Event;

public class CatCreatedEvent extends Event {

    private String catId;
    private String name;

    public static CatCreatedEvent create(String catId, String name) {
        return new CatCreatedEvent(catId, name);
    }

    private CatCreatedEvent() {
    }

    private CatCreatedEvent(String catId, String name) {
        this.catId = catId;
        this.name = name;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
