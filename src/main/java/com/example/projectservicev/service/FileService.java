package com.example.projectservicev.service;

import com.example.projectservicev.data.entity.FileEntity;
import com.example.projectservicev.data.repository.FileRepository;
import com.example.projectservicev.domain.mapper.CommitDomainMapper;
import com.example.projectservicev.domain.mapper.FileDomainMapper;
import com.example.projectservicev.domain.model.*;
import com.example.projectservicev.dto.FileFromDownloadService;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
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
    private final BranchService branchService;
    private final StorageService storageService;

    @Autowired
    public FileService(FileRepository fileRepository, FileDomainMapper fileDomainMapper, CommitDomainMapper commitDomainMapper, ModifiedFileService modifiedFileService, @Lazy BranchService branchService, StorageService storageService) {
        this.fileRepository = fileRepository;
        this.fileDomainMapper = fileDomainMapper;
        this.commitDomainMapper = commitDomainMapper;
        this.modifiedFileService = modifiedFileService;
        this.branchService = branchService;
        this.storageService = storageService;
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

    @Transactional
    public File deleteFileById(Long idBranch, Long fileId){
        FileEntity fileEntity = fileRepository.findByIdAndBranchEntity_Id(fileId, idBranch);
        if( fileEntity == null){
            //ERROR
        }
        fileEntity.setDeleted(true);
        File fileDeleted = fileDomainMapper.convertEntityToModel(fileRepository.save(fileEntity));
        modifiedFileService.saveModifiedFiles(fileDeleted, ModifiedFileTypeEnum.DELETED);

        return fileDomainMapper.convertEntityToModel(fileRepository.save(fileEntity));
    }

    public byte[] getFileOnServerById(Long idFile, String type) throws ChangeSetPersister.NotFoundException, IOException, URISyntaxException, InterruptedException {
        Optional<FileEntity> fileFoundOptional = fileRepository.findById(idFile);
        if( fileFoundOptional.isEmpty() ){
            throw new ChangeSetPersister.NotFoundException();// throw error;
        }
        File file = fileDomainMapper.convertEntityToModel(fileFoundOptional.get());
        return storageService.getFileOnServerTest(file.getId(), type);
//        InputStream inputStream = new ByteArrayInputStream(fileFromDownloadService.getFile());
//        file.setFile(new MockMultipartFile(file.getName() ,file.getName() , ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream));
//        return fileDomainMapper.convertEntityToModel(fileFoundOptional.get());
    }

    public List<File> getFilesFromBranch(Long idBranch){
        List<FileEntity> foundedFiles = fileRepository.findAllByBranchEntityId(idBranch);
        return foundedFiles.stream().map(fileDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    public List<File> getFilesByCommit(Commit commit){
        List<FileEntity> foundedFiles = fileRepository.findAllByCommitEntityCreation(commitDomainMapper.convertModelToEntity(commit, 0));
        return foundedFiles.stream().map(fileDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    public void deleteFileByCommit(Commit commit){
        List<FileEntity> foundedFiles = fileRepository.findAllByCommitEntityCreation(commitDomainMapper.convertModelToEntity(commit, 0 ));
        fileRepository.deleteAll(foundedFiles);
    }

    public File setCommitCreationToFile(Commit commit, Long fileId){
        Optional<FileEntity> fileEntityOptional = fileRepository.findById(fileId);
        if( fileEntityOptional.isEmpty()){
            //ERROR
            throw new RuntimeException("file not found");
        }
        FileEntity fileToUpdate = fileEntityOptional.get();
        fileToUpdate.setCommitCreation(commitDomainMapper.convertModelToEntity(commit,0));
        return fileDomainMapper.convertEntityToModel(fileRepository.save(fileToUpdate));
    }

    public File createFile(Long branchId, MultipartFile multipartFileToCreate) throws IOException, URISyntaxException, InterruptedException {

        Branch branch = branchService.getBranchById(branchId);

        File fileToCreate = new File();
        fileToCreate.setFile(multipartFileToCreate);
        fileToCreate.setLastCommitName("");
        fileToCreate.setBranch(branch);
        fileToCreate.setFromInit(false);
        fileToCreate.setUrl("none");
        fileToCreate.setName(multipartFileToCreate.getOriginalFilename());
        fileToCreate.setCommitCreation(null);
        fileToCreate.setDeleted(false);
        FileEntity test = fileDomainMapper.convertModelToEntity(fileToCreate);
        FileEntity fileCreatedEntity = fileRepository.save(fileDomainMapper.convertModelToEntity(fileToCreate));
        File fileCreated = fileDomainMapper.convertEntityToModel(fileCreatedEntity);
        fileToCreate.setId(fileCreated.getId());
        ModifiedFile modifiedFile = modifiedFileService.saveModifiedFiles(fileCreated, ModifiedFileTypeEnum.CREATED);
        storageService.saveFile(branchId, fileToCreate, "actual");
        return  fileCreated;
    }

    public File saveFile(Long branchId, Long fileId, MultipartFile multipartFileToSave) throws IOException, URISyntaxException, InterruptedException {
        Branch branch = branchService.getBranchById(branchId);
        FileEntity fileEntity = fileRepository.findByIdAndBranchEntity_Id(fileId, branchId);
        if(fileEntity == null){
            System.out.println("no file entity");
            throw new RuntimeException("File not found");
        }
        File fileToSave = fileDomainMapper.convertEntityToModel(fileEntity);
        fileToSave.setFile(multipartFileToSave);

//        FileEntity fileSavedEntity = fileRepository.save(fileDomainMapper.convertModelToEntity(fileToSave));

        ModifiedFile modifiedFile = modifiedFileService.saveModifiedFiles(fileToSave, ModifiedFileTypeEnum.MODIFIED);
        // save to server
        storageService.saveFile(branchId,fileToSave, "actual");
//        ModifiedFile modifiedFile
        return fileToSave;
    }

    public List<File> getAllFileModified(List<ModifiedFile> modifiedFiles){
        List<File> modifiedFilesFound = new ArrayList<>();

        for (ModifiedFile modifiedFile: modifiedFiles){
            Optional<FileEntity> modifiedFileFoundOptional = fileRepository.findById(modifiedFile.getFile().getId());
            modifiedFileFoundOptional.ifPresent(fileEntity -> modifiedFilesFound.add(fileDomainMapper.convertEntityToModel(fileEntity)));
        }
        return modifiedFilesFound;
    }

    private FileEntity getFileEntityById(Long fileId){
        Optional<FileEntity> fileEntityOptional = fileRepository.findById(fileId);
        if( fileEntityOptional.isEmpty()){
            throw new RuntimeException("azerty");
        }
        return fileEntityOptional.get();
    }

    public File getFileById(Long fileId){
        Optional<FileEntity> fileEntityOptional = fileRepository.findById(fileId);
        if( fileEntityOptional.isEmpty()){
            throw new RuntimeException("azerty");
        }
        return fileDomainMapper.convertEntityToModel(fileEntityOptional.get());
    }

    public File deleteFile(File file){
        FileEntity fileEntity = getFileEntityById(file.getId());
        fileEntity.setDeleted(true);
        return fileDomainMapper.convertEntityToModel(fileRepository.save(fileEntity));
    }

    public File restoreFile(File file){
        FileEntity fileEntity = getFileEntityById(file.getId());
        fileEntity.setDeleted(true);
        return fileDomainMapper.convertEntityToModel(fileRepository.save(fileEntity));
    }


}
