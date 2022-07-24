package fr.esgi.projectservice.request;

import fr.esgi.projectservice.data.entity.GroupEntity;

import java.util.Locale;

public class CreateProjectRequest {

    private String name;
    private boolean visibility;
    private Long groupId;

    public CreateProjectRequest(String name, Locale creationDate, boolean visibility, Long groupEntity) {
        this.name = name;
        this.visibility = visibility;
        this.groupId = groupEntity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public Long getGroupEntity() {
        return groupId;
    }
}
