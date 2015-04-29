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

    @Override
    public final boolean equals(final Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractNode that = (AbstractNode) o;

        if (!id.equals(that.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {

        return id.hashCode();

    }

}
