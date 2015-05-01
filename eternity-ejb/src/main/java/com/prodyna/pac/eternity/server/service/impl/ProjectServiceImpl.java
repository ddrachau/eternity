package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.exception.functional.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.exception.technical.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.server.exception.technical.NotCreatedRuntimeException;
import com.prodyna.pac.eternity.server.logging.Logging;
import com.prodyna.pac.eternity.server.model.booking.Booking;
import com.prodyna.pac.eternity.server.model.project.Project;
import com.prodyna.pac.eternity.server.model.user.User;
import com.prodyna.pac.eternity.server.service.CypherService;
import com.prodyna.pac.eternity.server.service.project.ProjectService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.prodyna.pac.eternity.components.common.QueryUtils.map;

/**
 * Default implementation for the ProjectService.
 */
@Logging
@Stateless
public class ProjectServiceImpl implements ProjectService {

    /**
     * Default return properties, to make object creation easier.
     */
    private static final String PROJECT_RETURN_PROPERTIES = "p.id, p.identifier, p.description";

    @Inject
    private CypherService cypherService;

    @Override
    public Project create(@NotNull Project project) throws ElementAlreadyExistsException {

        Project result = this.get(project.getIdentifier());

        if (result != null) {
            throw new ElementAlreadyExistsException();
        }

        project.setId(UUID.randomUUID().toString());

        final Map<String, Object> queryResult = cypherService.querySingle(
                "CREATE (p:Project {id:{1}, identifier:{2}, description:{3}}) " +
                        "RETURN " + PROJECT_RETURN_PROPERTIES,
                map(1, project.getId(), 2, project.getIdentifier(), 3, project.getDescription()));

        if (queryResult == null) {
            throw new NotCreatedRuntimeException(project.toString());
        }

        return project;

    }

    @Override
    public Project get(@NotNull String identifier) {

        Project result = null;

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (p:Project {identifier:{1}}) " +
                        "RETURN " + PROJECT_RETURN_PROPERTIES,
                map(1, identifier));

        if (queryResult != null) {
            result = this.getProject(queryResult);
        }

        return result;

    }

    @Override
    public Project get(@NotNull Booking booking) throws NoSuchElementRuntimeException {

        Project result = null;

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (p:Project)<-[:PERFORMED_FOR]-(b:Booking {id:{1}}) " +
                        "RETURN " + PROJECT_RETURN_PROPERTIES,
                map(1, booking.getId()));

        if (queryResult != null) {
            result = this.getProject(queryResult);
        }

        return result;

    }

    @Override
    public List<Project> findAll() {

        List<Project> result = new ArrayList<>();

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (p:Project) RETURN " + PROJECT_RETURN_PROPERTIES,
                null);

        for (Map<String, Object> values : queryResult) {
            result.add(this.getProject(values));
        }

        return result;
    }

    @Override
    public Project update(@NotNull Project project) throws NoSuchElementRuntimeException, ElementAlreadyExistsException {

        // Check for already present project with the new identifier
        Project check = this.get(project.getIdentifier());
        if (check != null && !check.getId().equals(project.getId())) {
            throw new ElementAlreadyExistsException();
        }

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (p:Project {id:{1}}) SET p.identifier={2}, p.description={3} " +
                        "RETURN " + PROJECT_RETURN_PROPERTIES,
                map(1, project.getId(), 2, project.getIdentifier(), 3, project.getDescription()));

        if (queryResult == null) {
            throw new NoSuchElementRuntimeException();
        } else {
            return this.getProject(queryResult);
        }

    }

    @Override
    public void delete(@NotNull String identifier) throws NoSuchElementRuntimeException {

        if (this.get(identifier) == null) {
            throw new NoSuchElementRuntimeException();
        }

        cypherService.query(
                "MATCH (p:Project {identifier:{1}})" +
                        "OPTIONAL MATCH (p)<-[a:ASSIGNED_TO]-(:User)" +
                        "OPTIONAL MATCH (p)<-[p1:PERFORMED_FOR]-(b:Booking)-[p2:PERFORMED_BY]->(:User) " +
                        "DELETE a,p,p1,b,p2",
                map(1, identifier));

    }

    @Override
    public List<Project> findAllAssignedToUser(@NotNull User user) {

        List<Project> result = new ArrayList<>();

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (p:Project)<-[:ASSIGNED_TO]-(u:User {identifier:{1}}) " +
                        "RETURN " + PROJECT_RETURN_PROPERTIES,
                map(1, user.getIdentifier()));

        for (Map<String, Object> values : queryResult) {
            result.add(this.getProject(values));
        }

        return result;

    }

    /**
     * Helper method to construct a Project from a query response
     *
     * @param values the available values
     * @return a filled Project
     */
    private Project getProject(@NotNull Map<String, Object> values) {

        Project result = new Project();

        String readId = (String) values.get("p.id");
        String readIdentifier = (String) values.get("p.identifier");
        String readDescription = (String) values.get("p.description");

        result.setId(readId);
        result.setIdentifier(readIdentifier);
        result.setDescription(readDescription);

        return result;
    }
}
