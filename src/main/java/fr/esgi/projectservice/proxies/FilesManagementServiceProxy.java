package fr.esgi.projectservice.proxies;

import fr.esgi.projectservice.configuration.FeignClientConfiguration;
import fr.esgi.projectservice.dto.UploadFileToServerRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "files-management-service", url = "localhost:9140/api/v1/files", configuration = FeignClientConfiguration.class)
public interface FilesManagementServiceProxy {

    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity upload(@RequestPart("file") MultipartFile file,
                          @RequestParam("details") UploadFileToServerRequest details
    );

}
