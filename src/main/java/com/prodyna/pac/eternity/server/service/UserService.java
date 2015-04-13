package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.server.exception.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementException;
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
     * @throws ElementAlreadyExistsException if a user with the same identifier already exists
     */
    User create(@NotNull User user) throws ElementAlreadyExistsException;

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
     * @throws NoSuchElementException        if the element to be updated cannot be found
     * @throws ElementAlreadyExistsException if a user with the same identifier already exists
     */
    User update(@NotNull User user) throws NoSuchElementException, ElementAlreadyExistsException;

    /**
     * Removes the given user from the database.
     *
     * @param identifier the user to be deleted
     * @throws NoSuchElementException if the given user cannot be found
     */
    void delete(@NotNull String identifier) throws NoSuchElementException;

}
