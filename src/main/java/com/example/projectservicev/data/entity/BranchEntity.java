package com.example.projectservicev.data.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "branch")
public class BranchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_id_seq")
    private Long id;
    private String name;
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;

    public BranchEntity(Long id, String name, Date creationDate, ProjectEntity projectEntity) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.projectEntity = projectEntity;
    }

    public BranchEntity() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public ProjectEntity getProjectEntity() {
        return projectEntity;
    }

    public void setProjectEntity(ProjectEntity projectEntity) {
        this.projectEntity = projectEntity;
    }
}
