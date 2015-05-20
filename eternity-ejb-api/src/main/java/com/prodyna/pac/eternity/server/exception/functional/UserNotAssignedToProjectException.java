package com.prodyna.pac.eternity.server.exception.functional;

/**
 * Function exception signaling that the user is not allowed to book on this project..
 */
public class UserNotAssignedToProjectException extends Exception {

    /**
     * Default constructor.
     */
    public UserNotAssignedToProjectException() {

    }

    /**
     * Default exception constructor with the root cause.
     *
     * @param cause the root cause
     */
    public UserNotAssignedToProjectException(final Throwable cause) {

        super(cause);
    }

    /**
     * Default exception constructor with a message.
     *
     * @param message the exception message
     */
    public UserNotAssignedToProjectException(final String message) {

        super(message);
    }

    /**
     * Default exception constructor with a message and the root cause.
     *
     * @param message the exception message
     * @param cause   the root cause
     */
    public UserNotAssignedToProjectException(final String message, final Throwable cause) {

        super(message, cause);
    }

}
