package fr.esgi.projectservice.controller;

import fr.esgi.projectservice.domain.model.Commit;
import fr.esgi.projectservice.domain.model.File;
import fr.esgi.projectservice.mapper.FileMapper;
import fr.esgi.projectservice.request.CreateFileRequest;
import fr.esgi.projectservice.request.CreateProjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


@RestController
@RequestMapping("/api/v1/project/branch/{idBranch}/file")
public class FileController {

    FileMapper fileMapper;

    @Autowired
    public FileController(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<File> createFile(@PathVariable("idBranch") Long branchId, @RequestBody CreateFileRequest request ) throws IOException, URISyntaxException, InterruptedException {
        return ResponseEntity.ok(fileMapper.createFile(branchId, request));
    }

    @PostMapping("/{fileId}/save")
    public void saveFile(@PathVariable("idBranch") Long idBranch, @PathVariable("fileId") Long fileId, @RequestParam("file") MultipartFile file) throws IOException, URISyntaxException, InterruptedException {
        fileMapper.saveFile(idBranch, fileId, file);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<File>> getAllFile(@PathVariable("idBranch") Long branchId) throws ChangeSetPersister.NotFoundException, IOException, URISyntaxException, InterruptedException {
        return ResponseEntity.ok(fileMapper.getAllFileFromBranch(branchId));
    }

    @GetMapping("/{fileId}/get")
    public ResponseEntity<File> getFile(@PathVariable("fileId") Long fileId) throws ChangeSetPersister.NotFoundException, IOException, URISyntaxException, InterruptedException {
        return ResponseEntity.ok(fileMapper.getFileById(fileId));
    }
    @GetMapping("/{fileId}/getFileData")
    public ResponseEntity<byte[]> getFile(@PathVariable("fileId") Long fileId, @RequestParam("type") String type) throws ChangeSetPersister.NotFoundException, IOException, URISyntaxException, InterruptedException {
        return ResponseEntity.ok(fileMapper.getFile(fileId, type));
    }

    @GetMapping("/{fileId}/getVersionsCommit")
    public ResponseEntity<List<Commit>> getFileVersionsCommit(@PathVariable("fileId") Long fileId){
        return ResponseEntity.ok(fileMapper.getFileVersionsCommit(fileId));
    }

    @GetMapping("{fileId}/getVersion/{commitId}")
    public ResponseEntity<byte[]> getFileVersion(@PathVariable("fileId") Long fileId, @PathVariable("commitId") Long commitId) throws IOException, URISyntaxException, InterruptedException {
        return ResponseEntity.ok(fileMapper.getFileVersion(fileId, commitId));
    }

    @DeleteMapping("/{fileId}/get")
    public ResponseEntity<File> deleteFile(@PathVariable("idBranch") Long idBranch, @PathVariable("fileId") Long fileId) {
        return ResponseEntity.ok(fileMapper.deleteFile(idBranch, fileId));
    }
}
