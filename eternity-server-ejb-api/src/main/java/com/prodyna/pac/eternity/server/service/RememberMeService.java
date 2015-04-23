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
     * @return the created project with the generated id
     */
    RememberMe create(@NotNull String userIdentifier);

    //RememberMe get(@NotNull String identifier);

    void deleteByUser(@NotNull String userIdentifier);

}
