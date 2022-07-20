package fr.esgi.projectservice.request;

public class UpdateProjectRequest {

    private String name;
    private boolean visibility;

    public UpdateProjectRequest(String name, boolean visibility) {
        this.name = name;
        this.visibility = visibility;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public boolean isVisibility() {
        return visibility;
    }
}
