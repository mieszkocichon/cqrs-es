package es.reporitory;

import es.events.CatCreatedEvent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventStore extends Event {

    private Map<String, List<Event>> store;

    public EventStore() {
        this.store = new HashMap<>();
    }

    public void addEvent(String id, Event event) {
        List<Event> events = this.store.get(id);

        if (events == null) {
            events = new LinkedList<>();
            events.add(event);
            this.store.put(id, events);
        }
        else {
            events.add(event);
        }
    }

    public List<Event> getEvents(String catId) {
        return this.store.get(catId);
    }
}
