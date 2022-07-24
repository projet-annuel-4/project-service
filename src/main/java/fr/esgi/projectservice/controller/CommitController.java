package fr.esgi.projectservice.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.esgi.projectservice.domain.model.Commit;
import fr.esgi.projectservice.dto.UploadFileToServerRequest;
import fr.esgi.projectservice.mapper.CommitMapper;
import fr.esgi.projectservice.proxies.FilesManagementServiceProxy;
import fr.esgi.projectservice.request.CreateCommitRequest;
import fr.esgi.projectservice.service.DeltaService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;
import java.nio.charset.StandardCharsets;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/project/branch/{idBranch}/commit")
public class CommitController {

    private final CommitMapper commitMapper;
    private final DeltaService deltaService;
    private final FilesManagementServiceProxy filesManagementServiceProxy;


    @Autowired
    public CommitController(CommitMapper commitMapper, DeltaService deltaService, FilesManagementServiceProxy filesManagementServiceProxy) {
        //  delta service for test
        this.commitMapper = commitMapper;
        this.deltaService = deltaService;
        this.filesManagementServiceProxy = filesManagementServiceProxy;
    }

    @PostMapping("")
    public void createCommit(@RequestBody CreateCommitRequest request,
                             @PathVariable("idBranch") Long idBranch) throws IOException, URISyntaxException, InterruptedException {
        commitMapper.createCommit(idBranch, request);
    }

    @GetMapping("/getAllCommit")
    public List<Commit> getAllCommit(@PathVariable("idBranch") Long idBranch) {
        return commitMapper.getAllCommitFromBranchId(idBranch);
    }

    @PostMapping("/{idCommit}/delete")
    public void deleteCommit(@PathVariable("id") Long idCommit) {
        commitMapper.deleteCommit(idCommit);
    }

    @PostMapping("/{commitId}/revert")
    public void revertCommit(@PathVariable("idBranch") Long branchId, @PathVariable("commitId") Long commitId) throws IOException, URISyntaxException, InterruptedException {
        commitMapper.revertCommit(branchId, commitId);
    }

    @PostMapping("/test")
    public void test() throws IOException, InterruptedException, URISyntaxException {
        UploadFileToServerRequest request = new UploadFileToServerRequest();
        request.setTitle("coucou");
        request.setType("actual");
        request.setId(5556);
        request.setDirecoryId(150L);
        File file = new File("C:\\Users\\LILOKE\\Desktop\\lien_git.txt");

        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        //MultipartFile multipartFile = new MockMultipartFile(request.getTitle(),  new FileInputStream(new File("C:\\Users\\LILOKE\\Desktop\\lien_git.txt")));

//        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
//        ByteArrayResource contentsAsResource = new ByteArrayResource(multipartFile.getBytes()) {
//            @Override
//            public String getFilename() {
//                return multipartFile.getOriginalFilename();
//            }
//        };
//        multiValueMap.add("file", contentsAsResource);
//        multiValueMap.add("fileType", multipartFile.getContentType());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Java objects to String
        String jsonRequest = gson.toJson(request);


        HttpEntity httpEntity = (HttpEntity) MultipartEntityBuilder.create()
                // JSON
                .addPart("details",
                        new StringBody(jsonRequest, ContentType.APPLICATION_JSON))
                // FILE
                .addBinaryBody("file", file)
                .build();

        /**
         * Use pipeline streams to write the encoded data directly to the network
         * instead of caching it in memory. Because Multipart request bodies contain
         * files, they can cause memory overflows if cached in memory.
         */
        Pipe pipe = Pipe.open();

        // Pipeline streams must be used in a multi-threaded environment. Using one
        // thread for simultaneous reads and writes can lead to deadlocks.
        new Thread(() -> {
            try (OutputStream outputStream = Channels.newOutputStream(pipe.sink())) {
                // Write the encoded data to the pipeline.
                httpEntity.writeTo(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request2 = HttpRequest.newBuilder(new URI("http://localhost:9140/api/v1/files"))
                // The Content-Type header is important, don't forget to set it.
                .header("Content-Type", httpEntity.getContentType().getValue())
                // Reads data from a pipeline stream.
                .POST(java.net.http.HttpRequest.BodyPublishers.ofInputStream(() -> Channels.newInputStream(pipe.source()))).build();

        HttpResponse<String> responseBody = httpClient.send(request2, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        //filesManagementServiceProxy.upload(multipartFile, request2);


    }
}
