package com.example.projectservicev.controller;

import com.example.projectservicev.data.repository.BranchRepository;
import com.example.projectservicev.domain.model.Branch;
import com.example.projectservicev.domain.model.Commit;
import com.example.projectservicev.mapper.BranchMapper;
import com.example.projectservicev.mapper.CommitMapper;
import com.example.projectservicev.request.FileRequest;
import com.example.projectservicev.request.InitBranchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

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
    public void createBranch(@PathVariable("id") Long idProject){
        branchMapper.createBranch(idProject);
    }

    @PostMapping("/{idBranch}/init")
    public void initBranch(@PathVariable("idBranch") Long idBranch, @RequestParam("files") MultipartFile[] files) throws IOException, URISyntaxException, InterruptedException {
        branchMapper.initBranch(idBranch, files);
    }



}
