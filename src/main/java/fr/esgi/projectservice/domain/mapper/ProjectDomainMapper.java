package fr.esgi.projectservice.domain.mapper;

import fr.esgi.projectservice.data.entity.ProjectEntity;
import fr.esgi.projectservice.domain.model.Project;
import fr.esgi.projectservice.request.CreateProjectRequest;
import fr.esgi.projectservice.request.UpdateProjectRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ProjectDomainMapper {

    private final ModelMapper modelMapper;
    private final GroupDomainMapper groupDomainMapper;

    @Autowired
    public ProjectDomainMapper(ModelMapper modelMapper, GroupDomainMapper groupDomainMapper) {
        this.modelMapper = modelMapper;
        this.groupDomainMapper = groupDomainMapper;
    }

    public ProjectEntity convertCreateRequestToEntity(CreateProjectRequest request) {
        return modelMapper.map(request, ProjectEntity.class);

    }

    public ProjectEntity convertUpdateRequestToEntity(UpdateProjectRequest request) {
        return modelMapper.map(request, ProjectEntity.class);
    }

    public Project convertEntityToModel(ProjectEntity projectEntity) {
        if (projectEntity == null) {
            return null;
        }
        return new Project(
                projectEntity.getId(),
                projectEntity.getName(),
                projectEntity.getCreationDate(),
                projectEntity.isVisibility(),
                groupDomainMapper.convertEntityToModel(projectEntity.getGroupEntity())
        );
    }

    public ProjectEntity convertModelToEntity(Project project) {
        if (project == null) {
            return null;
        }
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(project.getId());
        projectEntity.setGroupEntity(groupDomainMapper.convertModelToEntity(project.getGroup()));
        projectEntity.setName(project.getName());
        projectEntity.setCreationDate(project.getCreationDate());
        projectEntity.setVisibility(project.getVisibility());
        return modelMapper.map(project, ProjectEntity.class);
    }

}
