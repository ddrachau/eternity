package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.server.model.RememberMe;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;

/**
 * Basic service for operating on RememberMes.
 */
@Local
public interface RememberMeService {

    /**
     * Creates a remember me for the given user ensuring that there is only one.
     *
     * @param userIdentifier the user to remember
     * @return the created RememberMe with the generated id
     */
    RememberMe create(@NotNull String userIdentifier);

    /**
     * Searches for a single RememberMe.
     *
     * @param identifier the identifier to search for
     * @return the found RememberMe or null if none was found
     */
    RememberMe get(@NotNull String identifier);

    /**
     * Searches for a single RememberMe assigned to the given user.
     *
     * @param userIdentifier the user
     * @return the found RememberMe or null if none was found
     */
    RememberMe getByUser(@NotNull String userIdentifier);

    /**
     * Removes the rememberMe assigned to the given user.
     *
     * @param userIdentifier the user
     */
    void deleteByUser(@NotNull String userIdentifier);

}
