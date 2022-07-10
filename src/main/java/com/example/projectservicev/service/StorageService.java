package com.example.projectservicev.service;

import com.example.projectservicev.domain.model.Commit;
import com.example.projectservicev.domain.model.Delta;
import com.example.projectservicev.domain.model.File;
import com.example.projectservicev.dto.UploadFileToServerRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
import java.util.ArrayList;
import java.util.List;

@Service
public class StorageService {

//    private final String downloadEndpoint = "api/v1/files/";
//    private final String uploadEndpoint =

    @Value("${target.api}")
    private String uploadEndpoint;



    public void getActualFilesFromBranch(Long idBranch){

    }

    public void getLastCommitFilesFromBranch(Long idBranch){

    }

    public void saveFile(Long idBranch, File file ){

    }

    public void uploadFiles(Long idBranch, List<File> files) throws IOException, URISyntaxException, InterruptedException {
        System.out.println("storage");

        List<UploadFileToServerRequest> requests = buildRequests(idBranch, files, "actual");
        System.out.println(requests.size() + " ///// " + files.size());
        for( int i = 0; i < files.size(); i++){
            System.out.println("icici 2");
            java.io.File tmpFile = new java.io.File("/tmpFilesConvert/" + files.get(i).getId().toString());
            files.get(i).getFile().transferTo(tmpFile);
            System.out.println(files.get(i).getFile().getSize() + " ------ \n");
            uploadFile(requests.get(i), tmpFile);
            tmpFile.delete();
        }

    }

    private List<UploadFileToServerRequest> buildRequests(Long idBranch, List<File> files, String type){
        List<UploadFileToServerRequest> fileRequests = new ArrayList<>();
        for(File file : files){
            UploadFileToServerRequest request = new UploadFileToServerRequest();
            request.setId(file.getId());
            request.setDirecoryId(idBranch);
            request.setType(type);
            request.setLink("src");
            request.setTitle(file.getFile().getOriginalFilename());
            fileRequests.add(request);
        }

        return fileRequests;
    }

    public void deleteFile(File file){

    }

    public void deleteDeltas(List<Delta> deltaList){

    }

    public void getDeltasFromCommit(Commit commit){

    }

    private String uploadFile(UploadFileToServerRequest request, java.io.File file) throws IOException, URISyntaxException, InterruptedException {

        System.out.println("azerty");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        System.out.println(request.toString());

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
        System.out.println(uploadEndpoint);
        HttpRequest request2 = HttpRequest.newBuilder(new URI( "http://" + uploadEndpoint + "/api/v1/files"))
                // The Content-Type header is important, don't forget to set it.
                .header("Content-Type", httpEntity.getContentType().getValue())
                // Reads data from a pipeline stream.
                .POST(java.net.http.HttpRequest.BodyPublishers.ofInputStream(() -> Channels.newInputStream(pipe.source()))).build();

        System.out.println("/azerty/");
        HttpResponse<String> responseBody = httpClient.send(request2, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        System.out.println("azerty");
        return  responseBody.body();
    }

}
