package fr.esgi.projectservice.request;

import fr.esgi.projectservice.data.entity.GroupEntity;

import java.util.Locale;

public class CreateProjectRequest {

    private String name;
    private boolean visibility;
    private GroupEntity groupEntity;

    public CreateProjectRequest(String name, Locale creationDate, boolean visibility, GroupEntity groupEntity) {
        this.name = name;
        this.visibility = visibility;
        this.groupEntity = groupEntity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public void setGroupEntity(GroupEntity groupEntity) {
        this.groupEntity = groupEntity;
    }

    public String getName() {
        return name;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public GroupEntity getGroupEntity() {
        return groupEntity;
    }
}
