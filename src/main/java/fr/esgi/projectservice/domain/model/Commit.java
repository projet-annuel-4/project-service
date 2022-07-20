package fr.esgi.projectservice.domain.model;


import java.util.Date;

public class Commit {

    private Long id;
    private String name;
    private Date creationDate;
    private Commit parent;
    private Commit child;
    private Branch branch;

    public Commit() {
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

    public void setParent(Commit parent) {
        this.parent = parent;
    }

    public void setChild(Commit child) {
        this.child = child;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
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

    public Commit getParent() {
        return parent;
    }

    public Commit getChild() {
        return child;
    }

    public Branch getBranch() {
        return branch;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creationDate=" + creationDate +
                ", parent=" + parent +
                ", child=" + child +
                ", branch=" + branch +
                '}';
    }
}
