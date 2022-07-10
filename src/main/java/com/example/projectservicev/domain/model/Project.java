package com.example.projectservicev.domain.model;

import com.example.projectservicev.data.entity.GroupEntity;

import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.Locale;

public class Project {

    private Long id;
    private String name;
    private Date creationDate;
    private boolean visibility;
    private Group groupEntity;

    public Project(Long id, String name, Date creationDate, boolean visibility, Group groupEntity) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.visibility = visibility;
        this.groupEntity = groupEntity;
    }

    public Project() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public Group getGroupEntity() {
        return groupEntity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public void setGroupEntity(Group groupEntity) {
        this.groupEntity = groupEntity;
    }
}
