package com.example.projectservicev.domain.mapper;

import com.example.projectservicev.data.entity.BranchEntity;
import com.example.projectservicev.domain.model.Branch;
import com.example.projectservicev.request.CreateProjectRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BranchDomainMapper {

    private final ModelMapper modelMapper;
    private final ProjectDomainMapper projectDomainMapper;

    public BranchDomainMapper(ModelMapper modelMapper, ProjectDomainMapper projectDomainMapper) {
        this.modelMapper = modelMapper;
        this.projectDomainMapper = projectDomainMapper;
    }

    public com.example.projectservicev.domain.model.Branch convertCreateRequestToModel(CreateProjectRequest request){
        return modelMapper.map(request, com.example.projectservicev.domain.model.Branch.class);
    }

    public com.example.projectservicev.domain.model.Branch convertEntityToModel(BranchEntity branchEntity){
        if ( branchEntity == null){
            return null;
        }
        Branch branch = new Branch();
        branch.setId(branchEntity.getId());
        branch.setName(branchEntity.getName());
        branch.setCreationDate(branchEntity.getCreationDate());
        branch.setProject(projectDomainMapper.convertEntityToModel(branchEntity.getProjectEntity()));
        return branch;
    }

    public BranchEntity convertModelToEntity(com.example.projectservicev.domain.model.Branch branch){
        if ( branch == null){
            return null;
        }
        BranchEntity branchEntity = new BranchEntity();
        branchEntity.setId(branch.getId() == null ? null : branch.getId());
        branchEntity.setProjectEntity(projectDomainMapper.convertModelToEntity(branch.getProject()));
        branchEntity.setName(branch.getName());
        branchEntity.setCreationDate(branch.getCreationDate());
        return branchEntity;
    }
}
