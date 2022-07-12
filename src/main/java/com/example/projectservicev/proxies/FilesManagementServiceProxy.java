package com.example.projectservicev.proxies;

import com.example.projectservicev.configuration.FeignClientConfiguration;
import com.example.projectservicev.dto.UploadFileToServerRequest;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "files-management-service", url = "localhost:9140/api/v1/files", configuration = FeignClientConfiguration.class)
public interface FilesManagementServiceProxy {

    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity upload(@RequestPart("file") MultipartFile file,
                          @RequestParam("details") UploadFileToServerRequest details
                          );

}
