package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.exception.functional.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidUserException;
import com.prodyna.pac.eternity.server.exception.technical.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.server.exception.technical.NotCreatedRuntimeException;
import com.prodyna.pac.eternity.server.logging.Logging;
import com.prodyna.pac.eternity.server.model.Booking;
import com.prodyna.pac.eternity.server.model.Project;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.model.UserRole;
import com.prodyna.pac.eternity.server.service.CypherService;
import com.prodyna.pac.eternity.server.service.ProjectService;
import com.prodyna.pac.eternity.server.service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.prodyna.pac.eternity.server.common.PasswordHash.createHash;
import static com.prodyna.pac.eternity.server.common.PasswordHash.validatePassword;
import static com.prodyna.pac.eternity.server.common.QueryUtils.map;

/**
 * Default implementation for the UserService.
 */
@Logging
@Stateless
public class UserServiceImpl implements UserService {

    /**
     * Default return properties, to make object creation easier.
     */
    private static final String USER_RETURN_PROPERTIES = "u.id, u.identifier, u.forename, u.surname, u.password, u.role";

    @Inject
    private CypherService cypherService;

    @Inject
    private ProjectService projectService;

    @Override
    public User create(@NotNull User user) throws ElementAlreadyExistsException {

        User result = this.get(user.getIdentifier());

        if (result != null) {
            throw new ElementAlreadyExistsException();
        }

        user.setId(UUID.randomUUID().toString());

        final Map<String, Object> queryResult = cypherService.querySingle(
                "CREATE (u:User {id:{1}, identifier:{2}, forename:{3}, surname:{4}, role:{5}}) " +
                        "RETURN " + USER_RETURN_PROPERTIES,
                map(1, user.getId(), 2, user.getIdentifier(), 3, user.getForename(),
                        4, user.getSurname(), 5, user.getRole().name()));

        if (queryResult == null) {
            throw new NotCreatedRuntimeException(user.toString());
        }

        if (user.getPassword() != null) {
            try {
                this.storePassword(user.getIdentifier(), user.getPassword());
                user = get(user.getIdentifier());
            } catch (InvalidUserException e) {
                // should never happen since it was just created
                throw new RuntimeException(e);
            }
        }

        return user;
    }

    @Override
    public User get(@NotNull String identifier) {

        User result = null;

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {identifier:{1}}) RETURN " + USER_RETURN_PROPERTIES,
                map(1, identifier));

        if (queryResult != null) {
            result = this.getUser(queryResult);
        }

        return result;

    }

    @Override
    public User getByBooking(@NotNull Booking booking) throws NoSuchElementRuntimeException {

        User result = null;

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User)<-[:PERFORMED_BY]-(b:Booking {id:{1}}) " +
                        "RETURN " + USER_RETURN_PROPERTIES,
                map(1, booking.getId()));

        if (queryResult != null) {
            result = this.getUser(queryResult);
        }

        return result;

    }

    @Override
    public User getBySessionId(@NotNull String sessionId) {

        User result = null;

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User)<-[:ASSIGNED_TO]-(s:Session {id:{1}}) " +
                        "RETURN " + USER_RETURN_PROPERTIES,
                map(1, sessionId));

        if (queryResult != null) {
            result = this.getUser(queryResult);
        }

        return result;

    }

    @Override
    public User getByRememberMe(@NotNull String rememberMeId) {

        User result = null;

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User)<-[:ASSIGNED_TO]-(r:RememberMe {id:{1}}) " +
                        "RETURN " + USER_RETURN_PROPERTIES,
                map(1, rememberMeId));

        if (queryResult != null) {
            result = this.getUser(queryResult);
        }

        return result;

    }

    @Override
    public List<User> findAll() {

        List<User> result = new ArrayList<>();

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (u:User) RETURN " + USER_RETURN_PROPERTIES,
                null);

        for (Map<String, Object> values : queryResult) {
            result.add(this.getUser(values));
        }

        return result;

    }

    @Override
    public User update(@NotNull User user) throws NoSuchElementRuntimeException, ElementAlreadyExistsException {

        // Check for already present project with the new identifier
        User check = this.get(user.getIdentifier());
        if (check != null && !check.getId().equals(user.getId())) {
            throw new ElementAlreadyExistsException();
        }

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {id:{1}}) SET u.identifier={2}, u.forename={3}, u.surname={4}, u.role={5} " +
                        "RETURN " + USER_RETURN_PROPERTIES,
                map(1, user.getId(), 2, user.getIdentifier(), 3, user.getForename(),
                        4, user.getSurname(), 5, user.getRole().name()));

        if (queryResult == null) {
            throw new NoSuchElementRuntimeException();
        } else {
            return this.getUser(queryResult);
        }

    }

    @Override
    public void delete(@NotNull String identifier) throws NoSuchElementRuntimeException {

        if (this.get(identifier) == null) {
            throw new NoSuchElementRuntimeException();
        }

        cypherService.query(
                "MATCH (u:User {identifier:{1}})" +
                        "OPTIONAL MATCH (s:Session)-[a1:ASSIGNED_TO]->(u)" +
                        "OPTIONAL MATCH (:Project)<-[a2:ASSIGNED_TO]-(u)" +
                        "OPTIONAL MATCH (u)<-[p1:PERFORMED_BY]-(b:Booking)-[p2:PERFORMED_FOR]->(:Project) " +
                        "DELETE s,a1,a2,u,p1,b,p2",
                map(1, identifier));

    }

    @Override
    public void assignUserToProject(User user, Project project) throws NoSuchElementRuntimeException {

        String query = "MATCH (u:User {identifier:{1}}),(p:Project {identifier:{2}}) " +
                "CREATE UNIQUE (u)-[:ASSIGNED_TO]->(p)";
        this.assignQuery(query, user, project);

    }

    @Override
    public void unassignUserFromProject(User user, Project project) throws NoSuchElementRuntimeException {

        String query = "MATCH (u:User {identifier:{1}})-[a:ASSIGNED_TO]->(p:Project {identifier:{2}}) " +
                "DELETE a";
        this.assignQuery(query, user, project);

    }

    @Override
    public List<User> findAllAssignedToProject(Project project) {

        List<User> result = new ArrayList<>();

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (u:User)-[:ASSIGNED_TO]->(p:Project {identifier:{1}}) " +
                        "RETURN " + USER_RETURN_PROPERTIES,
                map(1, project.getIdentifier()));

        for (Map<String, Object> values : queryResult) {
            result.add(this.getUser(values));
        }

        return result;

    }

    @Override
    public boolean isAssignedTo(@NotNull User user, @NotNull Project project) {

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (u:User{id:{1}})-[r:ASSIGNED_TO]->(p:Project {id:{2}}) RETURN r",
                map(1, user.getId(), 2, project.getId()));

        return queryResult.size() > 0;

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

    @Override
    public void checkIfPasswordIsValid(@NotNull String userIdentifier, @NotNull String plainPassword)
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
     * Helper method to reuse the code for assign and unassign user from and to projects
     *
     * @param query   the concrete query to execute
     * @param user    the user of the assignment
     * @param project the project of the assignment
     * @throws NoSuchElementRuntimeException if the given user or project cannot be found
     */
    private void assignQuery(String query, User user, Project project) throws NoSuchElementRuntimeException {

        user = this.get(user.getIdentifier());
        project = projectService.get(project.getIdentifier());

        if (user == null || project == null) {
            throw new NoSuchElementRuntimeException("user or project unknown");
        }

        cypherService.querySingle(query, map(1, user.getIdentifier(), 2, project.getIdentifier()));

    }

    /**
     * Helper method to construct a User from a query response
     *
     * @param values the available values
     * @return a filled User
     */
    private User getUser(Map<String, Object> values) {

        User result = new User();

        String readId = (String) values.get("u.id");
        String readIdentifier = (String) values.get("u.identifier");
        String readForename = (String) values.get("u.forename");
        String readSurname = (String) values.get("u.surname");
        String readRole = (String) values.get("u.role");

        result.setId(readId);
        result.setIdentifier(readIdentifier);
        result.setForename(readForename);
        result.setSurname(readSurname);
        result.setRole(UserRole.valueOf(readRole));

        return result;

    }

}
