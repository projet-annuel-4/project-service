package com.example.projectservicev.service;

import com.example.projectservicev.data.entity.FileEntity;
import com.example.projectservicev.data.repository.FileRepository;
import com.example.projectservicev.domain.mapper.CommitDomainMapper;
import com.example.projectservicev.domain.mapper.FileDomainMapper;
import com.example.projectservicev.domain.model.Commit;
import com.example.projectservicev.domain.model.File;
import com.example.projectservicev.domain.model.ModifiedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FileService {

    private final FileRepository fileRepository;
    private final FileDomainMapper fileDomainMapper;
    private final CommitDomainMapper commitDomainMapper;
    private final ModifiedFileService modifiedFileService;

    @Autowired
    public FileService(FileRepository fileRepository, FileDomainMapper fileDomainMapper, CommitDomainMapper commitDomainMapper, ModifiedFileService modifiedFileService) {
        this.fileRepository = fileRepository;
        this.fileDomainMapper = fileDomainMapper;
        this.commitDomainMapper = commitDomainMapper;
        this.modifiedFileService = modifiedFileService;
    }

    public void addFileToBranch(Long idBranch, File fileToAdd){

        fileToAdd.setFromInit(false);
        fileToAdd.setCommitCreation(null);
        fileToAdd.setLastCommitName(null);
        fileRepository.save(fileDomainMapper.convertModelToEntity(fileToAdd));
    }

    public List<File> addFilesToBranch( List<File> filesToAdd){
        List<FileEntity> fileEntities = fileRepository.saveAll(filesToAdd.stream().map(fileDomainMapper::convertModelToEntity).collect(Collectors.toList()));
        return fileEntities.stream().map(fileDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    public File getFileById(Long idFile) throws ChangeSetPersister.NotFoundException {
        Optional<FileEntity> fileFoundOptional = fileRepository.findById(idFile);
        if( fileFoundOptional.isEmpty() ){
            throw new ChangeSetPersister.NotFoundException();// throw error;
        }
        return fileDomainMapper.convertEntityToModel(fileFoundOptional.get());
    }

    public List<File> getFilesFromBranch(Long idBranch){
        List<FileEntity> foundedFiles = fileRepository.findAllByBranchEntityId(idBranch);
        return foundedFiles.stream().map(fileDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    public List<File> getFilesByCommit(Commit commit){
        List<FileEntity> foundedFiles = fileRepository.findAllByCommitEntityCreation(commitDomainMapper.convertModelToEntity(commit));
        return foundedFiles.stream().map(fileDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    public void deleteFileByCommit(Commit commit){
        List<FileEntity> foundedFiles = fileRepository.findAllByCommitEntityCreation(commitDomainMapper.convertModelToEntity(commit));
        fileRepository.deleteAll(foundedFiles);
    }

    public void saveFile(File file){
        // save to server
//        ModifiedFile modifiedFile
    }

    public List<File> getAllFileModified(List<ModifiedFile> modifiedFiles){
        List<File> modifiedFilesFound = new ArrayList<>();

        for (ModifiedFile modifiedFile: modifiedFiles){
            Optional<FileEntity> modifiedFileFoundOptional = fileRepository.findById(modifiedFile.getFileEntity().getId());
            modifiedFileFoundOptional.ifPresent(fileEntity -> modifiedFilesFound.add(fileDomainMapper.convertEntityToModel(fileEntity)));
        }
        return modifiedFilesFound;
    }


}
