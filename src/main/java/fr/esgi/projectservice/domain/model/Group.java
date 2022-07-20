package fr.esgi.projectservice.domain.model;

public class Group {

    private Long id;

    public Group(Long id) {
        this.id = id;
    }

    public Group() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
