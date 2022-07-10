package com.example.projectservicev.domain.mapper;

import com.example.projectservicev.data.entity.FileEntity;
import com.example.projectservicev.domain.model.File;
import com.example.projectservicev.dto.FileUpload;
import com.example.projectservicev.request.CreateProjectRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileDomainMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public FileDomainMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public File convertCreateRequestToModel(CreateProjectRequest request){
        return modelMapper.map(request, File.class);
    }

    public File convertEntityToModel(FileEntity fileEntity){
        return modelMapper.map(fileEntity, File.class);
    }

    public FileEntity convertModelToEntity(File file){
        return modelMapper.map(file, FileEntity.class);
    }

//    public FileUpload convertModelToFileUpload(File file){
//        return new FileUpload(
//            file.getId(),
//                null,
//                "actual",
//                file.getFile().getOriginalFilename(),
//
//        )
//    }
}
