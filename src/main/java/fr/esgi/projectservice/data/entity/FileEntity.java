package fr.esgi.projectservice.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private FileEntity parentDirectory;
    private String lastCommitName;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private CommitEntity commitEntityCreation;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private BranchEntity branchEntity;
    private boolean fromInit;
    private boolean deleted;

    public FileEntity() {
    }

    public FileEntity(Long id, String name, boolean type, String url, FileEntity parentDirectory, String lastCommitName, CommitEntity commitEntityCreation, BranchEntity branchEntity, boolean fromInit, boolean deleted) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.url = url;
        this.parentDirectory = parentDirectory;
        this.lastCommitName = lastCommitName;
        this.commitEntityCreation = commitEntityCreation;
        this.branchEntity = branchEntity;
        this.fromInit = fromInit;
        this.deleted = deleted;
    }

    public CommitEntity getCommitEntityCreation() {
        return commitEntityCreation;
    }

    public void setCommitEntityCreation(CommitEntity commitEntityCreation) {
        this.commitEntityCreation = commitEntityCreation;
    }

    public BranchEntity getBranchEntity() {
        return branchEntity;
    }

    public void setBranchEntity(BranchEntity branchEntity) {
        this.branchEntity = branchEntity;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    @Override
    public String toString() {
        return "FileEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", parentDirectory=" + parentDirectory +
                ", lastCommitName='" + lastCommitName + '\'' +
                ", commitEntityCreation=" + commitEntityCreation +
                ", branchEntity=" + branchEntity +
                ", fromInit=" + fromInit +
                '}';
    }
}
