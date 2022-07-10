package com.example.projectservicev.request;

import com.example.projectservicev.domain.model.File;

import java.util.List;

public class InitBranchRequest {

    List<FileRequest> files;

    public InitBranchRequest(List<FileRequest> files) {
        this.files = files;
    }

    public InitBranchRequest() {
    }

    public List<FileRequest> getFiles() {
        return files;
    }

    public void setFiles(List<FileRequest> files) {
        this.files = files;
    }
}
