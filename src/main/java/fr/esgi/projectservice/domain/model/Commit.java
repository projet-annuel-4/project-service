package fr.esgi.projectservice.domain.model;


import java.util.Date;
import java.util.Objects;

public class Commit {

    private Long id;
    private String name;
    private Integer order;
    private Date creationDate;
    private Commit parent;
    private Commit child;
    private Branch branch;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Commit)) return false;
        Commit commit = (Commit) o;
        return getId().equals(commit.getId()) && getName().equals(commit.getName()) && getOrder().equals(commit.getOrder()) && getCreationDate().equals(commit.getCreationDate()) && getParent().equals(commit.getParent()) && getChild().equals(commit.getChild()) && getBranch().equals(commit.getBranch());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getOrder(), getCreationDate(), getParent(), getChild(), getBranch());
    }

    public Commit() {
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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
