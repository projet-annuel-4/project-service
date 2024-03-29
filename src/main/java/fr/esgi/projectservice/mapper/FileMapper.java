package fr.esgi.projectservice.mapper;

import fr.esgi.projectservice.domain.model.Commit;
import fr.esgi.projectservice.domain.model.File;
import fr.esgi.projectservice.request.CreateFileRequest;
import fr.esgi.projectservice.service.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileMapper {

    private final ModelMapper modelMapper;
    private final FileService fileService;

    @Autowired
    public FileMapper(ModelMapper modelMapper, FileService fileService) {
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }

    public File saveFile(Long idBranch, Long fileId, MultipartFile fileToSave) throws IOException, URISyntaxException, InterruptedException {
        return fileService.saveFile(idBranch, fileId, fileToSave);
    }

    public File createFile(Long branchId, CreateFileRequest request) throws IOException, URISyntaxException, InterruptedException {
        return fileService.createFile(branchId, request);
    }

    public File deleteFile(Long branchId, Long fileId) {
        return fileService.deleteFileById(branchId, fileId);
    }

    public byte[] getFile(Long fileId, String type) throws ChangeSetPersister.NotFoundException, IOException, URISyntaxException, InterruptedException {
        return fileService.getFileOnServerById(fileId, type);
    }

    public List<File> getAllFileFromBranch(Long branchId){
        List<File> files =  fileService.getFilesFromBranch(branchId);
        List<File> files1 = new ArrayList<>();
        for (File file: files){
            if( !file.isDeleted() ){
                files1.add(file);
            }
        }
        return files1;
    }

    public File getFileById(Long fileId){
        return fileService.getFileById(fileId);
    }

    public List<Commit> getFileVersionsCommit(Long fileId){
        return fileService.getFileVersionsCommit(fileId);
    }

    public byte[] getFileVersion(Long fileId, Long commitId) throws IOException, URISyntaxException, InterruptedException {
        return fileService.getFileVersion(fileId, commitId);
    }


}
