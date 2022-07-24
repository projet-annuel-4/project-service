package fr.esgi.projectservice.request;

public class CreateFileRequest {

    private String name;

    public CreateFileRequest(String name) {
        this.name = name;
    }

    public CreateFileRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
