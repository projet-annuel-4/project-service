package com.example.projectservicev.service;

import com.example.projectservicev.data.entity.BranchEntity;
import com.example.projectservicev.data.entity.ProjectEntity;
import com.example.projectservicev.data.repository.BranchRepository;
import com.example.projectservicev.data.repository.ProjectRepository;
import com.example.projectservicev.domain.mapper.BranchDomainMapper;
import com.example.projectservicev.domain.mapper.ProjectDomainMapper;
import com.example.projectservicev.domain.model.Branch;
import com.example.projectservicev.domain.model.File;
import com.example.projectservicev.domain.model.Project;
import com.example.projectservicev.request.InitBranchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BranchService {

    private final BranchRepository branchRepository;
    private final FileService fileService;
    private final ProjectRepository projectRepository;
    private final BranchDomainMapper branchDomainMapper;
    private final ProjectDomainMapper projectDomainMapper;
    private final StorageService storageService;

    @Autowired
    public BranchService(BranchRepository branchRepository, FileService fileService, ProjectRepository projectRepository, BranchDomainMapper branchDomainMapper, ProjectDomainMapper projectDomainMapper, StorageService storageService) {
        this.branchRepository = branchRepository;
        this.fileService = fileService;
        this.projectRepository = projectRepository;
        this.branchDomainMapper = branchDomainMapper;
        this.projectDomainMapper = projectDomainMapper;
        this.storageService = storageService;
    }

    public void createBranch(Long idProject){
        Optional<ProjectEntity> projectFoundOptional = projectRepository.findById(idProject);
        if( projectFoundOptional.isEmpty() ){
            return; // throw error;
        }
        Project projectParent = projectDomainMapper.convertEntityToModel(projectFoundOptional.get());
        com.example.projectservicev.domain.model.Branch newBranch = new com.example.projectservicev.domain.model.Branch();
        newBranch.setName("Main");
        newBranch.setProject(projectParent);
        newBranch.setCreationDate(new Date());
        branchRepository.save(branchDomainMapper.convertModelToEntity(newBranch));
    }

    public Branch getBranchById(Long idBranch){
        Optional<BranchEntity> branchEntityOptional = branchRepository.findById(idBranch);
        if( branchEntityOptional.isEmpty() ){
            return null; // throw error;
        }
        return branchDomainMapper.convertEntityToModel(branchEntityOptional.get());
    }

    public void initBranch(Long idBranch, MultipartFile[] files) throws IOException, URISyntaxException, InterruptedException {

        Branch branch = getBranchById(idBranch);
        List<File> filesToSaves = new ArrayList<>();
        for (MultipartFile file : files){
            File fileToSave = new File();
            fileToSave.setLastCommitName("init");
            fileToSave.setBranch(branch);
            fileToSave.setFromInit(true);
            fileToSave.setCommitCreation(null);
            fileToSave.setName(file.getOriginalFilename());
            fileToSave.setUrl("none");
            fileToSave.setFile(file);
            fileToSave.setDeleted(false);
            filesToSaves.add(fileToSave);
        }
        List<File> fileSaved = fileService.addFilesToBranch( filesToSaves );
        List<File> fileSavedWithFile = setMultipartInSavedFiles(filesToSaves, fileSaved);
        storageService.uploadFiles(idBranch, fileSavedWithFile);
    }

    private List<File> setMultipartInSavedFiles(List<File> filesWithFile, List<File> filesSaved){
        for( int i = 0; i < filesWithFile.size(); i++){
            filesSaved.get(i).setFile(filesWithFile.get(i).getFile());
        }
        return filesSaved;
    }
}
