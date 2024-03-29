package fr.esgi.projectservice.service;

import fr.esgi.projectservice.data.entity.DeltaEntity;
import fr.esgi.projectservice.data.repository.DeltaRepository;
import fr.esgi.projectservice.domain.mapper.CommitDomainMapper;
import fr.esgi.projectservice.domain.mapper.DeltaDomainMapper;
import fr.esgi.projectservice.domain.model.Commit;
import fr.esgi.projectservice.domain.model.Delta;
import fr.esgi.projectservice.domain.model.File;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeltaService {

    private final DeltaRepository deltaRepository;
    private final DeltaDomainMapper deltaDomainMapper;
    private final CommitDomainMapper commitDomainMapper;
    private final StorageService storageService;

    public DeltaService(DeltaRepository deltaRepository, DeltaDomainMapper deltaDomainMapper, CommitDomainMapper commitDomainMapper, StorageService storageService) {
        this.deltaRepository = deltaRepository;
        this.deltaDomainMapper = deltaDomainMapper;
        this.commitDomainMapper = commitDomainMapper;
        this.storageService = storageService;
    }

    public void createDelta(Long idBranch, Commit commit, File fileToCommit) throws IOException, URISyntaxException, InterruptedException {
        // recup les fichiers ici et executé les cmdd
        //generatePatch(idBranch, fileToCommit.getId(), commit.getId());
        Delta deltaToSave = new Delta();
        String directoryPath = "/tmpDelta/" + idBranch.toString() + "/";
        java.io.File patch = generatePatch(directoryPath, fileToCommit.getId(), commit.getId());
        deltaToSave.setPatchUrl("null");
        deltaToSave.setFileSrc(fileToCommit);
        deltaToSave.setFormCommit(commit);
        DeltaEntity deltaSaved = deltaRepository.save(deltaDomainMapper.convertModelToEntity(deltaToSave));
        storageService.saveDelta(idBranch, deltaDomainMapper.convertEntityToModel(deltaSaved), patch);
        // get patch file and send it to server with delta id + delete directory
    }

    public Delta getDeltaByFileIdAndCommitId(Long fileId, Long commitId) {
        return deltaDomainMapper.convertEntityToModel(deltaRepository.getByFileSrc_IdAndAndFormCommitEntity_Id(fileId, commitId));
    }

    public void revertDelta(Long idBranch, Commit commit, File fileToRevert) throws IOException, URISyntaxException, InterruptedException {

        Delta deltaToRevert = getDeltaByFileIdAndCommitId(fileToRevert.getId(), commit.getId());
        System.out.println("delta : " + deltaToRevert);
        String directoryPath = "/tmpDelta/" + idBranch.toString() + "/";
        java.io.File tmpDir = new java.io.File(directoryPath);
        if (!tmpDir.mkdir()) {
            throw new RuntimeException("unable to create dir");
        }
        java.io.File fileReverted = revertPatch(directoryPath, fileToRevert.getId(), deltaToRevert.getId());

        deltaRepository.delete(deltaDomainMapper.convertModelToEntity(deltaToRevert));
        storageService.saveFileReverted(fileToRevert.getBranch().getId(), fileToRevert, fileReverted);
        tmpDir.delete();
        // get patch file and send it to server with delta id + delete directory
    }

    public byte[] revertDeltaForDiff(Long idBranch, List<Commit> commits, File fileToRevert) throws IOException, URISyntaxException, InterruptedException {

        System.out.println("REVERTDELTAFORDIFF");
        String directoryPath = "/showDiff/" + idBranch.toString() + "/";
        java.io.File tmpDir = new java.io.File(directoryPath);
        if (!tmpDir.mkdir()) {
            throw new RuntimeException("unable to create dir");
        }

        setFileToShowDiffDir(directoryPath, fileToRevert.getId());
        java.io.File fileReverted = null;
        System.out.println(" ///// SIZE //// : " + commits.size());
        for (Commit commitToRevertForDiff : commits){
            System.out.println("je commence avec le commit  : " + commitToRevertForDiff.getId());
            Delta deltaToRevert = getDeltaByFileIdAndCommitId(fileToRevert.getId(), commitToRevertForDiff.getId());


            fileReverted = revertPatchForDiff(directoryPath, fileToRevert.getId(), deltaToRevert.getId());

        }
        byte[] fileRevertedToByte = Files.readAllBytes(Paths.get(fileReverted.getAbsolutePath()));
        System.out.println("j'ai supp le fichier : " + fileReverted.delete());
        System.out.println("j'ai supp le dir : " + tmpDir.delete());

        return fileRevertedToByte;
    }

    public void setupFilesToProcessDirectoryRevert(String directoryPath, Long fileToCommitId, Long patchToRevertId) throws IOException, URISyntaxException, InterruptedException {
        // create paths
        byte[] actualFile = storageService.getFileOnServerTest(fileToCommitId, "actual");
        byte[] patchFile = storageService.getFileOnServerTest(patchToRevertId, "patch");
        System.out.println("size actual : " + actualFile.length);
        System.out.println("size patchFile : " + patchFile.length);
        java.io.File tmpActualFile = new java.io.File(directoryPath + "actual_" + fileToCommitId.toString() + ".txt");
        java.io.File tmpPatchFile = new java.io.File(directoryPath + "patch_" + patchToRevertId.toString() + ".patch");


        try (
                OutputStream actualFileCreated = new FileOutputStream(tmpActualFile);
                OutputStream patchFileCreated = new FileOutputStream(tmpPatchFile);
        ) {
            actualFileCreated.write(actualFile);
            actualFileCreated.close();
            patchFileCreated.write(patchFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setFileToShowDiffDir(String directoryPath, Long fileToCommitId) throws IOException, URISyntaxException, InterruptedException {
        byte[] actualFile = storageService.getFileOnServerTest(fileToCommitId, "actual");

        java.io.File tmpActualFile = new java.io.File(directoryPath + "diff_" + fileToCommitId.toString() + ".txt");

        try (
                OutputStream actualFileCreated = new FileOutputStream(tmpActualFile);
        ) {
            actualFileCreated.write(actualFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setDeltaToShowDiffDir(String directoryPath, Long patchToRevertId) throws IOException, URISyntaxException, InterruptedException {
        byte[] patchFile = storageService.getFileOnServerTest(patchToRevertId, "patch");
        System.out.println("size patchFile : " + patchFile.length);
        java.io.File tmpPatchFile = new java.io.File(directoryPath + "patch_" + patchToRevertId.toString() + ".patch");


        try (
                OutputStream patchFileCreated = new FileOutputStream(tmpPatchFile);
        ) {
            patchFileCreated.write(patchFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public java.io.File revertPatch(String directoryPath, Long fileToCommitId, Long deltaId) throws IOException, URISyntaxException, InterruptedException {
        // get last_commit file and actual file

        setupFilesToProcessDirectoryRevert(directoryPath, fileToCommitId, deltaId);
        //TODO add extension in file to build file here
        System.out.println("//// PROCESS RESULT ////");
        try {
            Process process1 = Runtime.getRuntime()
                    .exec("dos2unix " + directoryPath + "actual_" + fileToCommitId + ".txt ");
            Process process = Runtime.getRuntime()
                    .exec("patch -R " + directoryPath + "actual_" + fileToCommitId + ".txt " + directoryPath + "patch_" + deltaId + ".patch");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(" UNABLE TO PATCH FILE ");
        }
        System.out.println("//// PROCESS END ////");
        java.io.File actualFile = new java.io.File(directoryPath + "actual_" + fileToCommitId + ".txt");
        java.io.File patchFile = new java.io.File(directoryPath + "patch_" + deltaId + ".patch");
        //TODO delete patch on server
        patchFile.delete();

        return actualFile;
    }

    public java.io.File revertPatchForDiff(String directoryPath, Long fileToCommitId, Long deltaId) throws IOException, URISyntaxException, InterruptedException {
        // get last_commit file and actual file
        System.out.println("revertPatchForDiff");

        setDeltaToShowDiffDir(directoryPath, deltaId);
        //TODO add extension in file to build file here
        System.out.println("//// PROCESS  DIFF RESULT ////");
        try {
            Process process1 = Runtime.getRuntime()
                    .exec("dos2unix " + directoryPath + "diff_" + fileToCommitId + ".txt ");
            Process process = Runtime.getRuntime()
                    .exec("patch -R " + directoryPath + "diff_" + fileToCommitId + ".txt " + directoryPath + "patch_" + deltaId + ".patch");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(" UNABLE TO PATCH FILE ");
        }
        System.out.println("//// PROCESS DIFF END ////");
        java.io.File actualFile = new java.io.File(directoryPath + "diff_" + fileToCommitId + ".txt");
        java.io.File patchFile = new java.io.File(directoryPath + "patch_" + deltaId + ".patch");
        //TODO delete patch on server
        patchFile.delete();

        return actualFile;
    }

    public java.io.File generatePatch(String directoryPath, Long fileToCommitId, Long idCommit) throws IOException, URISyntaxException, InterruptedException {
        // get last_commit file and actual file

        setupFilesToProcessDirectory(directoryPath, fileToCommitId);
        //TODO add extension in file to build file here
        String copy = "";
        try {
            Process process = Runtime.getRuntime()
                    .exec("diff " + directoryPath + "last_" + fileToCommitId + ".txt " + directoryPath + "actual_" + fileToCommitId + ".txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";

            while ((line = reader.readLine()) != null) {
                copy += line + "\n";
            }
        } catch (Exception e) {

        }

        java.io.File createdPatch = createPatchFile(directoryPath, copy);
        java.io.File lastFile = new java.io.File(directoryPath + "last_" + fileToCommitId + ".txt");
        java.io.File actualFile = new java.io.File(directoryPath + "actual_" + fileToCommitId + ".txt");
        lastFile.delete();
        actualFile.delete();
//        String patchPathServer = idCommit.toString() + "-" + fileToCommitId.toString();
        //save on the server return url of stocked path
        return createdPatch;
    }

    private java.io.File createPatchFile(String directoryPath, String copy) throws IOException {
        String filePath = directoryPath + "/patch.patch";
        FileWriter myWriterp = new FileWriter(directoryPath + "/patch.patch");
        myWriterp.write(copy);
        myWriterp.close();
        return new java.io.File(filePath);
    }

    public void setupFilesToProcessDirectory(String directoryPath, Long fileToCommitId) throws IOException, URISyntaxException, InterruptedException {
        // create paths
        byte[] actualFile = storageService.getFileOnServerTest(fileToCommitId, "actual");
        byte[] lastCommitFile = storageService.getFileOnServerTest(fileToCommitId, "last");
        java.io.File tmpActualFile = new java.io.File(directoryPath + "actual_" + fileToCommitId.toString() + ".txt");
        java.io.File tmpLastCommitFile = new java.io.File(directoryPath + "last_" + fileToCommitId.toString() + ".txt");


        try (
                OutputStream actualFileCreated = new FileOutputStream(tmpActualFile);
                OutputStream lastCommitFileCreated = new FileOutputStream(tmpLastCommitFile);
        ) {
            actualFileCreated.write(actualFile);
            actualFileCreated.close();
            lastCommitFileCreated.write(lastCommitFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Delta getDeltaById(Long id) {
        Optional<DeltaEntity> deltaFoundOptional = deltaRepository.findById(id);
        if (deltaFoundOptional.isEmpty()) {
            throw new RuntimeException();
        }
        return deltaDomainMapper.convertEntityToModel(deltaFoundOptional.get());
    }

    public List<Delta> getDeltaByCommit(Commit commit) {
        List<DeltaEntity> deltaEntities = deltaRepository.findAllByFormCommitEntity(commitDomainMapper.convertModelToEntity(commit, 0));
        return deltaEntities.stream().map(deltaDomainMapper::convertEntityToModel).collect(Collectors.toList());
    }

    public void deleteDeltaByCommit(Commit commit) {
        // delete on s3
        deltaRepository.deleteAllByFormCommitEntity(commitDomainMapper.convertModelToEntity(commit, 0));
    }
}
