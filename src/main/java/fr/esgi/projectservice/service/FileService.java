package fr.esgi.projectservice.service;

import fr.esgi.projectservice.data.entity.FileEntity;
import fr.esgi.projectservice.data.repository.FileRepository;
import fr.esgi.projectservice.domain.mapper.CommitDomainMapper;
import fr.esgi.projectservice.domain.mapper.FileDomainMapper;
import fr.esgi.projectservice.domain.model.*;
import fr.esgi.projectservice.exception.NameAlreadyTakenException;
import fr.esgi.projectservice.exception.NameMustBeNotNull;
import fr.esgi.projectservice.request.CreateFileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final CommitService commitService;
    private final DeltaService deltaService;

    @Autowired
    public FileService(FileRepository fileRepository, FileDomainMapper fileDomainMapper, CommitDomainMapper commitDomainMapper, ModifiedFileService modifiedFileService, @Lazy BranchService branchService, StorageService storageService, CommitService commitService, DeltaService deltaService) {
        this.fileRepository = fileRepository;
        this.fileDomainMapper = fileDomainMapper;
        this.commitDomainMapper = commitDomainMapper;
        this.modifiedFileService = modifiedFileService;
        this.branchService = branchService;
        this.storageService = storageService;
        this.commitService = commitService;
        this.deltaService = deltaService;
    }

    public void addFileToBranch(Long idBranch, File fileToAdd) {

        fileToAdd.setFromInit(false);
        fileToAdd.setCommitCreation(null);
        fileToAdd.setLastCommitName(null);
        fileRepository.save(fileDomainMapper.convertModelToEntity(fileToAdd));
    }

    public List<File> addFilesToBranch(List<File> filesToAdd) {
        List<FileEntity> fileEntities = fileRepository.saveAll(filesToAdd.stream().map(fileDomainMapper::convertModelToEntity).collect(Collectors.toList()));
        return fileEntities.stream().map(fileDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    @Transactional
    public File deleteFileById(Long idBranch, Long fileId) {
        FileEntity fileEntity = fileRepository.findByIdAndBranchEntity_Id(fileId, idBranch);
        if (fileEntity == null) {
            //ERROR
        }
        fileEntity.setDeleted(true);
        File fileDeleted = fileDomainMapper.convertEntityToModel(fileRepository.save(fileEntity));
        modifiedFileService.saveModifiedFiles(fileDeleted, ModifiedFileTypeEnum.DELETED);

        return fileDomainMapper.convertEntityToModel(fileRepository.save(fileEntity));
    }

    public byte[] getFileOnServerById(Long idFile, String type) throws ChangeSetPersister.NotFoundException, IOException, URISyntaxException, InterruptedException {
        Optional<FileEntity> fileFoundOptional = fileRepository.findById(idFile);
        if (fileFoundOptional.isEmpty()) {
            throw new ChangeSetPersister.NotFoundException();// throw error;
        }
        File file = fileDomainMapper.convertEntityToModel(fileFoundOptional.get());
        return storageService.getFileOnServerTest(file.getId(), type);
//        InputStream inputStream = new ByteArrayInputStream(fileFromDownloadService.getFile());
//        file.setFile(new MockMultipartFile(file.getName() ,file.getName() , ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream));
//        return fileDomainMapper.convertEntityToModel(fileFoundOptional.get());
    }

    public List<File> getFilesFromBranch(Long idBranch) {
        List<FileEntity> foundedFiles = fileRepository.findAllByBranchEntityId(idBranch);
        return foundedFiles.stream().map(fileDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    public List<File> getFilesByCommit(Commit commit) {
        List<FileEntity> foundedFiles = fileRepository.findAllByCommitEntityCreation(commitDomainMapper.convertModelToEntity(commit, 0));
        return foundedFiles.stream().map(fileDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    public void deleteFileByCommit(Commit commit) {
        List<FileEntity> foundedFiles = fileRepository.findAllByCommitEntityCreation(commitDomainMapper.convertModelToEntity(commit, 0));
        fileRepository.deleteAll(foundedFiles);
    }

    public File setCommitCreationToFile(Commit commit, Long fileId) {
        Optional<FileEntity> fileEntityOptional = fileRepository.findById(fileId);
        if (fileEntityOptional.isEmpty()) {
            //ERROR
            throw new RuntimeException("file not found");
        }
        FileEntity fileToUpdate = fileEntityOptional.get();
        fileToUpdate.setCommitCreation(commitDomainMapper.convertModelToEntity(commit, 0));
        fileToUpdate.setLastCommitName(commit.getName());
        return fileDomainMapper.convertEntityToModel(fileRepository.save(fileToUpdate));
    }

    public File setLastCommitNameToFile(Commit commit, Long fileId) {
        Optional<FileEntity> fileEntityOptional = fileRepository.findById(fileId);
        if (fileEntityOptional.isEmpty()) {
            //ERROR
            throw new RuntimeException("file not found");
        }
        FileEntity fileToUpdate = fileEntityOptional.get();
        fileToUpdate.setLastCommitName(commit.getName());
        return fileDomainMapper.convertEntityToModel(fileRepository.save(fileToUpdate));
    }

    public MultipartFile generateFile(CreateFileRequest request){
        java.io.File file = new java.io.File("/generateFile/"+request.getName());
        Path path = Paths.get("/generateFile/"+request.getName());
        String name = request.getName();
        String originalFileName = request.getName();
        String contentType = "text/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        MultipartFile result = new MockMultipartFile(name,
                originalFileName, contentType, content);

        return result;
    }

    public File createFile(Long branchId, CreateFileRequest request) throws IOException, URISyntaxException, InterruptedException {
        if(Objects.equals(request.getName(), "") || request.getName() == null){
            throw new NameMustBeNotNull("Vous devez donner un nom de fichier");
        }
        Branch branch = branchService.getBranchById(branchId);
        Optional<FileEntity> searchFile = fileRepository.findByBranchEntity_IdAndNameEquals(branchId, request.getName());
        if( searchFile.isPresent()){
            throw new NameAlreadyTakenException("Ce nom de fichier est déjà pris");
        }
        MultipartFile fileGenerated = generateFile(request);
        File fileToCreate = new File();
        fileToCreate.setFile(fileGenerated);
        fileToCreate.setLastCommitName("");
        fileToCreate.setBranch(branch);
        fileToCreate.setFromInit(false);
        fileToCreate.setUrl("none");
        fileToCreate.setName(request.getName());
        fileToCreate.setCommitCreation(null);
        fileToCreate.setDeleted(false);
        FileEntity test = fileDomainMapper.convertModelToEntity(fileToCreate);
        FileEntity fileCreatedEntity = fileRepository.save(fileDomainMapper.convertModelToEntity(fileToCreate));
        File fileCreated = fileDomainMapper.convertEntityToModel(fileCreatedEntity);
        fileToCreate.setId(fileCreated.getId());
        ModifiedFile modifiedFile = modifiedFileService.saveModifiedFiles(fileCreated, ModifiedFileTypeEnum.CREATED);
        storageService.saveFile(branchId, fileToCreate, "actual");
        return fileCreated;
    }

    public File saveFile(Long branchId, Long fileId, MultipartFile multipartFileToSave) throws IOException, URISyntaxException, InterruptedException {
        Branch branch = branchService.getBranchById(branchId);
        FileEntity fileEntity = fileRepository.findByIdAndBranchEntity_Id(fileId, branchId);
        if (fileEntity == null) {
            System.out.println("no file entity");
            throw new RuntimeException("File not found");
        }
        File fileToSave = fileDomainMapper.convertEntityToModel(fileEntity);
        fileToSave.setFile(multipartFileToSave);

//        FileEntity fileSavedEntity = fileRepository.save(fileDomainMapper.convertModelToEntity(fileToSave));

        ModifiedFile modifiedFile = modifiedFileService.saveModifiedFiles(fileToSave, ModifiedFileTypeEnum.MODIFIED);
        // save to server
        storageService.saveFile(branchId, fileToSave, "actual");
//        ModifiedFile modifiedFile
        return fileToSave;
    }

    public List<File> getAllFileModified(List<ModifiedFile> modifiedFiles) {
        List<File> modifiedFilesFound = new ArrayList<>();

        for (ModifiedFile modifiedFile : modifiedFiles) {
            Optional<FileEntity> modifiedFileFoundOptional = fileRepository.findById(modifiedFile.getFile().getId());
            modifiedFileFoundOptional.ifPresent(fileEntity -> modifiedFilesFound.add(fileDomainMapper.convertEntityToModel(fileEntity)));
        }
        return modifiedFilesFound;
    }

    private FileEntity getFileEntityById(Long fileId) {
        Optional<FileEntity> fileEntityOptional = fileRepository.findById(fileId);
        if (fileEntityOptional.isEmpty()) {
            throw new RuntimeException("azerty");
        }
        return fileEntityOptional.get();
    }

    public File getFileById(Long fileId) {
        Optional<FileEntity> fileEntityOptional = fileRepository.findById(fileId);
        if (fileEntityOptional.isEmpty()) {
            throw new RuntimeException("azerty");
        }
        return fileDomainMapper.convertEntityToModel(fileEntityOptional.get());
    }

    public File deleteFile(File file) {
        FileEntity fileEntity = getFileEntityById(file.getId());
        fileEntity.setDeleted(true);
        return fileDomainMapper.convertEntityToModel(fileRepository.save(fileEntity));
    }

    public File restoreFile(File file) {
        FileEntity fileEntity = getFileEntityById(file.getId());
        fileEntity.setDeleted(true);
        return fileDomainMapper.convertEntityToModel(fileRepository.save(fileEntity));
    }

    public List<Commit> getFileVersionsCommit(Long fileId){
        File file = getFileById(fileId);
        List<ModifiedFile> modifiedFiles = modifiedFileService.getAllModifiedFilesByFileId(fileId);
        List<Commit> commits = new ArrayList<>();
        for (ModifiedFile modifiedFile : modifiedFiles){
            if(!Objects.equals(modifiedFile.getAttachedCommit().getId(), file.getCommitCreation().getId())){
                commits.add(modifiedFile.getAttachedCommit());
            }
        }
        return commits;
    }

    public byte[] getFileVersion(Long fileId, Long commitId) throws IOException, URISyntaxException, InterruptedException {
        File file = getFileById(fileId);
        List<Commit> fileLinkedCommit = getFileVersionsCommit(fileId);
        List<Commit> commitChild = commitService.getAllChild(commitId);
        List<Commit> commitToRevert = new ArrayList<>();
        for (Commit child : commitChild){
            for (Commit linkedCommit : fileLinkedCommit){
                if(Objects.equals(child.getId(), linkedCommit.getId())){
                    commitToRevert.add(child);
                }
            }
        }
        return processRevertFile(file, commitToRevert);
    }

    public byte[] processRevertFile(File file, List<Commit> commits) throws IOException, URISyntaxException, InterruptedException {
        System.out.println("processRevertFile");
        java.io.File fileReverted = deltaService.revertDeltaForDiff(file.getBranch().getId(), commits, file);

        return Files.readAllBytes(Paths.get(fileReverted.getAbsolutePath()));

    }


}
