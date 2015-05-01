package com.prodyna.pac.eternity.server.exception.functional;

/**
 * Functional exception signaling that a time booking is inconsistent.
 */
public class InvalidBookingException extends Exception {

    /**
     * Default constructor.
     */
    public InvalidBookingException() {
    }

    /**
     * Default exception constructor with the root cause.
     *
     * @param cause the root cause
     */
    public InvalidBookingException(Throwable cause) {
        super(cause);
    }

    /**
     * Default exception constructor with a message.
     *
     * @param message the exception message
     */
    public InvalidBookingException(String message) {
        super(message);
    }

    /**
     * Default exception constructor with a message and the root cause.
     *
     * @param message the exception message
     * @param cause   the root cause
     */
    public InvalidBookingException(String message, Throwable cause) {
        super(message, cause);
    }

}
