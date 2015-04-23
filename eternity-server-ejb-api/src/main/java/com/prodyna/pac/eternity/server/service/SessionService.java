package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.server.model.Session;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;

@Local
public interface SessionService {

    Session create(@NotNull String userIdentifier);

    /**
     * Retrieves a session for a given session id.
     *
     * @param sessionId the id to search for
     * @return the session for the id or null if no session can be found.
     */
    Session get(String sessionId);

    void deleteByUser(@NotNull String userIdentifier);

}
