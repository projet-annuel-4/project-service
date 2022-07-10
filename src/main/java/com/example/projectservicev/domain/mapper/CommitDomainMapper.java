package com.example.projectservicev.domain.mapper;

import com.example.projectservicev.data.entity.CommitEntity;
import com.example.projectservicev.request.CreateProjectRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class CommitDomainMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public CommitDomainMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public com.example.projectservicev.domain.model.Commit convertCreateRequestToModel(CreateProjectRequest request){
        return modelMapper.map(request, com.example.projectservicev.domain.model.Commit.class);
    }

    public com.example.projectservicev.domain.model.Commit convertEntityToModel(CommitEntity commitEntity){
        return modelMapper.map(commitEntity, com.example.projectservicev.domain.model.Commit.class);
    }

    public CommitEntity convertModelToEntity(com.example.projectservicev.domain.model.Commit commit){
        return modelMapper.map(commit, CommitEntity.class);
    }

}
