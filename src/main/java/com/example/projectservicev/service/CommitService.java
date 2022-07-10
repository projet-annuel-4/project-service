package com.example.projectservicev.service;

import com.example.projectservicev.data.entity.CommitEntity;
import com.example.projectservicev.data.repository.BranchRepository;
import com.example.projectservicev.data.repository.CommitRepository;
import com.example.projectservicev.domain.mapper.BranchDomainMapper;
import com.example.projectservicev.domain.mapper.CommitDomainMapper;
import com.example.projectservicev.domain.model.Branch;
import com.example.projectservicev.domain.model.Commit;
import com.example.projectservicev.domain.model.File;
import com.example.projectservicev.domain.model.ModifiedFile;
import com.example.projectservicev.request.CreateCommitRequest;
import com.example.projectservicev.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.AssertTrue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommitService {

    private final CommitRepository commitRepository;
    private final BranchRepository branchRepository;
    private final CommitDomainMapper commitDomainMapper;
    private final BranchDomainMapper branchDomainMapper;
    private final ModifiedFileService modifiedFileService;
    private final BranchService branchService;
    private final DeltaService deltaService;
    private final FileService fileService;

    @Autowired
    public CommitService(CommitRepository commitRepository, BranchRepository branchRepository, CommitDomainMapper commitDomainMapper, BranchDomainMapper branchDomainMapper, ModifiedFileService modifiedFileService, BranchService branchService, DeltaService deltaService, FileService fileService) {
        this.commitRepository = commitRepository;
        this.branchRepository = branchRepository;
        this.commitDomainMapper = commitDomainMapper;
        this.branchDomainMapper = branchDomainMapper;
        this.modifiedFileService = modifiedFileService;
        this.branchService = branchService;
        this.deltaService = deltaService;
        this.fileService = fileService;
    }

    public void createCommit(Long idBranch, CreateCommitRequest request) {
        Branch branch = branchService.getBranchById(idBranch);
        Optional<CommitEntity> previousCommitOptional = commitRepository.findByBranchEntityAndChildIsNull(branchDomainMapper.convertModelToEntity(branch));
        Commit previousCommit;
        if( previousCommitOptional.isEmpty() ){
            previousCommit = null; // throw error;
        } else {
            previousCommit = commitDomainMapper.convertEntityToModel(previousCommitOptional.get());
        }
        Commit newCommit = new Commit();
        newCommit.setName(request.getName());
        newCommit.setCreationDate(DateTimeUtils.getDateNow());
        newCommit.setParent(previousCommit);
        newCommit.setBranch(branch);
        CommitEntity newCommitSavedEntity = commitRepository.save(commitDomainMapper.convertModelToEntity(newCommit));
        if( previousCommit != null){
            previousCommit.setChild(commitDomainMapper.convertEntityToModel(newCommitSavedEntity));
            commitRepository.save(commitDomainMapper.convertModelToEntity(previousCommit));
        }

        try {
            Files.createDirectory(Paths.get(idBranch.toString()));

        } catch (IOException e){
            //error directory creation
        }
        List<ModifiedFile> filesToPatch = modifiedFileService.setCommitToModifiedFiles(newCommit);
        // create repo on the docker
        // saveDeltas
        // delete repo docker
    }

    private void saveDeltas(Long idBranch, Commit commit, List<File> filesToCommit) throws IOException {
        for( File file : filesToCommit){
            deltaService.createDelta(idBranch, commit, file);
        }
    }

    public List<Commit> getAllCommitFromBranch(Long idBranch){
        List<CommitEntity> commitEntityEntities = commitRepository.findAllByBranchEntityId(idBranch);
        return commitEntityEntities.stream().map(commitDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    public Commit getParentCommit(Long idCommit){
        Optional<CommitEntity> targetCommitOptional = commitRepository.findById(idCommit);
        if( targetCommitOptional.isEmpty() ){
            throw new RuntimeException("notfound"); // throw error;
        }
        return commitDomainMapper.convertEntityToModel(targetCommitOptional.get()).getParent();
    }

    public Commit getChildCommit(Long idCommit){
        Optional<CommitEntity> targetCommitOptional = commitRepository.findById(idCommit);
        if( targetCommitOptional.isEmpty() ){
            throw new RuntimeException("notfound"); // throw error;
        }
        return commitDomainMapper.convertEntityToModel(targetCommitOptional.get()).getChild();
    }

    public List<Commit> getAllChild(Long idCommit){
        List<Commit> commitChildrens = new ArrayList();
        Commit commitChild;
        do{
            commitChild = getChildCommit(idCommit);
            if(commitChild == null){
                break;
            }
            commitChildrens.add(commitChild);
            idCommit = commitChild.getId();
        } while ( commitChild.getChild() != null );
        return commitChildrens;
    }

    public void deleteCommitByID(Long idCcommit){
        // apply delete delta and file
        commitRepository.deleteById(idCcommit);
    }

    public void deleteCommitAndChildById(Long idCommit){
        List<Commit> commitChildren = getAllChild(idCommit);
        for (Commit targetCommit: commitChildren){
            deltaService.deleteDeltaByCommit(targetCommit);
            fileService.deleteFileByCommit(targetCommit);
            deleteCommitByID(targetCommit.getId());
        }
        deleteCommitByID(idCommit);
    }


}
