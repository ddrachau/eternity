package com.prodyna.pac.eternity.server.exception.functional;

/**
 * Functional Exception signaling that a given token cannot be used for a login.
 */
public class InvalidTokenException extends InvalidLoginException {

    /**
     * Default constructor.
     */
    public InvalidTokenException() {
    }

    /**
     * Default exception constructor with the root cause.
     *
     * @param cause the root cause
     */
    public InvalidTokenException(Throwable cause) {
        super(cause);
    }

    /**
     * Default exception constructor with a message.
     *
     * @param message the exception message
     */
    public InvalidTokenException(String message) {
        super(message);
    }

    /**
     * Default exception constructor with a message and the root cause.
     *
     * @param message the exception message
     * @param cause   the root cause
     */
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }

}
