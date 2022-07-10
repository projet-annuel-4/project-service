package com.example.projectservicev.controller;

import com.example.projectservicev.domain.model.File;
import com.example.projectservicev.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/branch/{idBranch}/file")
public class FileController {

    FileMapper fileMapper;

    @Autowired
    public FileController(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @PostMapping("/{idFile}/save")
    public void saveFile(@PathVariable("idBranch") Long idBranch, @PathVariable("idFile") Long idFile,
                         File fileToSave){

    }
}
