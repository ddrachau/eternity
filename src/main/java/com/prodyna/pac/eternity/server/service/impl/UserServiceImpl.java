package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.exception.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementException;
import com.prodyna.pac.eternity.server.model.Project;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.CypherService;
import com.prodyna.pac.eternity.server.service.UserService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.prodyna.pac.eternity.server.common.QueryUtils.map;

public class UserServiceImpl implements UserService {

    @Inject
    private CypherService cypherService;

    @Override
    public User create(@NotNull User user) throws ElementAlreadyExistsException {

        User result = this.get(user.getIdentifier());

        if (result != null) {
            throw new ElementAlreadyExistsException();
        }

        user.setId(UUID.randomUUID().toString());

        final Map<String, Object> queryResult = cypherService.querySingle(
                "CREATE (u:User {id:{1}, identifier:{2}, forename:{3}, surname:{4}, password: {5}}) " +
                        "RETURN u.id, u.identifier, u.forename, u.surname, u.password",
                map(1, user.getId(), 2, user.getIdentifier(), 3, user.getForename(),
                        4, user.getSurname(), 5, user.getPassword()));

        return user;
    }

    @Override
    public User get(@NotNull String identifier) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User update(@NotNull User user) throws NoSuchElementException, ElementAlreadyExistsException {
        return null;
    }

    @Override
    public void delete(@NotNull String identifier) throws NoSuchElementException {

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
}
