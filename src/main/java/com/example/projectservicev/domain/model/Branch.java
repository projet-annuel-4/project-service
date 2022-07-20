package com.example.projectservicev.domain.model;


import java.util.Date;
import java.util.Locale;

public class Branch {

    private Long id;
    private String name;
    private Date creationDate;
    private Project project;

    public Branch() {
    }
    public Branch(Long id, String name, Date creationDate, Project project) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.project = project;
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

    public void setProject(Project project) {
        this.project = project;
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

    public Project getProject() {
        return project;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creationDate=" + creationDate +
                ", project=" + project +
                '}';
    }
}
