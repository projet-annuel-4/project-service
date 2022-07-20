package com.example.projectservicev.service;

import com.example.projectservicev.data.entity.ProjectEntity;
import com.example.projectservicev.data.repository.ProjectRepository;
import com.example.projectservicev.domain.mapper.ProjectDomainMapper;
import com.example.projectservicev.domain.model.Project;
import com.example.projectservicev.request.CreateProjectRequest;
import com.example.projectservicev.request.UpdateProjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectDomainMapper projectDomainMapper;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectDomainMapper projectDomainMapper) {
        this.projectRepository = projectRepository;
        this.projectDomainMapper = projectDomainMapper;
    }

    public Project createProject(CreateProjectRequest request){
        ProjectEntity projectToSave = projectDomainMapper.convertCreateRequestToEntity(request);
        projectToSave.setCreationDate(new Date());
        return projectDomainMapper.convertEntityToModel(projectRepository.save(projectToSave));
    }

    public void deleteProject(Long id){
        projectRepository.deleteById(id);
    }

    public void updateProject(UpdateProjectRequest request, Long id){
        Optional<ProjectEntity> projectEntityOptional = projectRepository.findById(id);
        if( projectEntityOptional.isEmpty() ){
            return; // throw error;
        }
        com.example.projectservicev.domain.model.Project updatedProject = projectDomainMapper.convertEntityToModel(projectEntityOptional.get());
        updatedProject.setName(request.getName());
        updatedProject.setVisibility(request.isVisibility());
        projectRepository.save(projectDomainMapper.convertModelToEntity(updatedProject));

    }



}
