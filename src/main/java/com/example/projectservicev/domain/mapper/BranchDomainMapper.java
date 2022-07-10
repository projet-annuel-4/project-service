package com.example.projectservicev.domain.mapper;

import com.example.projectservicev.data.entity.BranchEntity;
import com.example.projectservicev.request.CreateProjectRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BranchDomainMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public BranchDomainMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public com.example.projectservicev.domain.model.Branch convertCreateRequestToModel(CreateProjectRequest request){
        return modelMapper.map(request, com.example.projectservicev.domain.model.Branch.class);
    }

    public com.example.projectservicev.domain.model.Branch convertEntityToModel(BranchEntity branchEntity){
        return modelMapper.map(branchEntity, com.example.projectservicev.domain.model.Branch.class);
    }

    public BranchEntity convertModelToEntity(com.example.projectservicev.domain.model.Branch branch){
        return modelMapper.map(branch, BranchEntity.class);
    }
}
