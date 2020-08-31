package es.reporitory;

import java.time.Instant;
import java.util.UUID;

public abstract class Event {
    public final UUID id = UUID.randomUUID();

    public final Instant occurredAt = Instant.now();

    public Event() {
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
