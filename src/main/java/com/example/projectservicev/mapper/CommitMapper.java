package com.example.projectservicev.mapper;

import com.example.projectservicev.domain.model.Branch;
import com.example.projectservicev.domain.model.Commit;
import com.example.projectservicev.request.CreateCommitRequest;
import com.example.projectservicev.service.CommitService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
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

    public void createCommit(Long idBranch, CreateCommitRequest request) throws IOException {
        commitService.createCommit(idBranch, request);
    }

    public List<Commit> getAllCommitFromBranchId(Long branchId){
        return commitService.getAllCommitFromBranch(branchId);
    }

    public void deleteCommit(Long idCommit){
        commitService.deleteCommitAndChildById(idCommit);
    }
}
