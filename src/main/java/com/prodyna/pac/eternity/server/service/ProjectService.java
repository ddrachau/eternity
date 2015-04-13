package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.server.exception.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementException;
import com.prodyna.pac.eternity.server.model.Project;
import com.prodyna.pac.eternity.server.model.User;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Basic service for operating on projects.
 */
@Local
public interface ProjectService {

    /**
     * Creates a new project.
     *
     * @param project the project to create
     * @return the created project with the generated id
     * @throws ElementAlreadyExistsException if a project with the same identifier already exists
     */
    Project create(@NotNull Project project) throws ElementAlreadyExistsException;

    /**
     * Searches for a single project.
     *
     * @param identifier the identifier to search for
     * @return the found project or null if none was found
     */
    Project get(@NotNull String identifier);

    /**
     * Searches for all projects.
     *
     * @return a list of all found projects.
     */
    List<Project> findAll();

    /**
     * Updates the given project in the database.
     *
     * @param project the project to be updated
     * @return the updated project
     * @throws NoSuchElementException        if the element to be updated cannot be found
     * @throws ElementAlreadyExistsException if a project with the same identifier already exists
     */
    Project update(@NotNull Project project) throws NoSuchElementException, ElementAlreadyExistsException;

    /**
     * Removes the given project from the database.
     *
     * @param identifier the project to be deleted
     * @throws NoSuchElementException if the given project cannot be found
     */
    void delete(@NotNull String identifier) throws NoSuchElementException;

    /**
     * Search for all the projects a user is assigned to.
     *
     * @param user the source user
     * @return projects which are assigned to the user
     */
    List<Project> findAllAssignedToUser(User user);

}
