package fr.esgi.projectservice.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.esgi.projectservice.domain.model.Commit;
import fr.esgi.projectservice.domain.model.Delta;
import fr.esgi.projectservice.dto.FileFromDownloadService;
import fr.esgi.projectservice.dto.UploadFileToServerRequest;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
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

    public void copyFileFromDirectoryToDirectory(fr.esgi.projectservice.domain.model.File file, String directorySrc, String directoryTarget) throws IOException, URISyntaxException, InterruptedException {
        byte[] fileFromDirectorySrc = getFileOnServerTest(file.getId(), directorySrc);
        String directoryPath = "/tmpFilesConvert/" + file.getBranch().getId() + "/";
        java.io.File tmpDir = new java.io.File(directoryPath);
        if (!tmpDir.mkdir()) {
            throw new RuntimeException("unable to create dir");
        }
        java.io.File tmpFile = new java.io.File(directoryPath + file.getId().toString());

        try {
            OutputStream fileFromDirectorySrcCreated = new FileOutputStream(tmpFile);
            // Starting writing the bytes in it
            fileFromDirectorySrcCreated.write(fileFromDirectorySrc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        UploadFileToServerRequest request = buildRequestForFileObject(file.getBranch().getId(), file, directoryTarget);

        uploadFile(request, tmpFile);
        tmpFile.delete();
        tmpDir.delete();
    }

    public FileFromDownloadService getFileOnServer(Long fileId, String type) throws IOException, InterruptedException, URISyntaxException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonRequest = gson.toJson(type);

        HttpEntity httpEntity = (HttpEntity) MultipartEntityBuilder.create()
                // JSON
                .addPart("type",
                        new StringBody(jsonRequest, ContentType.APPLICATION_JSON))
                // FILE
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
        HttpRequest request2 = HttpRequest.newBuilder(new URI("http://" + uploadEndpoint + "/api/v1/files/" + fileId))
                // The Content-Type header is important, don't forget to set it.
                .header("Content-Type", httpEntity.getContentType().getValue())
                // Reads data from a pipeline stream.
                .GET().build();

        HttpResponse<String> responseBody = httpClient.send(request2, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        JSONObject jsonObject = new JSONObject(responseBody.body());
//        JsonObject response = new JSONO(responseBody.re());
        FileFromDownloadService response = new FileFromDownloadService();
        response.setId(((Number) jsonObject.get("id")).longValue());
        response.setFile(((String) jsonObject.get("file")).getBytes(StandardCharsets.UTF_8));
        response.setTitle((String) jsonObject.get("title"));
        response.setLink((String) jsonObject.get("link"));
        response.setDescription((String) jsonObject.get("description"));
        response.setDetails((String) jsonObject.get("details"));
        return response;
    }

    public void getActualFilesFromBranch(Long idBranch) {

    }

    public void getLastCommitFilesFromBranch(Long idBranch) {

    }

    public void saveFile(Long branchId, fr.esgi.projectservice.domain.model.File file, String type) throws IOException, URISyntaxException, InterruptedException {
        UploadFileToServerRequest request = buildRequestForFileObject(branchId, file, type);
        java.io.File tmpFile = new java.io.File("/tmpFilesConvert/" + file.getId().toString());
        file.getFile().transferTo(tmpFile);
        uploadFile(request, tmpFile);
        tmpFile.delete();
    }

    public void saveFileReverted(Long branchId, fr.esgi.projectservice.domain.model.File file, java.io.File fileReverted) throws IOException, URISyntaxException, InterruptedException {
        UploadFileToServerRequest requestActual = buildRequestForFileObject(branchId, file, "actual");
        UploadFileToServerRequest requestLast = buildRequestForFileObject(branchId, file, "last");
        uploadFile(requestActual, fileReverted);
        uploadFile(requestLast, fileReverted);

        fileReverted.delete();
    }

    public void saveFileRevertedForDiff(Long branchId, fr.esgi.projectservice.domain.model.File file, java.io.File fileReverted) throws IOException, URISyntaxException, InterruptedException {
        UploadFileToServerRequest requestActual = buildRequestForFileObject(branchId, file, "actual");
        UploadFileToServerRequest requestLast = buildRequestForFileObject(branchId, file, "last");
        uploadFile(requestActual, fileReverted);
        uploadFile(requestLast, fileReverted);

        fileReverted.delete();
    }

    public void saveDelta(Long branchId, Delta delta, java.io.File deltaFile) throws IOException, URISyntaxException, InterruptedException {
        UploadFileToServerRequest request = buildRequestForDeltaObject(branchId, delta, "patch");
        uploadFile(request, deltaFile);
        deltaFile.delete();
    }

    public void uploadFiles(Long idBranch, List<fr.esgi.projectservice.domain.model.File> files) throws IOException, URISyntaxException, InterruptedException {

        List<UploadFileToServerRequest> requests = buildRequests(idBranch, files, "actual");
        for (int i = 0; i < files.size(); i++) {
            java.io.File tmpFile = new java.io.File("/tmpFilesConvert/" + files.get(i).getId().toString());
            files.get(i).getFile().transferTo(tmpFile);
            uploadFile(requests.get(i), tmpFile);
            tmpFile.delete();
        }

    }

    private UploadFileToServerRequest buildRequestForFileObject(Long branchId, fr.esgi.projectservice.domain.model.File file, String type) {
        UploadFileToServerRequest request = new UploadFileToServerRequest();
        request.setId(file.getId());
        request.setDirecoryId(branchId);
        request.setType(type);
        request.setLink(null);
        request.setTitle(file.getName());

        return request;
    }

    private UploadFileToServerRequest buildRequestForDeltaObject(Long branchId, Delta delta, String type) {
        UploadFileToServerRequest request = new UploadFileToServerRequest();
        request.setId(delta.getId());
        request.setDirecoryId(branchId);
        request.setType(type);
        request.setLink(null);
        request.setTitle("raf");

        return request;
    }

    private List<UploadFileToServerRequest> buildRequests(Long idBranch, List<fr.esgi.projectservice.domain.model.File> files, String type) {
        List<UploadFileToServerRequest> fileRequests = new ArrayList<>();
        for (fr.esgi.projectservice.domain.model.File file : files) {
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

    public void deleteFile(fr.esgi.projectservice.domain.model.File file) {

    }

    public void deleteDeltas(List<Delta> deltaList) {

    }

    public void getDeltasFromCommit(Commit commit) {

    }

    private String uploadFile(UploadFileToServerRequest request, java.io.File file) throws IOException, URISyntaxException, InterruptedException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();


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
        HttpRequest request2 = HttpRequest.newBuilder(new URI("http://" + uploadEndpoint + "/api/v1/files"))
                // The Content-Type header is important, don't forget to set it.
                .header("Content-Type", httpEntity.getContentType().getValue())
                // Reads data from a pipeline stream.
                .POST(java.net.http.HttpRequest.BodyPublishers.ofInputStream(() -> Channels.newInputStream(pipe.source()))).build();

        HttpResponse<String> responseBody = httpClient.send(request2, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        return responseBody.body();
    }

    public byte[] getFileOnServerTest(Long fileId, String type) throws IOException, InterruptedException, URISyntaxException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonRequest = gson.toJson(type);

        HttpEntity httpEntity = (HttpEntity) MultipartEntityBuilder.create()
                // JSON
                // FILE
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
        HttpRequest request2 = HttpRequest.newBuilder(new URI("http://" + uploadEndpoint + "/api/v1/files/" + fileId + "/" + type))
                // The Content-Type header is important, don't forget to set it.
                .header("Content-Type", httpEntity.getContentType().getValue())
                // Reads data from a pipeline stream.
                .GET().build();

        HttpResponse<byte[]> responseBody = httpClient.send(request2, HttpResponse.BodyHandlers.ofByteArray());

        return responseBody.body();
    }
}
