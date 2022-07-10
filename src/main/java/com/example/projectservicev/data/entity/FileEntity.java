package com.example.projectservicev.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "file")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_id_seq")
    private Long id;
    private String name;
    private boolean type;
    private String url;
    @ManyToOne
    private FileEntity parentDirectory;
    private String lastCommitName;
    @ManyToOne
    private CommitEntity commitEntityCreation;
    @ManyToOne
    private BranchEntity branchEntity;
    private boolean fromInit;

    public FileEntity() {
    }

    public FileEntity(Long id, String name, boolean type, String url, FileEntity parentDirectory, String lastCommitName, CommitEntity commitEntityCreation, BranchEntity branchEntity, boolean fromInit) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.url = url;
        this.parentDirectory = parentDirectory;
        this.lastCommitName = lastCommitName;
        this.commitEntityCreation = commitEntityCreation;
        this.branchEntity = branchEntity;
        this.fromInit = fromInit;
    }

    public BranchEntity getBranch() {
        return branchEntity;
    }

    public void setBranch(BranchEntity branchEntity) {
        this.branchEntity = branchEntity;
    }

    public void setFromInit(boolean fromInit) {
        this.fromInit = fromInit;
    }

    public boolean isFromInit() {
        return fromInit;
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

    public void setParentDirectory(FileEntity parentDirectory) {
        this.parentDirectory = parentDirectory;
    }

    public void setLastCommitName(String lastCommitName) {
        this.lastCommitName = lastCommitName;
    }

    public void setCommitCreation(CommitEntity commitEntityCreation) {
        this.commitEntityCreation = commitEntityCreation;
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

    public FileEntity getParentDirectory() {
        return parentDirectory;
    }

    public String getLastCommitName() {
        return lastCommitName;
    }

    public CommitEntity getCommitCreation() {
        return commitEntityCreation;
    }
}
