package com.prodyna.pac.eternity.server.exception.technical;

/**
 * Persistence exception signaling that an element already exists for the id or an identifier.
 */
public class ElementAlreadyExistsRuntimeException extends RuntimeException {

    /**
     * Default constructor.
     */
    public ElementAlreadyExistsRuntimeException() {
    }

    /**
     * Default exception constructor with the root cause.
     *
     * @param cause the root cause
     */
    public ElementAlreadyExistsRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * Default exception constructor with a message.
     *
     * @param message the exception message
     */
    public ElementAlreadyExistsRuntimeException(String message) {
        super(message);
    }

    /**
     * Default exception constructor with a message and the root cause.
     *
     * @param message the exception message
     * @param cause   the root cause
     */
    public ElementAlreadyExistsRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
