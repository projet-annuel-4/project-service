package fr.esgi.projectservice.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "commit")
public class CommitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commit_id_seq")
    private Long id;
    private String name;
    private Integer order_commit;
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private CommitEntity parent;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private CommitEntity child;
    @ManyToOne(fetch = FetchType.LAZY)
    private BranchEntity branchEntity;


    public CommitEntity(Long id, String name, Date creationDate, CommitEntity parent, CommitEntity child, BranchEntity branchEntity, Integer order_commit) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.parent = parent;
        this.child = child;
        this.branchEntity = branchEntity;
        this.order_commit = order_commit;
    }

    public CommitEntity() {

    }

    public Integer getOrder() {
        return order_commit;
    }

    public void setOrder(Integer order) {
        this.order_commit = order;
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

    public void setParent(CommitEntity parent) {
        this.parent = parent;
    }

    public void setChild(CommitEntity child) {
        this.child = child;
    }

    public void setBranch(BranchEntity branchEntity) {
        this.branchEntity = branchEntity;
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

    public CommitEntity getParent() {
        return parent;
    }

    public CommitEntity getChild() {
        return child;
    }

    public BranchEntity getBranch() {
        return branchEntity;
    }
}
