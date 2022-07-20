package fr.esgi.projectservice.mapper;

import fr.esgi.projectservice.domain.model.File;
import fr.esgi.projectservice.service.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

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

    public File createFile(Long branchId, MultipartFile fileToCreate) throws IOException, URISyntaxException, InterruptedException {
        return fileService.createFile(branchId, fileToCreate);
    }

    public File deleteFile(Long branchId, Long fileId) {
        return fileService.deleteFileById(branchId, fileId);
    }

    public byte[] getFile(Long fileId, String type) throws ChangeSetPersister.NotFoundException, IOException, URISyntaxException, InterruptedException {
        return fileService.getFileOnServerById(fileId, type);
    }
}
