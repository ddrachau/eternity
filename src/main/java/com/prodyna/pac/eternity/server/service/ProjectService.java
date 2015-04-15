package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.server.exception.technical.ElementAlreadyExistsRuntimeException;
import com.prodyna.pac.eternity.server.exception.technical.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.server.model.Booking;
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
     * @throws ElementAlreadyExistsRuntimeException if a project with the same identifier already exists
     */
    Project create(@NotNull Project project) throws ElementAlreadyExistsRuntimeException;

    /**
     * Searches for a single project.
     *
     * @param identifier the identifier to search for
     * @return the found project or null if none was found
     */
    Project get(@NotNull String identifier);

    /**
     * Searches for a single project.
     *
     * @param booking the booking the project is mapped to
     * @return the found user
     * @throws NoSuchElementRuntimeException if no User can be found
     */
    Project get(@NotNull Booking booking) throws NoSuchElementRuntimeException;

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
     * @throws NoSuchElementRuntimeException        if the element to be updated cannot be found
     * @throws ElementAlreadyExistsRuntimeException if a project with the same identifier already exists
     */
    Project update(@NotNull Project project) throws NoSuchElementRuntimeException, ElementAlreadyExistsRuntimeException;

    /**
     * Removes the given project from the database.
     *
     * @param identifier the project to be deleted
     * @throws NoSuchElementRuntimeException if the given project cannot be found
     */
    void delete(@NotNull String identifier) throws NoSuchElementRuntimeException;

    /**
     * Search for all the projects a user is assigned to.
     *
     * @param user the source user
     * @return projects which are assigned to the user
     */
    List<Project> findAllAssignedToUser(@NotNull User user);

}
