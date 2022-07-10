package com.example.projectservicev.mapper;

import com.example.projectservicev.domain.model.File;
import com.example.projectservicev.service.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {

    private final ModelMapper modelMapper;
    private final FileService fileService;

    @Autowired
    public FileMapper(ModelMapper modelMapper, FileService fileService) {
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }

    public void saveFile(Long idBranch, File fileToSave){

    }
}
