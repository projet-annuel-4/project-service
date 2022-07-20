package com.example.projectservicev.controller;

import com.example.projectservicev.domain.model.File;
import com.example.projectservicev.dto.FileFromDownloadService;
import com.example.projectservicev.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/branch/{idBranch}/file")
public class FileController {

    FileMapper fileMapper;

    @Autowired
    public FileController(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @PostMapping("/create")
    public void createFile(@PathVariable("idBranch") Long branchId, @RequestParam("file") MultipartFile fileToCreate) throws IOException, URISyntaxException, InterruptedException {
        fileMapper.createFile(branchId, fileToCreate);
    }

    @PostMapping("/{fileId}/save")
    public void saveFile(@PathVariable("idBranch") Long idBranch, @PathVariable("fileId") Long fileId, @RequestParam("file") MultipartFile file) throws IOException, URISyntaxException, InterruptedException {
        fileMapper.saveFile(idBranch, fileId, file);
    }

    @GetMapping("/{fileId}/get")
    public ResponseEntity<byte[]> getFile(@PathVariable("fileId") Long fileId, @RequestParam("type") String type) throws ChangeSetPersister.NotFoundException, IOException, URISyntaxException, InterruptedException {
        return ResponseEntity.ok(fileMapper.getFile(fileId, type));
    }

    @DeleteMapping("/{fileId}/get")
    public ResponseEntity<File> deleteFile(@PathVariable("idBranch") Long idBranch, @PathVariable("fileId") Long fileId){
        return ResponseEntity.ok(fileMapper.deleteFile(idBranch, fileId));
    }
}