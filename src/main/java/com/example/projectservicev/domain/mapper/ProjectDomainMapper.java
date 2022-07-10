package com.example.projectservicev.domain.mapper;

import com.example.projectservicev.data.entity.ProjectEntity;
import com.example.projectservicev.domain.model.Project;
import com.example.projectservicev.request.CreateProjectRequest;
import com.example.projectservicev.request.UpdateProjectRequest;
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

    public ProjectEntity convertCreateRequestToEntity(CreateProjectRequest request){
        return modelMapper.map(request, ProjectEntity.class);
    }

    public ProjectEntity convertUpdateRequestToEntity(UpdateProjectRequest request){
        return modelMapper.map(request, ProjectEntity.class);
    }

    public Project convertEntityToModel(ProjectEntity projectEntity){
        System.out.println(projectEntity.toString());
        return new Project(
                projectEntity.getId(),
                projectEntity.getName(),
                projectEntity.getCreationDate(),
                projectEntity.isVisibility(),
                groupDomainMapper.convertEntityToModel(projectEntity.getGroupEntity())
        );
    }

    public ProjectEntity convertModelToEntity(Project project){
        return modelMapper.map(project, ProjectEntity.class);
    }

}
