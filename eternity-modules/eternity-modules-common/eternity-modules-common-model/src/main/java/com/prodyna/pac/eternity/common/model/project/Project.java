package com.prodyna.pac.eternity.common.model.project;

import com.prodyna.pac.eternity.common.model.AbstractNode;

import java.util.Objects;

/**
 * A project is a representation of a real work project. Users can book times on it.
 */
public class Project extends AbstractNode {

    /**
     * the functional identifier for the project
     */
    private String identifier;

    /**
     * the description for the project
     */
    private String description;

    /**
     * Empty default constructor *
     */
    public Project() {

    }

    /**
     * Creates a project and initialize the following properties:
     *
     * @param id          the technical identifier
     * @param identifier  the functional identifier
     * @param description the project description
     */
    public Project(final String id, final String identifier, final String description) {

        super(id);
        this.identifier = identifier;
        this.description = description;
    }

    /**
     * Creates a project and initialize the following properties:
     *
     * @param identifier  the functional identifier
     * @param description the project description
     */
    public Project(final String identifier, final String description) {

        this(null, identifier, description);

    }

    /**
     * Basic Getter
     *
     * @return the identifier
     */
    public String getIdentifier() {

        return identifier;

    }

    /**
     * Basic Setter
     *
     * @param identifier to be set
     */
    public void setIdentifier(final String identifier) {

        this.identifier = identifier;

    }

    /**
     * Basic Getter
     *
     * @return the description
     */
    public String getDescription() {

        return description;

    }

    /**
     * Basic Setter
     *
     * @param description to be set
     */
    public void setDescription(final String description) {

        this.description = description;

    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(identifier, project.identifier) &&
                Objects.equals(description, project.description);

    }

    @Override
    public int hashCode() {

        return Objects.hash(identifier, description);

    }

    @Override
    public String toString() {

        return "Project{" +
                "id='" + this.getId() + '\'' +
                ", identifier='" + identifier + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
