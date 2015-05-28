package com.prodyna.pac.eternity.server.event;

import java.util.Map;

/**
 * Represents an event in the application
 */
public final class EternityEvent {

    /**
     * the event type
     */
    private EventType type;

    /**
     * additional payload the event can store
     */
    private Map<String, String> payload;

    /**
     * Private constructor used by the factory methods
     *
     * @param type    the type
     * @param payload the payload
     */
    private EternityEvent(final EventType type, final Map<String, String> payload) {

        this.type = type;
        this.payload = payload;

    }

    /**
     * Default getter
     *
     * @return the type
     */
    public EventType getType() {

        return this.type;

    }

    /**
     * Default getter
     *
     * @return the payload
     */
    public Map<String, String> getPayload() {

        return this.payload;

    }

    /**
     * Creates a event for project changes
     *
     * @return the created event
     */
    public static EternityEvent createProjectEvent() {

        return new EternityEvent(EventType.PROJECT, null);

    }

    /**
     * Creates a event for booking changes
     *
     * @return the created event
     */
    public static EternityEvent createBookingEvent() {

        return new EternityEvent(EventType.BOOKING, null);

    }

    /**
     * Creates a event for user changes
     *
     * @return the created event
     */
    public static EternityEvent createUserEvent() {

        return new EternityEvent(EventType.USER, null);

    }

    /**
     * Creates a event for assignment changes
     *
     * @return the created event
     */
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
