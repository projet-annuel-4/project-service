package com.example.projectservicev.mapper;

import com.example.projectservicev.domain.model.Branch;
import com.example.projectservicev.domain.model.Commit;
import com.example.projectservicev.request.CreateCommitRequest;
import com.example.projectservicev.service.CommitService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Component
public class CommitMapper {

    private final ModelMapper modelMapper;
    private final CommitService commitService;

    @Autowired
    public CommitMapper(ModelMapper modelMapper, CommitService commitService) {
        this.modelMapper = modelMapper;
        this.commitService = commitService;
    }

    public void createCommit(Long idBranch, CreateCommitRequest request) throws IOException, URISyntaxException, InterruptedException {
        commitService.createCommit(idBranch, request);
    }

    public List<Commit> getAllCommitFromBranchId(Long branchId){
        return commitService.getAllCommitFromBranch(branchId);
    }

    public void revertCommit(Long branchId, Long commitId) throws IOException, URISyntaxException, InterruptedException {
        commitService.revertToCommit(branchId, commitId);
    }

    public void deleteCommit(Long idCommit){
        commitService.deleteCommitAndChildById(idCommit);
    }
}
