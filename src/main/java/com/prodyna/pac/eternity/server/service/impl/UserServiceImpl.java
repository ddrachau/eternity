package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.exception.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementException;
import com.prodyna.pac.eternity.server.model.Project;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.AuthenticationService;
import com.prodyna.pac.eternity.server.service.CypherService;
import com.prodyna.pac.eternity.server.service.UserService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.prodyna.pac.eternity.server.common.QueryUtils.map;

public class UserServiceImpl implements UserService {

    @Inject
    private CypherService cypherService;

    @Inject
    private AuthenticationService authenticationService;

    @Override
    public User create(@NotNull User user) throws ElementAlreadyExistsException {

        User result = this.get(user.getIdentifier());

        if (result != null) {
            throw new ElementAlreadyExistsException();
        }

        user.setId(UUID.randomUUID().toString());

        final Map<String, Object> queryResult = cypherService.querySingle(
                "CREATE (u:User {id:{1}, identifier:{2}, forename:{3}, surname:{4}}) " +
                        "RETURN u.id, u.identifier, u.forename, u.surname",
                map(1, user.getId(), 2, user.getIdentifier(), 3, user.getForename(),
                        4, user.getSurname()));

        if (user.getPassword() != null) {
            user = authenticationService.storePassword(user, user.getPassword());
        }

        return user;
    }

    @Override
    public User get(@NotNull String identifier) {

        User result = null;

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {identifier:{1}}) RETURN u.id, u.identifier, u.forename, u.surname",
                map(1, identifier));

        if (queryResult != null) {
            result = this.getUser(queryResult);
        }

        return result;

    }

    @Override
    public List<User> findAll() {

        List<User> result = new ArrayList<>();

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (u:User) RETURN u.id, u.identifier, u.forename, u.surname, u.password",
                null);

        for (Map<String, Object> values : queryResult) {
            result.add(this.getUser(values));
        }

        return result;

    }

    @Override
    public User update(@NotNull User user) throws NoSuchElementException, ElementAlreadyExistsException {

        // Check for already present project with the new identifier
        User check = this.get(user.getIdentifier());
        if (check != null && !check.getId().equals(user.getId())) {
            throw new ElementAlreadyExistsException();
        }

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (u:User {id:{1}}) SET u.identifier={2}, u.forename={3}, u.surname={4} " +
                        "RETURN u.id, u.identifier, u.forename, u.surname",
                map(1, user.getId(), 2, user.getIdentifier(), 3, user.getForename(),
                        4, user.getSurname()));

        if (queryResult == null) {
            throw new NoSuchElementException();
        } else {
            return this.getUser(queryResult);
        }

    }

    @Override
    public void delete(@NotNull String identifier) throws NoSuchElementException {

        if (this.get(identifier) == null) {
            throw new NoSuchElementException();
        }

        cypherService.query("MATCH (u:User {identifier:{1}}) DELETE u",
                map(1, identifier));

    }

    @Override
    public void assignUserToProject(User user, Project project) {

    }

    @Override
    public void unassignUserFromProject(User user, Project project) {

    }

    @Override
    public List<User> findAllAssignedToProject(Project project) {
        return null;
    }

    private User getUser(Map<String, Object> values) {

        User result = new User();

        String readId = (String) values.get("u.id");
        String readIdentifier = (String) values.get("u.identifier");
        String readForename = (String) values.get("u.forename");
        String readSurname = (String) values.get("u.surname");
        String readPassword = (String) values.get("u.password");

        result.setId(readId);
        result.setIdentifier(readIdentifier);
        result.setForename(readForename);
        result.setSurname(readSurname);
        result.setPassword(readPassword);

        return result;

    }

}
