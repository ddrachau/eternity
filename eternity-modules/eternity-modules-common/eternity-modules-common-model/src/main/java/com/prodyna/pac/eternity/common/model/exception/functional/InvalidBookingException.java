package com.prodyna.pac.eternity.common.model.exception.functional;

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
    public InvalidBookingException(final Throwable cause) {

        super(cause);
    }

    /**
     * Default exception constructor with a message.
     *
     * @param message the exception message
     */
    public InvalidBookingException(final String message) {

        super(message);
    }

    /**
     * Default exception constructor with a message and the root cause.
     *
     * @param message the exception message
     * @param cause   the root cause
     */
    public InvalidBookingException(final String message, final Throwable cause) {

        super(message, cause);
    }

}
