package com.example.projectservicev.domain.mapper;

import com.example.projectservicev.data.entity.DeltaEntity;
import com.example.projectservicev.domain.model.Delta;
import com.example.projectservicev.request.CreateProjectRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeltaDomainMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public DeltaDomainMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Delta convertCreateRequestToModel(CreateProjectRequest request){
        return modelMapper.map(request, Delta.class);
    }

    public Delta convertEntityToModel(DeltaEntity deltaEntity){
        return modelMapper.map(deltaEntity, Delta.class);
    }

    public DeltaEntity convertModelToEntity(Delta delta){
        return modelMapper.map(delta, DeltaEntity.class);
    }

}
