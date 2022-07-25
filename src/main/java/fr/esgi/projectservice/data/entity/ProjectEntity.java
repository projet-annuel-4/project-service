package fr.esgi.projectservice.data.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_id_seq")
    private Long id;
    private String name;
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    private boolean visibility;
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupEntity groupEntity;
    private boolean deleted;

    public ProjectEntity(Long id, String name, Date creationDate, boolean visibility, GroupEntity groupEntity, boolean deleted) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.visibility = visibility;
        this.groupEntity = groupEntity;
        this.deleted = deleted;
    }

    public ProjectEntity() {

    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setGroupEntity(GroupEntity groupEntity) {
        this.groupEntity = groupEntity;
    }

    public GroupEntity getGroupEntity() {
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

    @Override
    public String toString() {
        return "ProjectEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creationDate=" + creationDate +
                ", visibility=" + visibility +
                ", groupEntity=" + groupEntity.toString() +
                '}';
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ProjectEntity that = (ProjectEntity) o;
//        return Objects.equals(groupEntity.getId(), that.groupEntity.getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(groupEntity);
//    }
}
