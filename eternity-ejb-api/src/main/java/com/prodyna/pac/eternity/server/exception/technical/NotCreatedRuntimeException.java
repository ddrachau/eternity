package com.prodyna.pac.eternity.server.exception.technical;

/**
 * Persistence exception signaling that a creation did not return a new element.
 */
public class NotCreatedRuntimeException extends RuntimeException {

    /**
     * Default constructor.
     */
    public NotCreatedRuntimeException() {
    }

    /**
     * Default exception constructor with the root cause.
     *
     * @param cause the root cause
     */
    public NotCreatedRuntimeException(final Throwable cause) {
        super(cause);
    }

    /**
     * Default exception constructor with a message.
     *
     * @param message the exception message
     */
    public NotCreatedRuntimeException(final String message) {
        super(message);
    }

    /**
     * Default exception constructor with a message and the root cause.
     *
     * @param message the exception message
     * @param cause   the root cause
     */
    public NotCreatedRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
