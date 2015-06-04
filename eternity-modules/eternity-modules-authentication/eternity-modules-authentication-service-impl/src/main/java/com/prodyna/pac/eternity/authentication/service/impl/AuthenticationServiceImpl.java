package com.prodyna.pac.eternity.authentication.service.impl;

import com.prodyna.pac.eternity.authentication.service.AuthenticationService;
import com.prodyna.pac.eternity.authentication.service.RememberMeService;
import com.prodyna.pac.eternity.authentication.service.SessionService;
import com.prodyna.pac.eternity.common.helper.PasswordHashBuilder;
import com.prodyna.pac.eternity.common.helper.RememberMeAccessor;
import com.prodyna.pac.eternity.common.logging.Logging;
import com.prodyna.pac.eternity.common.model.authentication.Login;
import com.prodyna.pac.eternity.common.model.authentication.RememberMe;
import com.prodyna.pac.eternity.common.model.authentication.Session;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidTokenException;
import com.prodyna.pac.eternity.common.model.user.User;
import com.prodyna.pac.eternity.common.profiling.Profiling;
import com.prodyna.pac.eternity.common.service.CypherService;
import com.prodyna.pac.eternity.user.service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Default implementation for the AuthenticationService.
 */
@Logging
@Profiling
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

    @Inject
    private PasswordHashBuilder passwordHashBuilder;

    @Inject
    private RememberMeAccessor rememberMeAccessor;

    @Override
    public Login login(@NotNull @Valid final Login login) throws InvalidLoginException {

        String userIdentifier = login.getUsername();
        String plainPassword = login.getPassword();

        userService.checkIfPasswordIsValid(userIdentifier, plainPassword);

        return this.createLogin(login);

    }

    @Override
    public Login login(@NotNull final String rememberMeToken) throws InvalidLoginException {

        if (!rememberMeToken.contains(RememberMeAccessor.TOKEN_DELIMITER)) {
            throw new InvalidTokenException();
        }

        String rememberMeIdentifier = rememberMeAccessor.getRememberMeId(rememberMeToken);
        String rememberMePassword = rememberMeAccessor.getRememberMePassword(rememberMeToken);

        RememberMe r = rememberMeService.get(rememberMeIdentifier);
        if (r == null) {
            throw new InvalidTokenException();
        }

        User u = userService.getByRememberMe(r.getId());
        // no matter if the password is correct, this token is used
        rememberMeService.delete(r.getId());

        boolean valid = passwordHashBuilder.validatePassword(rememberMePassword, r.getHashedToken());

        if (valid) {

            return this.createLogin(new Login(u.getIdentifier(), null, true));

        } else {
            throw new InvalidTokenException();
        }

    }

    @Override
    public void logout(@NotNull final String sessionId, final String rememberMeToken) {

        sessionService.delete(sessionId);

        if (rememberMeToken != null && rememberMeToken.contains(RememberMeAccessor.TOKEN_DELIMITER)) {
            rememberMeService.delete(rememberMeAccessor.getRememberMeId(rememberMeToken));
        }

    }

    /**
     * Creates session and optionally a rememberMe for the given Login
     *
     * @param login the input
     * @return the updated login
     */
    private Login createLogin(@NotNull final Login login) {

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
