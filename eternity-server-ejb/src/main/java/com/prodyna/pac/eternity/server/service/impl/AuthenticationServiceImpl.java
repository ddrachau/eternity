package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.server.logging.Logging;
import com.prodyna.pac.eternity.server.model.Login;
import com.prodyna.pac.eternity.server.model.RememberMe;
import com.prodyna.pac.eternity.server.model.Session;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Default implementation for the AuthenticationService.
 */
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
    public Login login(@NotNull Login login) throws InvalidLoginException {

        String userIdentifier = login.getUsername();
        String plainPassword = login.getPassword();

        userService.checkIfPasswordIsValid(userIdentifier, plainPassword);

        rememberMeService.deleteByUser(userIdentifier);
        sessionService.deleteByUser(userIdentifier);

        Session session = sessionService.create(userIdentifier);

        login.setXsrfToken(session.getId());

        if (login.isRemember()) {
            RememberMe rememberMe = rememberMeService.create(userIdentifier);
            login.setRememberMeToken(rememberMe.getToken());
        }

        return login;

    }

    @Override
    public void logout(@NotNull String sessionId) {

        User user = userService.getBySessionId(sessionId);

        rememberMeService.deleteByUser(user.getIdentifier());
        sessionService.deleteByUser(user.getIdentifier());

    }

}
