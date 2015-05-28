package com.prodyna.pac.eternity.server.model.project;

import java.util.List;

/**
 * DTO for assigning projects to a user
 */
public class AssignProjects {

    /**
     * list of already assigned projects for a requested user
     */
    private List<Project> assignedProjects;

    /**
     * list of projects which can be assigned to an user
     */
    private List<Project> assignableProjects;

    /**
     * Default getter
     *
     * @return the assignedProjects
     */
    public List<Project> getAssignedProjects() {

        return assignedProjects;
    }

    /**
     * Default setter
     *
     * @param assignedProjects to be set
     */
    public void setAssignedProjects(final List<Project> assignedProjects) {

        this.assignedProjects = assignedProjects;
    }

    /**
     * Default getter
     *
     * @return the assignableProjects
     */
    public List<Project> getAssignableProjects() {

        return assignableProjects;
    }

    /**
     * Default setter
     *
     * @param assignableProjects to be set
     */
    public void setAssignableProjects(final List<Project> assignableProjects) {

        this.assignableProjects = assignableProjects;
    }

}
