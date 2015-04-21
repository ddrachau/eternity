package com.prodyna.pac.eternity.server.exception.functional;

/**
 * Functional Exception signaling that a password or a user was invalid for a login.
 */
public class InvalidUserException extends InvalidLoginException {

    /**
     * Default constructor.
     */
    public InvalidUserException() {
    }

    /**
     * Default exception constructor with the root cause.
     *
     * @param cause the root cause
     */
    public InvalidUserException(Throwable cause) {
        super(cause);
    }

    /**
     * Default exception constructor with a message.
     *
     * @param message the exception message
     */
    public InvalidUserException(String message) {
        super(message);
    }

    /**
     * Default exception constructor with a message and the root cause.
     *
     * @param message the exception message
     * @param cause   the root cause
     */
    public InvalidUserException(String message, Throwable cause) {
        super(message, cause);
    }

}
