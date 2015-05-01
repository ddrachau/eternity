package com.prodyna.pac.eternity.server.exception.functional;

/**
 * Functional Exception signaling that a login was not possible.
 */
public abstract class InvalidLoginException extends Exception {

    /**
     * Default constructor.
     */
    public InvalidLoginException() {
    }

    /**
     * Default exception constructor with the root cause.
     *
     * @param cause the root cause
     */
    public InvalidLoginException(Throwable cause) {
        super(cause);
    }

    /**
     * Default exception constructor with a message.
     *
     * @param message the exception message
     */
    public InvalidLoginException(String message) {
        super(message);
    }

    /**
     * Default exception constructor with a message and the root cause.
     *
     * @param message the exception message
     * @param cause   the root cause
     */
    public InvalidLoginException(String message, Throwable cause) {
        super(message, cause);
    }

}
