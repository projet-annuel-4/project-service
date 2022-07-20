package com.example.projectservicev.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "delta")
public class DeltaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delta_id_seq")
    private Long id;
    private String patchUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    private FileEntity fileSrc;
    @ManyToOne(fetch = FetchType.LAZY)
    private CommitEntity formCommitEntity;

    public DeltaEntity(Long id, String patchUrl, FileEntity fileSrc, CommitEntity formCommitEntity) {
        this.id = id;
        this.patchUrl = patchUrl;
        this.fileSrc = fileSrc;
        this.formCommitEntity = formCommitEntity;
    }

    public DeltaEntity() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPatchUrl(String patchUrl) {
        this.patchUrl = patchUrl;
    }

    public void setFileSrc(FileEntity fileSrc) {
        this.fileSrc = fileSrc;
    }

    public void setFormCommit(CommitEntity formCommitEntity) {
        this.formCommitEntity = formCommitEntity;
    }

    public Long getId() {
        return id;
    }

    public String getPatchUrl() {
        return patchUrl;
    }

    public FileEntity getFileSrc() {
        return fileSrc;
    }

    public CommitEntity getFormCommit() {
        return formCommitEntity;
    }
}
