package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.server.exception.ElementAlreadyExistsRuntimeException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.server.model.Project;
import com.prodyna.pac.eternity.server.model.User;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Basic service for operating on users.
 */
@Local
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param user the user to create
     * @return the created project with the generated id
     * @throws ElementAlreadyExistsRuntimeException if a user with the same identifier already exists
     */
    User create(@NotNull User user) throws ElementAlreadyExistsRuntimeException;

    /**
     * Searches for a single user.
     *
     * @param identifier the identifier to search for
     * @return the found user or null if none was found
     */
    User get(@NotNull String identifier);

    /**
     * Searches for all users.
     *
     * @return a list of all found users.
     */
    List<User> findAll();

    /**
     * Updates the given user in the database.
     *
     * @param user the project to be updated
     * @return the updated user
     * @throws NoSuchElementRuntimeException        if the element to be updated cannot be found
     * @throws ElementAlreadyExistsRuntimeException if a user with the same identifier already exists
     */
    User update(@NotNull User user) throws NoSuchElementRuntimeException, ElementAlreadyExistsRuntimeException;

    /**
     * Removes the given user from the database.
     *
     * @param identifier the user to be deleted
     * @throws NoSuchElementRuntimeException if the given user cannot be found
     */
    void delete(@NotNull String identifier) throws NoSuchElementRuntimeException;

    /**
     * Assigns the given project to the given user.
     *
     * @param user    the user to be assigned
     * @param project the target project
     * @throws NoSuchElementRuntimeException if the given user or project cannot be found
     */
    void assignUserToProject(User user, Project project) throws NoSuchElementRuntimeException;

    /**
     * Unassign the given project from the given user.
     *
     * @param user    the user to be unassigned
     * @param project the target project
     * @throws NoSuchElementRuntimeException if the given user or project cannot be found
     */
    void unassignUserFromProject(User user, Project project) throws NoSuchElementRuntimeException;

    /**
     * Search for all the users assigned to the given project.
     *
     * @param project the target project
     * @return users which are assigned to the project
     */
    List<User> findAllAssignedToProject(Project project);
}
