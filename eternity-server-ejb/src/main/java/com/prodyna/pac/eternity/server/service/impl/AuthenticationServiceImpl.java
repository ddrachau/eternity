package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.server.exception.technical.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.server.logging.Logging;
import com.prodyna.pac.eternity.server.model.Session;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

@Logging
@Stateless
public class AuthenticationServiceImpl implements AuthenticationService {

    @Inject
    private CypherService cypherService;

    @Inject
    private UserService userService;

    @Inject
    private SessionService sessionService;

    @Inject
    private RememberMeService rememberMeService;

    @Override
    public Session login(@NotNull String userIdentifier, @NotNull String plainPassword)
            throws InvalidLoginException, NoSuchElementRuntimeException {

        userService.checkIfPasswordIsValid(userIdentifier, plainPassword);

        rememberMeService.deleteByUser(userIdentifier);
        sessionService.deleteByUser(userIdentifier);

        return sessionService.create(userIdentifier);

    }

    @Override
    public void logout(@NotNull String sessionId) {

        User user = userService.getBySessionId(sessionId);

        rememberMeService.deleteByUser(user.getIdentifier());
        sessionService.deleteByUser(user.getIdentifier());

    }

}
