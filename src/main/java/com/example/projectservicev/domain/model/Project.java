package com.example.projectservicev.domain.model;

import java.util.Date;

public class Project {

    private Long id;
    private String name;
    private Date creationDate;
    private boolean visibility;
    private Group group;

    public Project(Long id, String name, Date creationDate, boolean visibility, Group group) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.visibility = visibility;
        this.group = group;
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

    public boolean getVisibility() {
        return visibility;
    }

    public Group getGroup() {
        return group;
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

    public void setGroup(Group group) {
        this.group = group;
    }
}
