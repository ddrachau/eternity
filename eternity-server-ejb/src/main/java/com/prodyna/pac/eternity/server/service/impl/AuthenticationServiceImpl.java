package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.logging.Logging;
import com.prodyna.pac.eternity.server.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.server.exception.technical.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.AuthenticationService;
import com.prodyna.pac.eternity.server.service.CypherService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Map;

import static com.prodyna.pac.eternity.server.common.PasswordHash.createHash;
import static com.prodyna.pac.eternity.server.common.PasswordHash.validatePassword;
import static com.prodyna.pac.eternity.server.common.QueryUtils.map;

@Logging
@Stateless
public class AuthenticationServiceImpl implements AuthenticationService {

    @Inject
    private CypherService cypherService;

    @Override
    public void login(@NotNull User user, @NotNull String plainPassword)
            throws InvalidPasswordException, NoSuchElementRuntimeException {

        // return session? Invalidate potential open session
        this.checkIfPasswordIsValid(user, plainPassword);
        // build session

    }

    @Override
    public void logout() {
        //// destroy the session for the user in the context
        // invalidate only the session in the interceptor context?
        throw new UnsupportedOperationException();
    }

    @Override
    public User storePassword(@NotNull User user, @NotNull String plainPassword) throws NoSuchElementRuntimeException {

        String passwordHash = createHash(plainPassword);

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {id:{1}}) SET u.password={2} RETURN u.id",
                map(1, user.getId(), 2, passwordHash));

        if (queryResult == null) {
            throw new NoSuchElementRuntimeException(user.toString());
        }

        user.setPassword(passwordHash);

        return user;
    }

    @Override
    public User changePassword(@NotNull User user, @NotNull String oldPlainPassword, @NotNull String newPlainPassword)
            throws InvalidPasswordException, NoSuchElementRuntimeException {

        this.checkIfPasswordIsValid(user, oldPlainPassword);

        return this.storePassword(user, newPlainPassword);

    }

    /**
     * Checks if the given password checks against the given user.
     *
     * @param user          the user to check against
     * @param plainPassword the users password to check
     * @throws NoSuchElementRuntimeException if the user does not exists
     * @throws InvalidPasswordException      if the old password is incorrect
     */
    private void checkIfPasswordIsValid(@NotNull User user, @NotNull String plainPassword)
            throws InvalidPasswordException, NoSuchElementRuntimeException {

        String RETURN_PASSWORD = "u.password";

        final Map<String, Object> oldPasswordQueryResult = cypherService.querySingle(
                "MATCH (u:User {id:{1}}) RETURN " + RETURN_PASSWORD,
                map(1, user.getId()));

        if (oldPasswordQueryResult == null) {
            throw new NoSuchElementRuntimeException(user.toString());
        }

        String currentPasswordHash = (String) oldPasswordQueryResult.get(RETURN_PASSWORD);

        if (!validatePassword(plainPassword, currentPasswordHash)) {
            throw new InvalidPasswordException();
        }

    }

}
