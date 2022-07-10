package com.example.projectservicev.domain.model;

import com.example.projectservicev.data.entity.CommitEntity;

public class Delta {

    private String patchUrl;
    private File fileSrc;
    private Commit formCommit;

    public Delta(String patchUrl, File fileSrc, Commit formCommit) {
        this.patchUrl = patchUrl;
        this.fileSrc = fileSrc;
        this.formCommit = formCommit;
    }

    public Delta(){

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
