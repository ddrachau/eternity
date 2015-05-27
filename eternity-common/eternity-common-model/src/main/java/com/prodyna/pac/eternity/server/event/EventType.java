package com.prodyna.pac.eternity.server.event;

/**
 * Enum for the available events
 */
public enum EventType {

    /**
     * A project was created, updated or deleted
     */
    PROJECT("project"),
    /**
     * A booking was created, updated or deleted
     */
    BOOKING("booking"),
    /**
     * A user was created, updated or deleted
     */
    USER("user"),
    /**
     * An user was assigned or unassigned from a project
     */
    ASSIGNMENT("assignment");

    /**
     * The key representing the event.
     */
    private String messageKey;

    /**
     * Default constructor.
     *
     * @param messageKey the key representing the event
     */
    EventType(final String messageKey) {

        this.messageKey = messageKey;

    }

    /**
     * Default getter
     *
     * @return the messageKey
     */
    public String getMessageKey() {

        return this.messageKey;

    }

    @Override
    public String toString() {

        return "EventType{" +
                "messageKey='" + messageKey + '\'' +
                '}';
    }

}
