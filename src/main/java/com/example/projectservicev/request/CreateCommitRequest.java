package com.example.projectservicev.request;

public class CreateCommitRequest {

    private String name;

    public CreateCommitRequest(String name) {
        this.name = name;
    }

    public CreateCommitRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
