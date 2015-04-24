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
     * @param id the technical identifier
     */
    public AbstractNode(String id) {
        this.id = id;
    }

    /**
     * Basic Getter
     *
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Basic Setter
     *
     * @param id to be set
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractNode that = (AbstractNode) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
