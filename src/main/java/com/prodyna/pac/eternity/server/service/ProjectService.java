package com.prodyna.pac.eternity.server.service;

import com.prodyna.pac.eternity.server.entity.Project;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ProjectService {

    public Project create(Project project);

    public List<Project> findAll();

    public void update(Project abteilung);

    public void delete(Project abteilung);

}
