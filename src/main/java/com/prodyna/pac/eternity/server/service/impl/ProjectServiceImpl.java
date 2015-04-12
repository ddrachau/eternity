package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.common.logging.Logging;
import com.prodyna.pac.eternity.server.exception.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementException;
import com.prodyna.pac.eternity.server.model.Project;
import com.prodyna.pac.eternity.server.service.CypherService;
import com.prodyna.pac.eternity.server.service.ProjectService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.prodyna.pac.eternity.server.common.QueryUtils.map;

@Logging
@Stateless
public class ProjectServiceImpl implements ProjectService {

    private static final String QUERY_CREATE_PROJECT = "CREATE (p:Project {id:{1}, identifier:{2}, description:{3}}) RETURN p.id, p.identifier, p.description";
    private static final String QUERY_GET_PROJECT_BY_IDENTIFIER = "MATCH (p:Project {identifier:{1}}) RETURN p.id, p.identifier, p.description";
    private static final String QUERY_FIND_ALL_PROJECTS = "MATCH (p:Project) RETURN p.id, p.identifier, p.description";
    private static final String QUERY_DELETE_PROJECT_BY_IDENTIFIER = "MATCH (p:Project {identifier:{1}}) DELETE p";

    @Inject
    private CypherService cypherService;

    @Override
    public Project create(@NotNull Project project) throws ElementAlreadyExistsException {

        Project result = this.get(project.getIdentifer());

        if (result != null) {
            throw new ElementAlreadyExistsException();
        }

        project.setId(UUID.randomUUID().toString());

        final Map<String, Object> queryResult = cypherService.querySingle(
                QUERY_CREATE_PROJECT,
                map(1, project.getId(), 2, project.getIdentifer(), 3, project.getDescription()));

        return project;

    }

    @Override
    public Project get(@NotNull String identifier) {

        Project result = null;

        final Map<String, Object> queryResult = cypherService.querySingle(
                QUERY_GET_PROJECT_BY_IDENTIFIER, map(1, identifier));

        if (queryResult != null) {
            result = this.getProject(queryResult);
        }

        return result;

    }

    @Override
    public List<Project> findAll() {

        List<Project> result = new ArrayList<>();

        final List<Map<String, Object>> queryResult = cypherService.query(
                QUERY_FIND_ALL_PROJECTS, null);

        for (Map<String, Object> values : queryResult) {
            result.add(this.getProject(values));
        }

        return result;
    }

    @Override
    public Project update(@NotNull Project project) throws ElementAlreadyExistsException {
        return null;
    }

    @Override
    public void delete(@NotNull String identifier) throws NoSuchElementException {

        if (cypherService.querySingle(QUERY_GET_PROJECT_BY_IDENTIFIER, map(1, identifier)) == null) {
            throw new NoSuchElementException();
        }

        cypherService.query(QUERY_DELETE_PROJECT_BY_IDENTIFIER, map(1, identifier));

    }

    private Project getProject(Map<String, Object> values) {

        Project result = new Project();

        String readId = (String) values.get("p.id");
        String readIdentifier = (String) values.get("p.identifier");
        String readDescription = (String) values.get("p.description");

        result.setId(readId);
        result.setIdentifer(readIdentifier);
        result.setDescription(readDescription);

        return result;
    }

}
