package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidUserException;
import com.prodyna.pac.eternity.server.exception.technical.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.server.exception.technical.NotCreatedRuntimeException;
import com.prodyna.pac.eternity.server.logging.Logging;
import com.prodyna.pac.eternity.server.model.Session;
import com.prodyna.pac.eternity.server.service.AuthenticationService;
import com.prodyna.pac.eternity.server.service.CypherService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

import static com.prodyna.pac.eternity.server.common.DateUtils.getCalendar;
import static com.prodyna.pac.eternity.server.common.DateUtils.getNow;
import static com.prodyna.pac.eternity.server.common.PasswordHash.createHash;
import static com.prodyna.pac.eternity.server.common.PasswordHash.validatePassword;
import static com.prodyna.pac.eternity.server.common.QueryUtils.map;

@Logging
@Stateless
public class AuthenticationServiceImpl implements AuthenticationService {

    /**
     * Default return properties, to make object creation easier.
     */
    private static String SESSION_RETURN_PROPERTIES = "s.id, s.lastAccessedTime, s.createTime";

    @Inject
    private CypherService cypherService;

    @Override
    public Session login(@NotNull String userIdentifier, @NotNull String plainPassword)
            throws InvalidLoginException, NoSuchElementRuntimeException {

        this.checkIfPasswordIsValid(userIdentifier, plainPassword);

        // Invalidate potential open session
        cypherService.query(
                "MATCH (s:Session)-[a:ASSIGNED_TO]->(u:User {identifier:{1}})" +
                        "DELETE s,a",
                map(1, userIdentifier));

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {identifier:{1}}) " +
                        "CREATE (s:Session {id:{2}, lastAccessedTime:{3}, createTime:{3}}) " +
                        "-[:ASSIGNED_TO]->(u) " +
                        "RETURN " + SESSION_RETURN_PROPERTIES,
                map(1, userIdentifier, 2, UUID.randomUUID().toString(), 3, getNow().getTimeInMillis()));

        if (queryResult == null) {

            throw new NotCreatedRuntimeException();

        } else {

            return this.getSession(queryResult);

        }

    }

    @Override
    public void logout(@NotNull String sessionId) {

        cypherService.query(
                "MATCH (s:Session {id:{1}})-[a:ASSIGNED_TO]->(:User)" +
                        "DELETE s,a",
                map(1, sessionId));

    }

    @Override
    public Session getSession(String sessionId) {

        Session result = null;

        if (sessionId != null) {
            final Map<String, Object> queryResult = cypherService.querySingle(
                    "MATCH (s:Session {id:{1}}) SET s.lastAccessedTime={2} " +
                            "RETURN " + SESSION_RETURN_PROPERTIES,
                    map(1, sessionId, 2, getNow().getTimeInMillis()));

            if (queryResult != null) {
                result = this.getSession(queryResult);
            }
        }

        return result;

    }

    @Override
    public void storePassword(@NotNull String userIdentifier, @NotNull String plainPassword) throws InvalidUserException {

        String passwordHash = createHash(plainPassword);

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {identifier:{1}}) SET u.password={2} RETURN u.id",
                map(1, userIdentifier, 2, passwordHash));

        if (queryResult == null) {
            throw new InvalidUserException(userIdentifier);
        }

    }

    @Override
    public void changePassword(@NotNull String userIdentifier, @NotNull String oldPlainPassword,
                               @NotNull String newPlainPassword)
            throws InvalidPasswordException, InvalidUserException {

        this.checkIfPasswordIsValid(userIdentifier, oldPlainPassword);
        this.storePassword(userIdentifier, newPlainPassword);

    }

    /**
     * Checks if the given password checks against the given user.
     *
     * @param userIdentifier the user to check against
     * @param plainPassword  the users password to check
     * @throws NoSuchElementRuntimeException if the user does not exists
     * @throws InvalidPasswordException      if the old password is incorrect
     */
    private void checkIfPasswordIsValid(@NotNull String userIdentifier, @NotNull String plainPassword)
            throws InvalidPasswordException, InvalidUserException {

        String RETURN_PASSWORD = "u.password";

        final Map<String, Object> oldPasswordQueryResult = cypherService.querySingle(
                "MATCH (u:User {identifier:{1}}) RETURN " + RETURN_PASSWORD,
                map(1, userIdentifier));

        if (oldPasswordQueryResult == null) {
            throw new InvalidUserException(userIdentifier);
        }

        String currentPasswordHash = (String) oldPasswordQueryResult.get(RETURN_PASSWORD);

        if (!validatePassword(plainPassword, currentPasswordHash)) {
            throw new InvalidPasswordException();
        }

    }

    /**
     * Helper method to construct a Session from a query response
     *
     * @param values the available values
     * @return a filled Session
     */
    private Session getSession(Map<String, Object> values) {

        Session result = new Session();

        String readId = (String) values.get("s.id");
        long readLastAccessedTime = (long) values.get("s.lastAccessedTime");
        long readCreateTime = (long) values.get("s.createTime");

        result.setId(readId);
        result.setLastAccessedTime(getCalendar(readLastAccessedTime));
        result.setCreatedTime(getCalendar(readCreateTime));

        return result;

    }

}
