package com.example.projectservicev.domain.model;

import com.example.projectservicev.data.entity.CommitEntity;
import org.springframework.web.multipart.MultipartFile;

public class Delta {

    private Long id;
    private String patchUrl;
    private File fileSrc;
    private MultipartFile file;
    private Commit formCommit;

    public Delta(Long id, String patchUrl, File fileSrc, MultipartFile file, Commit formCommit) {
        this.id = id;
        this.patchUrl = patchUrl;
        this.fileSrc = fileSrc;
        this.file = file;
        this.formCommit = formCommit;
    }

    public Delta(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getPatchUrl() {
        return patchUrl;
    }

    public File getFileSrc() {
        return fileSrc;
    }

    public Commit getFormCommit() {
        return formCommit;
    }

    public void setPatchUrl(String patchUrl) {
        this.patchUrl = patchUrl;
    }

    public void setFileSrc(File fileSrc) {
        this.fileSrc = fileSrc;
    }

    public void setFormCommit(Commit formCommit) {
        this.formCommit = formCommit;
    }
}
