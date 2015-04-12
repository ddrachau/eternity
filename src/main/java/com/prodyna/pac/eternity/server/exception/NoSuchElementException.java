package com.prodyna.pac.eternity.server.exception;

/**
 * Persistence exception signaling that an element for the id or identifier could not be found.
 */
public class NoSuchElementException extends Exception {

    /**
     * Default constructor.
     */
    public NoSuchElementException() {
    }

    /**
     * Default exception constructor with the root cause.
     *
     * @param cause the root cause
     */
    public NoSuchElementException(Throwable cause) {
        super(cause);
    }

    /**
     * Default exception constructor with a message.
     *
     * @param message the exception message
     */
    public NoSuchElementException(String message) {
        super(message);
    }

    /**
     * Default exception constructor with a message and the root cause.
     *
     * @param message the exception message
     * @param cause   the root cause
     */
    public NoSuchElementException(String message, Throwable cause) {
        super(message, cause);
    }
}
