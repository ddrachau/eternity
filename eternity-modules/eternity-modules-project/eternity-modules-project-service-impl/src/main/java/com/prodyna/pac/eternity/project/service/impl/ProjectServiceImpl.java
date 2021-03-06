package com.prodyna.pac.eternity.project.service.impl;

import com.prodyna.pac.eternity.common.helper.QueryMapBuilder;
import com.prodyna.pac.eternity.common.logging.Logging;
import com.prodyna.pac.eternity.common.model.FilterRequest;
import com.prodyna.pac.eternity.common.model.FilterResponse;
import com.prodyna.pac.eternity.common.model.booking.Booking;
import com.prodyna.pac.eternity.common.model.event.EternityEvent;
import com.prodyna.pac.eternity.common.model.exception.functional.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.common.model.exception.technical.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.common.model.exception.technical.NotCreatedRuntimeException;
import com.prodyna.pac.eternity.common.model.project.Project;
import com.prodyna.pac.eternity.common.model.user.User;
import com.prodyna.pac.eternity.common.profiling.Profiling;
import com.prodyna.pac.eternity.common.service.CypherService;
import com.prodyna.pac.eternity.project.service.ProjectService;
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
 * Default implementation for the ProjectService.
 */
@Logging
@Profiling
@Stateless
public class ProjectServiceImpl implements ProjectService {

    /**
     * Default return properties, to make object creation easier.
     */
    private static final String PROJECT_RETURN_PROPERTIES = "p.id, p.identifier, p.description";

    /**
     * Fire events if projects change to give the ui a chance to react
     */
    @Inject
    private Event<EternityEvent> events;

    @Inject
    private Logger logger;

    @Inject
    private CypherService cypherService;

    @Inject
    private QueryMapBuilder queryMapBuilder;

    @Override
    public Project create(@NotNull @Valid final Project project) throws ElementAlreadyExistsException {

        Project result = this.get(project.getIdentifier());

        if (result != null) {
            throw new ElementAlreadyExistsException();
        }

        project.setId(UUID.randomUUID().toString());

        final Map<String, Object> queryResult = cypherService.querySingle(
                "CREATE (p:Project {id:{1}, identifier:{2}, description:{3}}) " +
                        "RETURN " + PROJECT_RETURN_PROPERTIES,
                queryMapBuilder.map(1, project.getId(), 2, project.getIdentifier(), 3, project.getDescription()));

        if (queryResult == null) {
            throw new NotCreatedRuntimeException(project.toString());
        }

        events.fire(EternityEvent.createProjectEvent());
        logger.info("Project {} created", project.getIdentifier());

        return project;

    }

    @Override
    public Project get(@NotNull final String identifier) {

        Project result = null;

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (p:Project {identifier:{1}}) " +
                        "RETURN " + PROJECT_RETURN_PROPERTIES,
                queryMapBuilder.map(1, identifier));

        if (queryResult != null) {
            result = this.getProject(queryResult);
        }

        return result;

    }

    @Override
    public Project get(@NotNull final Booking booking) throws NoSuchElementRuntimeException {

        Project result = null;

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (p:Project)<-[:PERFORMED_FOR]-(b:Booking {id:{1}}) " +
                        "RETURN " + PROJECT_RETURN_PROPERTIES,
                queryMapBuilder.map(1, booking.getId()));

        if (queryResult != null) {
            result = this.getProject(queryResult);
        }

        return result;

    }

    @Override
    public List<Project> find() {

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
    public FilterResponse<Project> find(@NotNull final FilterRequest filterRequest) {

        filterRequest.setMappings(this.getRequestMappings());
        String filterString = filterRequest.getFilterString();

        int allProjects = (int) cypherService.querySingle(
                "MATCH (p:Project) " +
                        filterString +
                        "RETURN count(p)",
                null).get("count(p)");

        filterRequest.setTotalSize(allProjects);

        FilterResponse<Project> response = new FilterResponse<>();

        response.setTotalSize(allProjects);
        response.setPageSize(filterRequest.getPageSize());
        response.setOffset(filterRequest.getStart());

        if (allProjects > 0) {

            List<Project> result = new ArrayList<>();

            final List<Map<String, Object>> queryResult = cypherService.query(
                    "MATCH (p:Project) " +
                            filterString +
                            "RETURN " + PROJECT_RETURN_PROPERTIES,
                    null, filterRequest);

            for (Map<String, Object> values : queryResult) {
                result.add(this.getProject(values));
            }

            response.setData(result);

        }

        return response;

    }

    @Override
    public Project update(@NotNull @Valid final Project project)
            throws NoSuchElementRuntimeException, ElementAlreadyExistsException {

        // Check for already present project with the new identifier
        Project check = this.get(project.getIdentifier());
        if (check != null && !check.getId().equals(project.getId())) {
            throw new ElementAlreadyExistsException();
        }

        final Map<String, Object> queryResult = cypherService.querySingle(
                "MATCH (p:Project {id:{1}}) SET p.identifier={2}, p.description={3} " +
                        "RETURN " + PROJECT_RETURN_PROPERTIES,
                queryMapBuilder.map(1, project.getId(), 2, project.getIdentifier(), 3, project.getDescription()));

        if (queryResult == null) {
            throw new NoSuchElementRuntimeException();
        } else {

            events.fire(EternityEvent.createProjectEvent());
            Project result = this.getProject(queryResult);
            logger.info("Project {} updated", project.getIdentifier());

            return result;
        }

    }

    @Override
    public void delete(@NotNull final String identifier) throws NoSuchElementRuntimeException {

        if (this.get(identifier) == null) {
            throw new NoSuchElementRuntimeException();
        }

        cypherService.query(
                "MATCH (p:Project {identifier:{1}})" +
                        "OPTIONAL MATCH (p)<-[a:ASSIGNED_TO]-(:User)" +
                        "OPTIONAL MATCH (p)<-[p1:PERFORMED_FOR]-(b:Booking)-[p2:PERFORMED_BY]->(:User) " +
                        "DELETE a,p,p1,b,p2",
                queryMapBuilder.map(1, identifier));

        events.fire(EternityEvent.createProjectEvent());
        logger.info("Project {} deleted", identifier);

    }

    @Override
    public List<Project> findAllAssignedToUser(@NotNull final User user) {

        List<Project> result = new ArrayList<>();

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (p:Project)<-[:ASSIGNED_TO]-(u:User {identifier:{1}}) " +
                        "RETURN " + PROJECT_RETURN_PROPERTIES + " " +
                        "ORDER BY p.identifier",
                queryMapBuilder.map(1, user.getIdentifier()));

        for (Map<String, Object> values : queryResult) {
            result.add(this.getProject(values));
        }

        return result;

    }

    @Override
    public List<Project> findAllAssignableToUser(@NotNull final User user) {

        List<Project> result = new ArrayList<>();

        final List<Map<String, Object>> queryResult = cypherService.query(
                "MATCH (p:Project), (u:User {identifier:{1}}) " +
                        "WHERE NOT (p)<-[:ASSIGNED_TO]-(u) " +
                        "RETURN " + PROJECT_RETURN_PROPERTIES + " " +
                        "ORDER BY p.identifier",
                queryMapBuilder.map(1, user.getIdentifier()));

        for (Map<String, Object> values : queryResult) {
            result.add(this.getProject(values));
        }

        return result;

    }

    /**
     * Creates a map of mappings between ui variables and query variables.
     *
     * @return the mapping
     */
    private Map<String, String> getRequestMappings() {

        HashMap<String, String> result = new HashMap<>();

        result.put("identifier", "p.identifier");
        result.put("description", "p.description");

        return result;

    }

    /**
     * Helper method to construct a Project from a query response
     *
     * @param values the available values
     * @return a filled Project
     */
    private Project getProject(@NotNull final Map<String, Object> values) {

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
