package com.prodyna.pac.eternity.user.service.impl;

import com.prodyna.pac.eternity.common.helper.PasswordHashBuilder;
import com.prodyna.pac.eternity.common.helper.QueryMapBuilder;
import com.prodyna.pac.eternity.common.logging.Logging;
import com.prodyna.pac.eternity.common.model.FilterRequest;
import com.prodyna.pac.eternity.common.model.FilterResponse;
import com.prodyna.pac.eternity.common.model.booking.Booking;
import com.prodyna.pac.eternity.common.model.event.EternityEvent;
import com.prodyna.pac.eternity.common.model.exception.functional.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidUserException;
import com.prodyna.pac.eternity.common.model.exception.technical.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.common.model.exception.technical.NotCreatedRuntimeException;
import com.prodyna.pac.eternity.common.model.project.Project;
import com.prodyna.pac.eternity.common.model.user.User;
import com.prodyna.pac.eternity.common.model.user.UserRole;
import com.prodyna.pac.eternity.common.profiling.Profiling;
import com.prodyna.pac.eternity.common.service.CypherService;
import com.prodyna.pac.eternity.project.service.ProjectService;
import com.prodyna.pac.eternity.user.service.UserService;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Default implementation for the UserService.
 */
@Logging
@Profiling
@Stateless
public class UserServiceImpl implements UserService {

    /**
     * Default return properties, to make object creation easier.
     */
    private static final String USER_RETURN_PROPERTIES =
            "u.id, u.identifier, u.forename, u.surname, u.password, u.role";

    /**
     * Fire events if user change to give the ui a chance to react
     */
    @Inject
    private Event<EternityEvent> events;

    @Inject
    private Logger logger;

    @Inject
    private CypherService cypherService;

    @Inject
    private ProjectService projectService;

    @Inject
    private PasswordHashBuilder passwordHashBuilder;

    @Inject
    private QueryMapBuilder queryMapBuilder;

    @Override
    public User create(@NotNull @Valid final User user) throws ElementAlreadyExistsException {

        User result = this.get(user.getIdentifier());

        if (result != null) {
            throw new ElementAlreadyExistsException();
        }

        result = user;
        result.setId(UUID.randomUUID().toString());

        final Map<String, Object> queryResult = cypherService.querySingle(
                "CREATE (u:User {id:{1}, identifier:{2}, forename:{3}, surname:{4}, role:{5}}) " +
                        "RETURN " + USER_RETURN_PROPERTIES,
                queryMapBuilder.map(1, result.getId(), 2, result.getIdentifier(), 3, result.getForename(),
                        4, result.getSurname(), 5, result.getRole().name()));

        if (queryResult == null) {
            throw new NotCreatedRuntimeException(result.toString());
        }

        if (user.getPassword() != null && user.getPassword().length() > 0) {
            try {
                this.storePassword(result.getIdentifier(), result.getPassword());
                result = get(user.getIdentifier());
            } catch (InvalidUserException e) {
                // should never happen since it was just created
                throw new RuntimeException(e);
            }
        }

        events.fire(EternityEvent.createUserEvent());
        logger.info("User {} created", user.getIdentifier());

        return result;
    }

    @Override
    public User get(@NotNull final String identifier) {

        User result = null;

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {identifier:{1}}) RETURN " + USER_RETURN_PROPERTIES,
                queryMapBuilder.map(1, identifier));

        if (queryResult != null) {
            result = this.getUser(queryResult);
        }

        return result;

    }

    @Override
    public User getByBooking(@NotNull final Booking booking) throws NoSuchElementRuntimeException {

        User result = null;

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User)<-[:PERFORMED_BY]-(b:Booking {id:{1}}) " +
                        "RETURN " + USER_RETURN_PROPERTIES,
                queryMapBuilder.map(1, booking.getId()));

        if (queryResult != null) {
            result = this.getUser(queryResult);
        }

        return result;

    }

    @Override
    public User getBySessionId(@NotNull final String sessionId) {

        User result = null;

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User)<-[:ASSIGNED_TO]-(s:Session {id:{1}}) " +
                        "RETURN " + USER_RETURN_PROPERTIES,
                queryMapBuilder.map(1, sessionId));

        if (queryResult != null) {
            result = this.getUser(queryResult);
        }

        return result;

    }

    @Override
    public User getByRememberMe(@NotNull final String rememberMeId) {

        User result = null;

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User)<-[:ASSIGNED_TO]-(r:RememberMe {id:{1}}) " +
                        "RETURN " + USER_RETURN_PROPERTIES,
                queryMapBuilder.map(1, rememberMeId));

        if (queryResult != null) {
            result = this.getUser(queryResult);
        }

        return result;

    }

    @Override
    public List<User> find() {

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
    public FilterResponse<User> find(@NotNull final FilterRequest filterRequest) {

        filterRequest.setMappings(this.getRequestMappings());
        String filterString = filterRequest.getFilterString();

        int allUser = (int) cypherService.querySingle(
                "MATCH (u:User) " +
                        filterString +
                        "RETURN count(u)",
                null).get("count(u)");

        filterRequest.setTotalSize(allUser);

        FilterResponse<User> response = new FilterResponse<>();

        response.setTotalSize(allUser);
        response.setPageSize(filterRequest.getPageSize());
        response.setOffset(filterRequest.getStart());

        if (allUser > 0) {

            List<User> result = new ArrayList<>();

            final List<Map<String, Object>> queryResult = cypherService.query(
                    "MATCH (u:User) " +
                            filterString +
                            "RETURN " + USER_RETURN_PROPERTIES,
                    null, filterRequest);

            for (Map<String, Object> values : queryResult) {
                result.add(this.getUser(values));
            }

            response.setData(result);

        }

        return response;

    }

    @Override
    public User update(@NotNull @Valid final User user)
            throws NoSuchElementRuntimeException, ElementAlreadyExistsException {

        // Check for already present project with the new identifier
        User check = this.get(user.getIdentifier());
        if (check != null && !check.getId().equals(user.getId())) {
            throw new ElementAlreadyExistsException();
        }

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {id:{1}}) SET u.identifier={2}, u.forename={3}, u.surname={4}, u.role={5} " +
                        "RETURN " + USER_RETURN_PROPERTIES,
                queryMapBuilder.map(1, user.getId(), 2, user.getIdentifier(), 3, user.getForename(),
                        4, user.getSurname(), 5, user.getRole().name()));

        if (queryResult == null) {
            throw new NoSuchElementRuntimeException();
        } else {

            events.fire(EternityEvent.createUserEvent());
            User result = this.getUser(queryResult);
            logger.info("User {} updated", user.getIdentifier());

            return result;
        }


    }

    @Override
    public void delete(@NotNull final String identifier) throws NoSuchElementRuntimeException {

        if (this.get(identifier) == null) {
            throw new NoSuchElementRuntimeException();
        }

        cypherService.query(
                "MATCH (u:User {identifier:{1}})" +
                        "OPTIONAL MATCH (s:Session)-[a1:ASSIGNED_TO]->(u)" +
                        "OPTIONAL MATCH (r:RememberMe)-[a2:ASSIGNED_TO]->(u)" +
                        "OPTIONAL MATCH (:Project)<-[a3:ASSIGNED_TO]-(u)" +
                        "OPTIONAL MATCH (u)<-[p1:PERFORMED_BY]-(b:Booking)-[p2:PERFORMED_FOR]->(:Project) " +
                        "DELETE s,a1,r,a2,a3,u,p1,b,p2",
                queryMapBuilder.map(1, identifier));

        events.fire(EternityEvent.createUserEvent());
        logger.info("User {} deleted", identifier);

    }

    @Override
    public void assignUserToProject(final User user, final Project project) throws NoSuchElementRuntimeException {

        String query = "MATCH (u:User {identifier:{1}}),(p:Project {identifier:{2}}) " +
                "CREATE UNIQUE (u)-[:ASSIGNED_TO]->(p)";
        this.assignQuery(query, user, project);

        events.fire(EternityEvent.createAssignmentEvent());

    }

    @Override
    public void unassignUserFromProject(final User user, final Project project) throws NoSuchElementRuntimeException {

        String query = "MATCH (u:User {identifier:{1}})-[a:ASSIGNED_TO]->(p:Project {identifier:{2}}) " +
                "DELETE a";
        this.assignQuery(query, user, project);

        events.fire(EternityEvent.createAssignmentEvent());

    }

    @Override
    public List<User> findAllAssignedToProject(final Project project) {

        List<User> result = new ArrayList<>();

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (u:User)-[:ASSIGNED_TO]->(p:Project {identifier:{1}}) " +
                        "RETURN " + USER_RETURN_PROPERTIES,
                queryMapBuilder.map(1, project.getIdentifier()));

        for (Map<String, Object> values : queryResult) {
            result.add(this.getUser(values));
        }

        return result;

    }

    @Override
    public boolean isAssignedTo(@NotNull final User user, @NotNull final Project project) {

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (u:User{id:{1}})-[r:ASSIGNED_TO]->(p:Project {id:{2}}) RETURN r",
                queryMapBuilder.map(1, user.getId(), 2, project.getId()));

        return queryResult.size() > 0;

    }

    @Override
    public void storePassword(@NotNull final String userIdentifier, @NotNull final String plainPassword)
            throws InvalidUserException {

        String passwordHash = passwordHashBuilder.createHash(plainPassword);

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {identifier:{1}}) SET u.password={2} RETURN u.id",
                queryMapBuilder.map(1, userIdentifier, 2, passwordHash));

        if (queryResult == null) {
            throw new InvalidUserException(userIdentifier);
        }

    }

    @Override
    public void changePassword(@NotNull final String userIdentifier, @NotNull final String oldPlainPassword,
                               @NotNull final String newPlainPassword)
            throws InvalidPasswordException, InvalidUserException {

        this.checkIfPasswordIsValid(userIdentifier, oldPlainPassword);
        this.storePassword(userIdentifier, newPlainPassword);

    }

    @Override
    public void checkIfPasswordIsValid(@NotNull final String userIdentifier, @NotNull final String plainPassword)
            throws InvalidPasswordException, InvalidUserException {

        final Map<String, Object> oldPasswordQueryResult = cypherService.querySingle(
                "MATCH (u:User {identifier:{1}}) RETURN u.password",
                queryMapBuilder.map(1, userIdentifier));

        if (oldPasswordQueryResult == null) {
            throw new InvalidUserException(userIdentifier);
        }

        String currentPasswordHash = (String) oldPasswordQueryResult.get("u.password");

        if (!passwordHashBuilder.validatePassword(plainPassword, currentPasswordHash)) {
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
    private void assignQuery(final String query, final User user, final Project project)
            throws NoSuchElementRuntimeException {

        User readUser = this.get(user.getIdentifier());
        Project readProject = projectService.get(project.getIdentifier());

        if (readUser == null || readProject == null) {
            throw new NoSuchElementRuntimeException("user or project unknown");
        }

        cypherService.querySingle(query,
                queryMapBuilder.map(1, readUser.getIdentifier(), 2, readProject.getIdentifier()));

    }

    /**
     * Creates a map of mappings between ui variables and query variables.
     *
     * @return the mapping
     */
    private Map<String, String> getRequestMappings() {

        HashMap<String, String> result = new HashMap<>();

        result.put("id", "u.id");
        result.put("identifier", "u.identifier");
        result.put("forename", "u.forename");
        result.put("surname", "u.surname");
        result.put("role", "u.role");

        return result;

    }

    /**
     * Helper method to construct a User from a query response
     *
     * @param values the available values
     * @return a filled User
     */
    private User getUser(final Map<String, Object> values) {

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
