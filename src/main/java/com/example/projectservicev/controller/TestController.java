package com.example.projectservicev.controller;


import com.example.projectservicev.domain.model.Commit;
import com.example.projectservicev.domain.model.File;
import com.example.projectservicev.dto.FileFromDownloadService;
import com.example.projectservicev.service.CommitService;
import com.example.projectservicev.service.DeltaService;
import com.example.projectservicev.service.FileService;
import com.example.projectservicev.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final FileService fileService;
    private final StorageService storageService;
    private final DeltaService deltaService;
    private final CommitService commitService;

    @Autowired
    public TestController(FileService fileService, StorageService storageService, DeltaService deltaService, CommitService commitService) {
        this.fileService = fileService;
        this.storageService = storageService;
        this.deltaService = deltaService;
        this.commitService = commitService;
    }

    @GetMapping("/{fileId}/get")
    public String test(@PathVariable("fileId") Long fileId, @RequestParam("type") String type) throws ChangeSetPersister.NotFoundException, IOException, URISyntaxException, InterruptedException {
        File file = fileService.getFileById(fileId);
        Commit commit = commitService.getCommitById(55L);
        deltaService.createDelta(file.getBranch().getId(), commit, file);
        return "ook";
    }


}
