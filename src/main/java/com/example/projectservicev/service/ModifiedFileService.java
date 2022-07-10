package com.example.projectservicev.service;

import com.example.projectservicev.data.entity.ModifiedFileEntity;
import com.example.projectservicev.data.repository.ModifiedFilesRepository;
import com.example.projectservicev.domain.mapper.BranchDomainMapper;
import com.example.projectservicev.domain.mapper.CommitDomainMapper;
import com.example.projectservicev.domain.mapper.FileDomainMapper;
import com.example.projectservicev.domain.mapper.ModifiedFilesDomainMapper;
import com.example.projectservicev.domain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModifiedFileService {

    private final ModifiedFilesRepository modifiedFilesRepository;
    private final ModifiedFilesDomainMapper modifiedFilesDomainMapper;
    private final FileDomainMapper fileDomainMapper;
    private final BranchDomainMapper branchDomainMapper;
    private final CommitDomainMapper commitDomainMapper;

    @Autowired
    public ModifiedFileService(ModifiedFilesRepository modifiedFilesRepository, ModifiedFilesDomainMapper modifiedFilesDomainMapper, FileDomainMapper fileDomainMapper, BranchDomainMapper branchDomainMapper, CommitDomainMapper commitMapper) {
        this.modifiedFilesRepository = modifiedFilesRepository;
        this.modifiedFilesDomainMapper = modifiedFilesDomainMapper;
        this.fileDomainMapper = fileDomainMapper;
        this.branchDomainMapper = branchDomainMapper;
        this.commitDomainMapper = commitMapper;
    }

    public void saveModifiedFiles(ModifiedFile modifiedFile){
        modifiedFilesRepository.save(modifiedFilesDomainMapper.convertModelToEntity(modifiedFile));
    }

    public void deleteByBranchAndFile(Branch branch, File file){
        modifiedFilesRepository.deleteByBranchEntityAndFileEntity(branchDomainMapper.convertModelToEntity(branch),
                                                                    fileDomainMapper.convertModelToEntity(file));
    }

    public List<ModifiedFile> getAllModifiedFilesByBranch(Branch branch){
        List<ModifiedFileEntity> modifiedFileEntities = modifiedFilesRepository
                .getAllByBranchEntity(branchDomainMapper.convertModelToEntity(branch));

        return modifiedFileEntities.stream().map(modifiedFilesDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    public List<ModifiedFile> getAllModifiedFilesByBranchAndType(Branch branch, ModifiedFileTypeEnum modificationType){
        List<ModifiedFileEntity> modifiedFileEntities = modifiedFilesRepository
                .getAllByBranchEntityAndModificationType(branchDomainMapper.convertModelToEntity(branch), modificationType);

        return modifiedFileEntities.stream().map(modifiedFilesDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    public List<ModifiedFile> setCommitToModifiedFiles(Commit commit){
        List<ModifiedFileEntity> modifiedFileEntities = modifiedFilesRepository.getAllByCommitEntityIsNull();
        for (ModifiedFileEntity modifiedFileEntity : modifiedFileEntities){
            modifiedFileEntity.setCommitEntity(commitDomainMapper.convertModelToEntity(commit));
            modifiedFilesRepository.save(modifiedFileEntity);
        }
        return modifiedFileEntities.stream().map(modifiedFilesDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }
}
