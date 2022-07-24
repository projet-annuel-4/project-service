package fr.esgi.projectservice.mapper;

import fr.esgi.projectservice.domain.model.Project;
import fr.esgi.projectservice.request.CreateProjectRequest;
import fr.esgi.projectservice.request.UpdateProjectRequest;
import fr.esgi.projectservice.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectMapper {

    private final ModelMapper modelMapper;
    private final ProjectService projectService;

    public ProjectMapper(ModelMapper modelMapper, ProjectService projectService) {
        this.modelMapper = modelMapper;
        this.projectService = projectService;
    }

    public Project createProject(CreateProjectRequest request) {
        return projectService.createProject(request);
    }

    public List<Project> getProjectByGroup(Long groupId){
        return projectService.getProjectByGroupId(groupId);
    }

    public void deleteProject(Long id) {
        projectService.deleteProject(id);
    }

    public void updateProject(UpdateProjectRequest request, Long id) {
        projectService.updateProject(request, id);
    }
}
