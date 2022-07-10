package com.example.projectservicev.domain.model;


public class ModifiedFile {

    private Long id;
    private File fileEntity;
    private Branch branchEntity;
    private ModifiedFileTypeEnum modificationType;


    public ModifiedFile() {
    }

    public ModifiedFile(Long id, File fileEntity, Branch branchEntity, ModifiedFileTypeEnum modicationType) {
        this.id = id;
        this.fileEntity = fileEntity;
        this.branchEntity = branchEntity;
        this.modificationType = modicationType;
    }

    public void setModificationType(ModifiedFileTypeEnum modificationType) {
        this.modificationType = modificationType;
    }

    public ModifiedFileTypeEnum getModificationType() {
        return modificationType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFileEntity(File fileEntity) {
        this.fileEntity = fileEntity;
    }

    public void setBranchEntity(Branch branchEntity) {
        this.branchEntity = branchEntity;
    }

    public Long getId() {
        return id;
    }

    public File getFileEntity() {
        return fileEntity;
    }

    public Branch getBranchEntity() {
        return branchEntity;
    }
}
