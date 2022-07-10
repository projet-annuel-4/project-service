package com.example.projectservicev.request;

import com.example.projectservicev.data.entity.GroupEntity;

import javax.persistence.ManyToOne;
import java.util.Locale;

public class CreateProjectRequest {

    private String name;
    private Locale creationDate;
    private boolean visibility;
    private GroupEntity groupEntity;

    public CreateProjectRequest(String name, Locale creationDate, boolean visibility, GroupEntity groupEntity) {
        this.name = name;
        this.creationDate = creationDate;
        this.visibility = visibility;
        this.groupEntity = groupEntity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreationDate(Locale creationDate) {
        this.creationDate = creationDate;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public void setGroupEntity(GroupEntity groupEntity) {
        this.groupEntity = groupEntity;
    }

    public String getName() {
        return name;
    }

    public Locale getCreationDate() {
        return creationDate;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public GroupEntity getGroupEntity() {
        return groupEntity;
    }
}
