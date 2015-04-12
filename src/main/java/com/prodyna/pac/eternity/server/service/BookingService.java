package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.server.exception.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementException;
import com.prodyna.pac.eternity.server.model.Booking;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Basic service for operating on bookings.
 */
@Local
public interface BookingService {

    /**
     * Creates a new project.
     *
     * @param project the project to create
     * @return the created project with the generated id
     * @throws ElementAlreadyExistsException if a project with the same identifier already exists
     */
    Booking create(@NotNull Booking project) throws ElementAlreadyExistsException;

    /**
     * Searches for a single project.
     *
     * @param identifier the identifier to search for
     * @return the found project or null if none was found
     */
    Booking get(@NotNull String identifier);

    /**
     * Searches for all projects.
     *
     * @return a list of all found projects.
     */
    List<Booking> findAll();

    /**
     * Updates the given project in the database.
     *
     * @param project the project to be updated
     * @return the updated project
     * @throws NoSuchElementException        if the element to be updated cannot be found
     * @throws ElementAlreadyExistsException if a project with the same identifier already exists
     */
    Booking update(@NotNull Booking project) throws NoSuchElementException, ElementAlreadyExistsException;

    /**
     * Removes the given project from the database.
     *
     * @param identifier the project to be deleted
     * @throws NoSuchElementException if the given project cannot be found
     */
    void delete(@NotNull String identifier) throws NoSuchElementException;

}
