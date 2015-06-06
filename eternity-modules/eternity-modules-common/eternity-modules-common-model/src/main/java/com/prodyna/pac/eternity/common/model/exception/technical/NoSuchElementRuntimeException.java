package com.prodyna.pac.eternity.common.model.exception.technical;

/**
 * Persistence exception signaling that an element for the id or identifier could not be found.
 */
public class NoSuchElementRuntimeException extends RuntimeException {

    /**
     * Default constructor.
     */
    public NoSuchElementRuntimeException() {

    }

    /**
     * Default exception constructor with the root cause.
     *
     * @param cause the root cause
     */
    public NoSuchElementRuntimeException(final Throwable cause) {

        super(cause);
    }

    /**
     * Default exception constructor with a message.
     *
     * @param message the exception message
     */
    public NoSuchElementRuntimeException(final String message) {

        super(message);
    }

    /**
     * Default exception constructor with a message and the root cause.
     *
     * @param message the exception message
     * @param cause   the root cause
     */
    public NoSuchElementRuntimeException(final String message, final Throwable cause) {

        super(message, cause);
    }

}
