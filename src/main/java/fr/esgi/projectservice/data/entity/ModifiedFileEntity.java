package fr.esgi.projectservice.data.entity;

import fr.esgi.projectservice.domain.model.ModifiedFileTypeEnum;

import javax.persistence.*;

@Entity
@Table(name = "modified_files")
public class ModifiedFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "modified_files_id_seq")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private FileEntity fileEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    private BranchEntity branchEntity;
    @Enumerated(EnumType.STRING)
    private ModifiedFileTypeEnum modificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    private CommitEntity commitEntity;


    public ModifiedFileEntity() {

    }

    public ModifiedFileEntity(Long id, FileEntity fileEntity, BranchEntity branchEntity, ModifiedFileTypeEnum modificationType, CommitEntity commitEntity) {
        this.id = id;
        this.fileEntity = fileEntity;
        this.branchEntity = branchEntity;
        this.modificationType = modificationType;
        this.commitEntity = commitEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileEntity getFileEntity() {
        return fileEntity;
    }

    public void setFileEntity(FileEntity fileEntity) {
        this.fileEntity = fileEntity;
    }

    public BranchEntity getBranchEntity() {
        return branchEntity;
    }

    public void setBranchEntity(BranchEntity branchEntity) {
        this.branchEntity = branchEntity;
    }

    public ModifiedFileTypeEnum getModificationType() {
        return modificationType;
    }

    public void setModificationType(ModifiedFileTypeEnum modificationType) {
        this.modificationType = modificationType;
    }

    public CommitEntity getCommitEntity() {
        return commitEntity;
    }

    public void setCommitEntity(CommitEntity commitEntity) {
        this.commitEntity = commitEntity;
    }

    @Override
    public String toString() {
        return "ModifiedFileEntity{" +
                "id=" + id +
                ", fileEntity=" + fileEntity +
                ", branchEntity=" + branchEntity +
                ", modificationType=" + modificationType +
                ", commitEntity=" + commitEntity +
                '}';
    }
}
