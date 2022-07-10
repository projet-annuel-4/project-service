package com.example.projectservicev.domain.mapper;

import com.example.projectservicev.data.entity.ModifiedFileEntity;
import com.example.projectservicev.domain.model.ModifiedFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModifiedFilesDomainMapper {

    private ModelMapper modelMapper;

    @Autowired
    public ModifiedFilesDomainMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ModifiedFile convertEntityToModel(ModifiedFileEntity modifiedFileEntity){
        return modelMapper.map(modifiedFileEntity, ModifiedFile.class);
    }

    public ModifiedFileEntity convertModelToEntity(ModifiedFile modifiedFile){
        return modelMapper.map(modifiedFile, ModifiedFileEntity.class);
    }
}
