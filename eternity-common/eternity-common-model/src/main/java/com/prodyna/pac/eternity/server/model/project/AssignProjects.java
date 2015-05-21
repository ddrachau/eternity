package com.prodyna.pac.eternity.server.model.project;

import java.util.List;

/**
 * Created by daniel on 20.05.15.
 */
public class AssignProjects {

    private List<Project> assignedProjects;

    private List<Project> assignableProjects;

    public List<Project> getAssignedProjects() {

        return assignedProjects;
    }

    public void setAssignedProjects(final List<Project> assignedProjects) {

        this.assignedProjects = assignedProjects;
    }

    public List<Project> getAssignableProjects() {

        return assignableProjects;
    }

    public void setAssignableProjects(final List<Project> assignableProjects) {

        this.assignableProjects = assignableProjects;
    }
}
