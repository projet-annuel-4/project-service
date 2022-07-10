package com.example.projectservicev.service;

import com.example.projectservicev.data.entity.DeltaEntity;
import com.example.projectservicev.data.repository.DeltaRepository;
import com.example.projectservicev.domain.mapper.CommitDomainMapper;
import com.example.projectservicev.domain.mapper.DeltaDomainMapper;
import com.example.projectservicev.domain.model.Commit;
import com.example.projectservicev.domain.model.Delta;
import com.example.projectservicev.domain.model.File;
import com.example.projectservicev.dto.FileFromDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeltaService {

    private final DeltaRepository deltaRepository;
    private final DeltaDomainMapper deltaDomainMapper;
    private final CommitDomainMapper commitDomainMapper;

    @Autowired
    public DeltaService(DeltaRepository deltaRepository, DeltaDomainMapper deltaDomainMapper, CommitDomainMapper commitDomainMapper) {
        this.deltaRepository = deltaRepository;
        this.deltaDomainMapper = deltaDomainMapper;
        this.commitDomainMapper = commitDomainMapper;
    }

    public void createDelta(Long idBranch, Commit commit, File fileToCommit) throws IOException {
        // recup les fichiers ici et executé les cmdd
        //generatePatch(idBranch, fileToCommit.getId(), commit.getId());
        Delta deltaToSave = new Delta();
        deltaToSave.setPatchUrl(generatePatch(idBranch, fileToCommit.getId(), commit.getId()));
        deltaToSave.setFileSrc(fileToCommit);
        deltaToSave.setFormCommit(commit);
        deltaRepository.save(deltaDomainMapper.convertModelToEntity(deltaToSave));
    }

    public String generatePatch(Long idBranch, Long idFileToCommit, Long idCommit) throws IOException {
        // get last_commit file and actual file
        String directoryPath =  idBranch.toString();
        //setupFilesToProcessDirectory(directoryPath, idFileToCommit);

        ProcessBuilder processBuilder = new ProcessBuilder();
        //processBuilder.directory(new java.io.File(directoryPath));
        String command = "diff /123/last_commit_" +idFileToCommit+ ".txt /123/actual_" +idFileToCommit+ ".txt > /123/patch.patch";
        //String command = "ls";
        processBuilder.command(command);

        java.io.File f = new java.io.File("/123");
        f.mkdir();
        java.io.File fl = new java.io.File("/123/last_commit_1.txt");
        java.io.File fa = new java.io.File("/123/actual_1.txt");
        fl.createNewFile();
        fa.createNewFile();
        java.io.File fp = new java.io.File("/123/patch.patch");
        fp.createNewFile();
        FileWriter myWriter = new FileWriter("/123/last_commit_1.txt");
        myWriter.write("test 123");
        myWriter.close();
        FileWriter myWriter2 = new FileWriter("/123/actual_1.txt");
        myWriter2.write("test 1234586");
        myWriter2.close();
        //diff -u /123/last_commit_1.txt /123/actual_1.txt > /123/patch.patch
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String[] command1 = new String[]{"diff","/123/last_commit_1.txt","/123/actual_1.txt",">","/123/patch.patch"};
        String copy = "";
        try {
            System.out.println("zeafzerfgjzçieojfoizejfiojzefio");
            Process process = Runtime.getRuntime()
                    .exec("diff /123/last_commit_1.txt /123/actual_1.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                copy += line +"\n";
            }
        } catch (Exception e){

        }

        java.io.File createdPatch = createPatchFile("/123", copy);
        String patchPathServer = idCommit.toString() + "-" + idFileToCommit.toString();
        //save on the server return url of stocked path
        return patchPathServer;
    }

    private java.io.File createPatchFile(String directoryPath, String copy) throws IOException {
        String filePath = directoryPath + "/patch.patch";
        FileWriter myWriterp = new FileWriter(directoryPath + "/patch.patch");
        myWriterp.write(copy);
        myWriterp.close();
        return new java.io.File(filePath);
    }

    private void setupFilesToProcessDirectory(String directoryPath, Long idFileToCommit){
        // create paths
        FileFromDownloadService lastCommitFile = new FileFromDownloadService();// call the download service
        FileFromDownloadService actualFile = new FileFromDownloadService();// call the download service

        try (
             FileOutputStream lastCommitFileCreated = new FileOutputStream(directoryPath+"last_commit_"+idFileToCommit.toString());
             FileOutputStream actualFileCreated = new FileOutputStream(directoryPath+"actual_"+idFileToCommit.toString());
        ){
            lastCommitFileCreated.write(lastCommitFile.getFile());
            actualFileCreated.write(actualFile.getFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Delta getDeltaById(Long id){
        Optional<DeltaEntity> deltaFoundOptional = deltaRepository.findById(id);
        if( deltaFoundOptional.isEmpty() ){
            throw new RuntimeException();
        }
        return deltaDomainMapper.convertEntityToModel(deltaFoundOptional.get());
    }

    public List<Delta> getDeltaByCommit(Commit commit){
        List<DeltaEntity> deltaEntities = deltaRepository.findAllByFormCommitEntity(commitDomainMapper.convertModelToEntity(commit));
        return deltaEntities.stream().map(deltaDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    public void deleteDeltaByCommit(Commit commit){
        // delete on s3
        deltaRepository.deleteAllByFormCommitEntity(commitDomainMapper.convertModelToEntity(commit));
    }
}
