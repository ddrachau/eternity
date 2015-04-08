package com.prodyna.pac.eternity.server.service.impl;

import com.prodyna.pac.eternity.server.common.logging.Logging;
import com.prodyna.pac.eternity.server.entity.Project;
import com.prodyna.pac.eternity.server.service.ProjectService;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@Logging
@Stateless
public class ProjectServiceImpl implements ProjectService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager entityManager;

    @Override
    public Project create(Project project) {
        log.info("Adding " + project);
        entityManager.persist(project);
        return project;
    }

    @Override
    public void delete(Project project) {
        Project p = entityManager.find(Project.class, project.getId());
        entityManager.remove(p);
    }

    @Override
    public List<Project> findAll() {
        return entityManager.createQuery("from Project").getResultList();
    }

    @Override
    public void update(Project project) {
        entityManager.merge(project);
    }

}
