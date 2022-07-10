package com.example.projectservicev.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "groups")
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_id_seq")
    private Long id;

    public GroupEntity(Long id) {
        this.id = id;
    }

    public GroupEntity() {

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
