package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.components.common.PasswordHash;
import com.prodyna.pac.eternity.components.common.RememberMeUtils;
import com.prodyna.pac.eternity.server.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidTokenException;
import com.prodyna.pac.eternity.server.logging.Logging;
import com.prodyna.pac.eternity.server.model.authentication.Login;
import com.prodyna.pac.eternity.server.model.authentication.RememberMe;
import com.prodyna.pac.eternity.server.model.authentication.Session;
import com.prodyna.pac.eternity.server.model.user.User;
import com.prodyna.pac.eternity.server.service.*;
import com.prodyna.pac.eternity.server.service.authentication.AuthenticationService;
import com.prodyna.pac.eternity.server.service.authentication.RememberMeService;
import com.prodyna.pac.eternity.server.service.authentication.SessionService;
import com.prodyna.pac.eternity.server.service.user.UserService;

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

        return this.createLogin(login);

    }

    @Override
    public Login login(@NotNull String rememberMeToken) throws InvalidLoginException {

        if (!rememberMeToken.contains(RememberMeUtils.TOKEN_DELIMITER)) {
            throw new InvalidTokenException();
        }

        String rememberMeIdentifier = RememberMeUtils.getRememberMeId(rememberMeToken);
        String rememberMePassword = RememberMeUtils.getRememberMePassword(rememberMeToken);

        RememberMe r = rememberMeService.get(rememberMeIdentifier);
        if (r == null) {
            throw new InvalidTokenException();
        }

        User u = userService.getByRememberMe(r.getId());
        // no matter if the password is correct, this token is used
        rememberMeService.delete(r.getId());

        boolean valid = PasswordHash.validatePassword(rememberMePassword, r.getHashedToken());

        if (valid) {

            return this.createLogin(new Login(u.getIdentifier(), null, true));

        } else {
            throw new InvalidTokenException();
        }

    }

    @Override
    public void logout(@NotNull String sessionId, String rememberMeToken) {

        sessionService.delete(sessionId);

        if (rememberMeToken != null && rememberMeToken.contains(RememberMeUtils.TOKEN_DELIMITER)) {
            rememberMeService.delete(RememberMeUtils.getRememberMeId(rememberMeToken));
        }

    }

    /**
     * Creates session and optionally a rememberMe for the given Login
     *
     * @param login the input
     * @return the updated login
     */
    private Login createLogin(@NotNull Login login) {

        Session session = sessionService.create(login.getUsername());
        User user = userService.get(login.getUsername());

        login.setXsrfToken(session.getId());
        login.setUser(user);

        if (login.isRemember()) {
            RememberMe rememberMe = rememberMeService.create(login.getUsername());
            login.setRememberMeToken(rememberMe.getToken());
        }

        return login;

    }

}