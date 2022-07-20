package fr.esgi.projectservice.controller;

import fr.esgi.projectservice.data.entity.GroupEntity;
import fr.esgi.projectservice.domain.model.Project;
import fr.esgi.projectservice.mapper.BranchMapper;
import fr.esgi.projectservice.mapper.ProjectMapper;
import fr.esgi.projectservice.request.CreateProjectRequest;
import fr.esgi.projectservice.request.UpdateProjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectMapper projectMapper;
    private final BranchMapper branchMapper;

    @Autowired
    public ProjectController(ProjectMapper projectMapper, BranchMapper branchMapper) {
        this.projectMapper = projectMapper;
        this.branchMapper = branchMapper;
    }

    @PostMapping("/createProject")
    public void createProject(@RequestBody CreateProjectRequest request) {
        request.setGroupEntity(new GroupEntity(1L));
        Project projectCreated = projectMapper.createProject(request);
        branchMapper.createBranch(projectCreated.getId());
    }

    @DeleteMapping("/deleteProject/{id}")
    public void deleteProject(@PathVariable("id") Long id) {
        projectMapper.deleteProject(id);
    }

    @PutMapping("/updateProject/{id}")
    public void updateProject(@RequestBody UpdateProjectRequest request, @PathVariable("id") Long id) {
        projectMapper.updateProject(request, id);
    }

}
