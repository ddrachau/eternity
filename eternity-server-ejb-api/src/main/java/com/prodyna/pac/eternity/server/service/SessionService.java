package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.server.model.Session;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;

/**
 * Basic service for operating on sessions.
 */
@Local
public interface SessionService {

    /**
     * Creates a session for the given user ensuring that there is only one.
     *
     * @param userIdentifier the user to remember
     * @return the created session with the generated id
     */
    Session create(@NotNull String userIdentifier);

    /**
     * Retrieves a session for a given session id.
     *
     * @param sessionId the id to search for
     * @return the session for the id or null if no session can be found.
     */
    Session get(String sessionId);

    /**
     * Removes the session assigned to the given user.
     *
     * @param userIdentifier the user
     */
    void deleteByUser(@NotNull String userIdentifier);

}
