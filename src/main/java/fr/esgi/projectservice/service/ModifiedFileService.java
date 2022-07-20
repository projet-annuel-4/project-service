package fr.esgi.projectservice.service;

import fr.esgi.projectservice.data.entity.ModifiedFileEntity;
import fr.esgi.projectservice.data.repository.ModifiedFilesRepository;
import fr.esgi.projectservice.domain.mapper.BranchDomainMapper;
import fr.esgi.projectservice.domain.mapper.CommitDomainMapper;
import fr.esgi.projectservice.domain.mapper.FileDomainMapper;
import fr.esgi.projectservice.domain.mapper.ModifiedFilesDomainMapper;
import fr.esgi.projectservice.domain.model.*;
import org.springframework.context.annotation.Lazy;
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
    private final BranchService branchService;

    public ModifiedFileService(ModifiedFilesRepository modifiedFilesRepository, ModifiedFilesDomainMapper modifiedFilesDomainMapper, FileDomainMapper fileDomainMapper, BranchDomainMapper branchDomainMapper, CommitDomainMapper commitDomainMapper, @Lazy BranchService branchService) {
        this.modifiedFilesRepository = modifiedFilesRepository;
        this.modifiedFilesDomainMapper = modifiedFilesDomainMapper;
        this.fileDomainMapper = fileDomainMapper;
        this.branchDomainMapper = branchDomainMapper;
        this.commitDomainMapper = commitDomainMapper;
        this.branchService = branchService;
    }

    public ModifiedFile saveModifiedFiles(File fileModified, ModifiedFileTypeEnum modificationType) {
        ModifiedFileEntity modifiedFileEntityFound = modifiedFilesRepository.getByFileEntity_IdAndAndCommitEntityIsNull(fileModified.getId());
        if (modifiedFileEntityFound != null) {
            return modifiedFilesDomainMapper.convertEntityToModel(modifiedFileEntityFound);
        }

        ModifiedFile modifiedFile = new ModifiedFile();
        modifiedFile.setAttachedCommit(null);
        modifiedFile.setFile(fileModified);
        modifiedFile.setModificationType(modificationType);
        modifiedFile.setBranch(branchService.getBranchById(fileModified.getBranch().getId()));

        ModifiedFileEntity modifiedFileEntity = modifiedFilesRepository.save(modifiedFilesDomainMapper.convertModelToEntity(modifiedFile));

        return modifiedFilesDomainMapper.convertEntityToModel(modifiedFileEntity);
    }

    public void deleteById(Long modifiedFileId) {
        modifiedFilesRepository.deleteById(modifiedFileId);
    }


    public void deleteByBranchAndFile(Branch branch, File file) {
        modifiedFilesRepository.deleteByBranchEntityAndFileEntity(branchDomainMapper.convertModelToEntity(branch),
                fileDomainMapper.convertModelToEntity(file));
    }

    public List<ModifiedFile> getAllModifiedFilesByBranch(Branch branch) {
        List<ModifiedFileEntity> modifiedFileEntities = modifiedFilesRepository
                .getAllByBranchEntity(branchDomainMapper.convertModelToEntity(branch));

        return modifiedFileEntities.stream().map(modifiedFilesDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    public List<ModifiedFile> getAllModifiedFilesByBranchAndType(Branch branch, ModifiedFileTypeEnum modificationType) {
        List<ModifiedFileEntity> modifiedFileEntities = modifiedFilesRepository
                .getAllByBranchEntityAndModificationType(branchDomainMapper.convertModelToEntity(branch), modificationType);

        return modifiedFileEntities.stream().map(modifiedFilesDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    public List<ModifiedFile> setCommitToModifiedFiles(Commit commit) {
        List<ModifiedFileEntity> modifiedFileEntities = modifiedFilesRepository.getAllByCommitEntityIsNull();
        for (ModifiedFileEntity modifiedFileEntity : modifiedFileEntities) {
            modifiedFileEntity.setCommitEntity(commitDomainMapper.convertModelToEntity(commit, 0));
            modifiedFilesRepository.save(modifiedFileEntity);
        }
        return modifiedFileEntities.stream().map(modifiedFilesDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    public List<ModifiedFile> getAllModifiedFilesByCommit(Commit commit) {
        List<ModifiedFileEntity> modifiedFileEntities = modifiedFilesRepository.getAllByCommitEntity_Id(commit.getId());
        return modifiedFileEntities.stream().map(modifiedFilesDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    // getall where commit = null
}
