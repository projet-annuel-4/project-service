package fr.esgi.projectservice.controller;

import fr.esgi.projectservice.mapper.BranchMapper;
import fr.esgi.projectservice.mapper.CommitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/branch")
public class BranchController {

    private final BranchMapper branchMapper;
    private final CommitMapper commitMapper;

    @Autowired
    public BranchController(BranchMapper branchMapper, CommitMapper commitMapper) {
        this.branchMapper = branchMapper;
        this.commitMapper = commitMapper;
    }

    @PostMapping("/createBranch/{id}")
    public void createBranch(@PathVariable("id") Long idProject) {
        branchMapper.createBranch(idProject);
    }

    @PostMapping("/{idBranch}/init")
    public void initBranch(@PathVariable("idBranch") Long idBranch, @RequestParam("files") MultipartFile[] files) throws IOException, URISyntaxException, InterruptedException {
        branchMapper.initBranch(idBranch, files);
    }


}
