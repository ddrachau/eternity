package com.prodyna.pac.eternity.server.model;

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
    public Project(String id, String identifier, String description) {
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
    public Project(String identifier, String description) {
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
    public void setIdentifier(String identifier) {
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
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (description != null ? !description.equals(project.description) : project.description != null) return false;
        if (!identifier.equals(project.identifier)) return false;

        return super.equals(o);
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
