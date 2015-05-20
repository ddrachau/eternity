package com.prodyna.pac.eternity.server.event;

import java.util.Map;

public final class EternityEvent {

    private EventType type;

    private Map<String, String> payload;

    private EternityEvent(EventType type, Map<String, String> payload) {

        this.type = type;
        this.payload = payload;

    }

    public EventType getType() {

        return type;
    }

    public Map<String, String> getPayload() {

        return payload;
    }

    public static EternityEvent createProjectEvent() {

        return new EternityEvent(EventType.PROJECT, null);
    }

    public static EternityEvent createBookingEvent() {

        return new EternityEvent(EventType.BOOKING, null);
    }

    public static EternityEvent createUserEvent() {

        return new EternityEvent(EventType.USER, null);
    }

    public static EternityEvent createAssignmentEvent() {

        return new EternityEvent(EventType.ASSIGNMENT, null);
    }

    @Override
    public String toString() {

        return "EternityEvent{" +
                "type=" + type +
                ", payload=" + payload +
                '}';
    }

}
