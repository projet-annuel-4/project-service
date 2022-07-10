package com.example.projectservicev.controller;

import com.example.projectservicev.data.entity.GroupEntity;
import com.example.projectservicev.domain.model.Project;
import com.example.projectservicev.mapper.BranchMapper;
import com.example.projectservicev.mapper.ProjectMapper;
import com.example.projectservicev.request.CreateProjectRequest;
import com.example.projectservicev.request.UpdateProjectRequest;
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
    public void createProject(@RequestBody CreateProjectRequest request){
        request.setGroupEntity(new GroupEntity(1L));
        Project projectCreated = projectMapper.createProject(request);
        branchMapper.createBranch(projectCreated.getId());
    }

    @DeleteMapping("/deleteProject/{id}")
    public void deleteProject(@PathVariable("id") Long id){
        projectMapper.deleteProject(id);
    }

    @PutMapping("/updateProject/{id}")
    public void updateProject(@RequestBody UpdateProjectRequest request, @PathVariable("id") Long id){
        projectMapper.updateProject(request, id);
    }

}
