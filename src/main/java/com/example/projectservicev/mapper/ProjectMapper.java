package com.example.projectservicev.mapper;

import com.example.projectservicev.domain.model.Project;
import com.example.projectservicev.request.CreateProjectRequest;
import com.example.projectservicev.request.UpdateProjectRequest;
import com.example.projectservicev.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    private final ModelMapper modelMapper;
    private final ProjectService projectService;

    public ProjectMapper(ModelMapper modelMapper, ProjectService projectService) {
        this.modelMapper = modelMapper;
        this.projectService = projectService;
    }

    public Project createProject(CreateProjectRequest request){
        return projectService.createProject(request);
    }

    public void deleteProject(Long id){
        projectService.deleteProject(id);
    }

    public void updateProject(UpdateProjectRequest request, Long id){
        projectService.updateProject(request, id);
    }
}
