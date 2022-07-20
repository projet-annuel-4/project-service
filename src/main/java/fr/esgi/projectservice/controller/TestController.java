package fr.esgi.projectservice.controller;


import fr.esgi.projectservice.domain.model.Commit;
import fr.esgi.projectservice.domain.model.File;
import fr.esgi.projectservice.service.CommitService;
import fr.esgi.projectservice.service.DeltaService;
import fr.esgi.projectservice.service.FileService;
import fr.esgi.projectservice.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
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
