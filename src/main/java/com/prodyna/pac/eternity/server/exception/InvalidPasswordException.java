package com.prodyna.pac.eternity.server.exception;

/**
 * Exception signaling that a password for a user is not correct.
 */
public class InvalidPasswordException extends Exception {

    /**
     * Default constructor.
     */
    public InvalidPasswordException() {
    }

    /**
     * Default exception constructor with the root cause.
     *
     * @param cause the root cause
     */
    public InvalidPasswordException(Throwable cause) {
        super(cause);
    }

    /**
     * Default exception constructor with a message.
     *
     * @param message the exception message
     */
    public InvalidPasswordException(String message) {
        super(message);
    }

    /**
     * Default exception constructor with a message and the root cause.
     *
     * @param message the exception message
     * @param cause   the root cause
     */
    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
