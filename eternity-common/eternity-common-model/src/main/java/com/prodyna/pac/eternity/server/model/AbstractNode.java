package com.prodyna.pac.eternity.server.model;

/**
 * AbstractNode is a common base class for nodes.
 */
public abstract class AbstractNode {

    /**
     * technical identifier
     */
    private String id;

    /**
     * Empty default constructor.
     */
    public AbstractNode() {

    }

    /**
     * Creates an AbstractNode and initialize the following property:
     *
     * @param newId the technical identifier
     */
    public AbstractNode(final String newId) {

        this.id = newId;

    }

    /**
     * Basic Getter
     *
     * @return the id
     */
    public final String getId() {

        return this.id;

    }

    /**
     * Basic Setter
     *
     * @param newId to be set
     */
    public final void setId(final String newId) {

        this.id = newId;

    }

}
