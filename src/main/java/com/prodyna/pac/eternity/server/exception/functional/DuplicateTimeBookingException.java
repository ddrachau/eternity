package com.prodyna.pac.eternity.server.exception.functional;

/**
 * Functional exception signaling that a time booking (partially) exists for a user and project.
 */
public class DuplicateTimeBookingException extends Exception {

    /**
     * Default constructor.
     */
    public DuplicateTimeBookingException() {
    }

    /**
     * Default exception constructor with the root cause.
     *
     * @param cause the root cause
     */
    public DuplicateTimeBookingException(Throwable cause) {
        super(cause);
    }

    /**
     * Default exception constructor with a message.
     *
     * @param message the exception message
     */
    public DuplicateTimeBookingException(String message) {
        super(message);
    }

    /**
     * Default exception constructor with a message and the root cause.
     *
     * @param message the exception message
     * @param cause   the root cause
     */
    public DuplicateTimeBookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
