package com.example.projectservicev.domain.model;


import org.springframework.web.multipart.MultipartFile;

public class File {

    private Long id;
    private String name;
    private boolean type;
    private String url;
    private MultipartFile file;
    private File parentDirectory;
    private String lastCommitName;
    private Commit commitCreation;
    private Branch branch;
    private boolean fromInit;
    private boolean deleted;

    public File(){

    }

    public File(Long id, String name, boolean type, String url, MultipartFile file, File parentDirectory, String lastCommitName, Commit commitCreation, Branch branch, boolean fromInit, boolean deleted) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.url = url;
        this.file = file;
        this.parentDirectory = parentDirectory;
        this.lastCommitName = lastCommitName;
        this.commitCreation = commitCreation;
        this.branch = branch;
        this.fromInit = fromInit;
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public void setParentDirectory(File parentDirectory) {
        this.parentDirectory = parentDirectory;
    }

    public void setLastCommitName(String lastCommitName) {
        this.lastCommitName = lastCommitName;
    }

    public void setCommitCreation(Commit commitCreation) {
        this.commitCreation = commitCreation;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public void setFromInit(boolean fromInit) {
        this.fromInit = fromInit;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public MultipartFile getFile() {
        return file;
    }

    public File getParentDirectory() {
        return parentDirectory;
    }

    public String getLastCommitName() {
        return lastCommitName;
    }

    public Commit getCommitCreation() {
        return commitCreation;
    }

    public Branch getBranch() {
        return branch;
    }

    public boolean isFromInit() {
        return fromInit;
    }
}
