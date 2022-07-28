package fr.esgi.projectservice.data.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
public class GroupEntity {

    @Id
    private Long id;
    private String name;


    public GroupEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public GroupEntity() {

    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "GroupEntity{" +
                "id=" + id +
                '}';
    }
}
