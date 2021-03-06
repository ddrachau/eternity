package com.prodyna.pac.eternity.common.model.exception.functional;

/**
 * Functional exception signaling that an element already exists for the id or an identifier.
 */
public class ElementAlreadyExistsException extends Exception {

    /**
     * Default constructor.
     */
    public ElementAlreadyExistsException() {

    }

    /**
     * Default exception constructor with the root cause.
     *
     * @param cause the root cause
     */
    public ElementAlreadyExistsException(final Throwable cause) {

        super(cause);
    }

    /**
     * Default exception constructor with a message.
     *
     * @param message the exception message
     */
    public ElementAlreadyExistsException(final String message) {

        super(message);
    }

    /**
     * Default exception constructor with a message and the root cause.
     *
     * @param message the exception message
     * @param cause   the root cause
     */
    public ElementAlreadyExistsException(final String message, final Throwable cause) {

        super(message, cause);
    }

}
