package fr.esgi.projectservice.service;

import fr.esgi.projectservice.data.entity.CommitEntity;
import fr.esgi.projectservice.data.repository.BranchRepository;
import fr.esgi.projectservice.data.repository.CommitRepository;
import fr.esgi.projectservice.domain.mapper.BranchDomainMapper;
import fr.esgi.projectservice.domain.mapper.CommitDomainMapper;
import fr.esgi.projectservice.domain.model.*;
import fr.esgi.projectservice.request.CreateCommitRequest;
import fr.esgi.projectservice.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
    private final StorageService storageService;

    @Autowired
    public CommitService(CommitRepository commitRepository, BranchRepository branchRepository, CommitDomainMapper commitDomainMapper, BranchDomainMapper branchDomainMapper, ModifiedFileService modifiedFileService, BranchService branchService, DeltaService deltaService, FileService fileService, StorageService storageService) {
        this.commitRepository = commitRepository;
        this.branchRepository = branchRepository;
        this.commitDomainMapper = commitDomainMapper;
        this.branchDomainMapper = branchDomainMapper;
        this.modifiedFileService = modifiedFileService;
        this.branchService = branchService;
        this.deltaService = deltaService;
        this.fileService = fileService;
        this.storageService = storageService;
    }

    public void createCommit(Long branchId, CreateCommitRequest request) throws IOException, URISyntaxException, InterruptedException {
        Branch branch = branchService.getBranchById(branchId);
        Optional<CommitEntity> previousCommitOptional = commitRepository.findByBranchEntityAndChildIsNull(branchDomainMapper.convertModelToEntity(branch));
        Commit previousCommit;
        int order = 0;
        if (previousCommitOptional.isEmpty()) {
            previousCommit = null; // throw error;
        } else {
            previousCommit = commitDomainMapper.convertEntityToModel(previousCommitOptional.get(),0);
            order = previousCommit.getOrder() + 1;
        }
        Commit newCommit = new Commit();
        newCommit.setName(request.getName());
        newCommit.setCreationDate(DateTimeUtils.getDateNow());
        newCommit.setParent(previousCommit);
        newCommit.setBranch(branch);
        newCommit.setOrder(order);
        CommitEntity newCommitSavedEntity = commitRepository.save(commitDomainMapper.convertModelToEntity(newCommit, 0));
        if (previousCommit != null) {
            previousCommit.setChild(commitDomainMapper.convertEntityToModel(newCommitSavedEntity,0));
            commitRepository.save(commitDomainMapper.convertModelToEntity(previousCommit, 0));
        }

        Commit newCommitSaved = commitDomainMapper.convertEntityToModel(newCommitSavedEntity,0);

        try {
            Files.createDirectory(Paths.get(branchId.toString()));

        } catch (IOException e) {
            //error directory creation
        }
        List<ModifiedFile> commitToModifiedFiles = modifiedFileService.setCommitToModifiedFiles(newCommitSaved);

        List<File> filesToPatch = extractFilesToPatch(commitToModifiedFiles, newCommitSaved);

        String directoryPath = "/tmpDelta/" + branchId.toString() + "/";
        java.io.File dir = new java.io.File(directoryPath);
        if (!dir.mkdir()) {
            throw new RuntimeException("unable to create directory");
        }

        saveDeltas(branchId, newCommitSaved, filesToPatch);

        boolean test = dir.delete();
        System.out.println("dir deleted : " + test);
        // create repo on the docker
        // saveDeltas
        // delete repo docker
    }

    private List<File> extractFilesToPatch(List<ModifiedFile> modifiedFiles, Commit commit) throws IOException, URISyntaxException, InterruptedException {
        List<File> filesToPatch = new ArrayList<>();

        for (ModifiedFile modifiedFile : modifiedFiles) {
            if (modifiedFile.getModificationType() == ModifiedFileTypeEnum.DELETED) {
                //fileService.deleteFile(modifiedFile.getFile());
                continue;
            }
            if (modifiedFile.getModificationType() == ModifiedFileTypeEnum.CREATED) {
                fileService.setCommitCreationToFile(commit, modifiedFile.getFile().getId());
                storageService.copyFileFromDirectoryToDirectory(modifiedFile.getFile(), "actual", "last");
                continue;
            }
            fileService.setLastCommitNameToFile(commit,modifiedFile.getFile().getId());
            filesToPatch.add(modifiedFile.getFile());
        }
        return filesToPatch;
    }

    private List<File> extractFilesToRevert(List<ModifiedFile> modifiedFiles, Commit commit) throws IOException, URISyntaxException, InterruptedException {
        List<File> filesToPatch = new ArrayList<>();

        for (ModifiedFile modifiedFile : modifiedFiles) {
            if (modifiedFile.getModificationType() == ModifiedFileTypeEnum.DELETED) {
                fileService.restoreFile(modifiedFile.getFile());
                modifiedFileService.deleteById(modifiedFile.getId());
                continue;
            }
            if (modifiedFile.getModificationType() == ModifiedFileTypeEnum.CREATED) {
                fileService.deleteFile(modifiedFile.getFile());
                modifiedFileService.deleteById(modifiedFile.getId());
                // delete file definitely
//                fileService.setCommitCreationToFile(commit, modifiedFile.getFile().getId());
//                storageService.copyFileFromDirectoryToDirectory(modifiedFile.getFile(), "actual", "last");
                continue;
            }
            filesToPatch.add(modifiedFile.getFile());
            modifiedFileService.deleteById(modifiedFile.getId());
        }
        return filesToPatch;
    }

    private void saveDeltas(Long idBranch, Commit commit, List<File> filesToCommit) throws IOException, URISyntaxException, InterruptedException {
        for (File file : filesToCommit) {
            deltaService.createDelta(idBranch, commit, file);
            storageService.copyFileFromDirectoryToDirectory(file, "actual", "last");
        }
    }

    public List<Commit> getAllCommitFromBranch(Long idBranch) {
        List<CommitEntity> commitEntityEntities = commitRepository.findAllByBranchEntityId(idBranch);
        return commitDomainMapper.convertListEntitiesToListModels(commitEntityEntities);
    }

    public Commit getParentCommit(Long idCommit) {
        Optional<CommitEntity> targetCommitOptional = commitRepository.findById(idCommit);
        if (targetCommitOptional.isEmpty()) {
            throw new RuntimeException("notfound"); // throw error;
        }
        return commitDomainMapper.convertEntityToModel(targetCommitOptional.get(),0).getParent();
    }

    public Commit getCommitById(Long commitId) {
        Optional<CommitEntity> commitOptional = commitRepository.findById(commitId);
        if (commitOptional.isEmpty()) {
            throw new RuntimeException("commit not found"); // throw error;
        }
        return commitDomainMapper.convertEntityToModel(commitOptional.get(),0);
    }

    public Commit getChildCommit(Long idCommit) {
        Optional<CommitEntity> targetCommitOptional = commitRepository.findById(idCommit);
        if (targetCommitOptional.isEmpty()) {
            throw new RuntimeException("notfound"); // throw error;
        }
        return commitDomainMapper.convertEntityToModel(targetCommitOptional.get(),0).getChild();
    }

    public List<Commit> getAllChild(Long idCommit) {
        List<Commit> commitChildrens = new ArrayList();
        Commit commitChild;
        do {
            commitChild = getChildCommit(idCommit);
            if (commitChild == null) {
                break;
            }
            commitChildrens.add(commitChild);
            idCommit = commitChild.getId();
        } while (true);

        return commitChildrens;
    }

    public void deleteCommitByID(Long commitId) {
        CommitEntity parentCommitEntity = commitRepository.findByChild_Id(commitId);
        if (parentCommitEntity != null) {
            parentCommitEntity.setChild(null);
            commitRepository.save(parentCommitEntity);
        }
        commitRepository.deleteById(commitId);
    }

    public void deleteCommitAndChildById(Long idCommit) {
        List<Commit> commitChildren = getAllChild(idCommit);
        for (Commit targetCommit : commitChildren) {
            deltaService.deleteDeltaByCommit(targetCommit);
            fileService.deleteFileByCommit(targetCommit);
            deleteCommitByID(targetCommit.getId());
        }
        deleteCommitByID(idCommit);
    }

    public void revertToCommit(Long branchId, Long commitId) throws IOException, URISyntaxException, InterruptedException {
        //TODO set last file to actual and delete modified file with commit null
        Commit commit = getCommitById(commitId);
        List<Commit> commitChilds = getAllChild(commitId);
        commitChilds = sortCommitByOrderDesc(commitChilds);
        for (Commit child : commitChilds) {
            System.out.println("JE COMMENCE AVEC LE COMMIT : " + child.getId());
            List<ModifiedFile> commitModifications = modifiedFileService.getAllModifiedFilesByCommit(child);
            List<File> filesToRevert = extractFilesToRevert(commitModifications, child);
            revertDeltas(branchId, child, filesToRevert);
            deleteCommitByID(child.getId());
        }
        List<ModifiedFile> commitModifications = modifiedFileService.getAllModifiedFilesByCommit(commit);
        List<File> filesToRevert = extractFilesToRevert(commitModifications, commit);
        System.out.println("filesToRevert : " + filesToRevert.size());
        revertDeltas(branchId, commit, filesToRevert);
        deleteCommitByID(commit.getId());
    }

    public List<Commit> sortCommitByOrderDesc(List<Commit> commits){
        int n = commits.size();

        for (int i = 0; i < n-1; i++)
        {
            int min_idx = i;
            for (int j = i+1; j < n; j++)
                if (commits.get(j).getOrder() > commits.get(min_idx).getOrder())
                    min_idx = j;
            Commit temp = commits.get(min_idx);
            commits.set(min_idx, commits.get(i));
            commits.set(i, temp);
        }

        return commits;
    }

    public void revertDeltas(Long idBranch, Commit commit, List<File> fileToRevert) throws IOException, URISyntaxException, InterruptedException {
        for (File file : fileToRevert) {
            deltaService.revertDelta(idBranch, commit, file);
        }
    }


}
