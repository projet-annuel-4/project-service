package com.example.projectservicev.domain.mapper;

import com.example.projectservicev.data.entity.CommitEntity;
import com.example.projectservicev.request.CreateProjectRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component

public class CommitDomainMapper {

    private final ModelMapper modelMapper;
    private final BranchDomainMapper branchDomainMapper;

    @Autowired
    public CommitDomainMapper(ModelMapper modelMapper, BranchDomainMapper branchDomainMapper) {
        this.modelMapper = modelMapper;
        this.branchDomainMapper = branchDomainMapper;
    }

    public com.example.projectservicev.domain.model.Commit convertCreateRequestToModel(CreateProjectRequest request){
        return modelMapper.map(request, com.example.projectservicev.domain.model.Commit.class);
    }

    public com.example.projectservicev.domain.model.Commit convertEntityToModel(CommitEntity commitEntity){
        return modelMapper.map(commitEntity, com.example.projectservicev.domain.model.Commit.class);
    }

    public CommitEntity convertModelToEntity(com.example.projectservicev.domain.model.Commit commit, int loop){
        if( loop == 2){
            return null;
        }
        if ( commit == null){
            return null;
        }
        CommitEntity commitEntity = new CommitEntity();
        commitEntity.setId(commit.getId() == null ? null : commit.getId());
        commitEntity.setCreationDate(new Date());
        commitEntity.setBranch(branchDomainMapper.convertModelToEntity(commit.getBranch()));
        commitEntity.setName(commit.getName());
        commitEntity.setChild(commit.getChild() == null ? null : convertModelToEntity(commit.getChild(), loop+1));
        commitEntity.setParent(commit.getParent() == null ? null : convertModelToEntity(commit.getParent(), loop+1 ));

        return commitEntity;
    }

}
