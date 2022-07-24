package fr.esgi.projectservice.service;

import fr.esgi.projectservice.bus.CreatedProjectProducer;
import fr.esgi.projectservice.bus.DeletedProjectProducer;
import fr.esgi.projectservice.data.entity.ProjectEntity;
import fr.esgi.projectservice.data.repository.ProjectRepository;
import fr.esgi.projectservice.domain.mapper.ProjectDomainMapper;
import fr.esgi.projectservice.domain.model.Project;
import fr.esgi.projectservice.request.CreateProjectRequest;
import fr.esgi.projectservice.request.UpdateProjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectDomainMapper projectDomainMapper;
    private final CreatedProjectProducer createdProjectProducer;
    private final DeletedProjectProducer deletedProjectProducer;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectDomainMapper projectDomainMapper, CreatedProjectProducer createdProjectProducer, DeletedProjectProducer deletedProjectProducer) {
        this.projectRepository = projectRepository;
        this.projectDomainMapper = projectDomainMapper;
        this.createdProjectProducer = createdProjectProducer;
        this.deletedProjectProducer = deletedProjectProducer;
    }

    public Project createProject(CreateProjectRequest request) {
        var projectToSave = projectDomainMapper.convertCreateRequestToEntity(request);
        projectToSave.setCreationDate(new Date());
        projectRepository.saveAndFlush(projectToSave);
        var project = projectDomainMapper.convertEntityToModel(projectToSave);
        createdProjectProducer.projectCreated(project);
        return project;
    }


    public List<Project> getProjectByGroupId(Long groupId){
        List<ProjectEntity> projectEntities = projectRepository.getAllByGroupEntity_Id(groupId);
        return projectEntities.stream().map(projectDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }


    @Transactional
    public void deleteProject(Long id) {
        var project = projectRepository.findById(id);
        projectRepository.deleteById(id);
        deletedProjectProducer.projectDeleted(projectDomainMapper.convertEntityToModel(project.get()));
    }

    public void updateProject(UpdateProjectRequest request, Long id) {
        Optional<ProjectEntity> projectEntityOptional = projectRepository.findById(id);
        if (projectEntityOptional.isEmpty()) {
            return; // throw error;
        }
        Project updatedProject = projectDomainMapper.convertEntityToModel(projectEntityOptional.get());
        updatedProject.setName(request.getName());
        updatedProject.setVisibility(request.isVisibility());
        projectRepository.save(projectDomainMapper.convertModelToEntity(updatedProject));

    }


}
