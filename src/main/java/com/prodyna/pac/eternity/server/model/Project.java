package com.prodyna.pac.eternity.server.model;

public class Project {

    private String id;
    private String identifer;
    private String description;

    public Project() {

    }

    public Project(String id, String identifer, String description) {
        this.id = id;
        this.identifer = identifer;
        this.description = description;
    }

    public Project(String identifer, String description) {
        this(null, identifer, description);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentifer() {
        return identifer;
    }

    public void setIdentifer(String identifer) {
        this.identifer = identifer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (id != project.id) return false;
        if (identifer != null ? !identifer.equals(project.identifer) : project.identifer != null) return false;
        return !(description != null ? !description.equals(project.description) : project.description != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", identifer='" + identifer + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
