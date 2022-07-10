package com.example.projectservicev.dto;

public class UploadFileToServerRequest {

    private long id;
    private Long direcoryId;
    private String type;
    private String title;
    private String link;
    private String description;

    public UploadFileToServerRequest() {
    }

    public UploadFileToServerRequest(long id, Long direcoryId, String type, String title, String link, String description) {
        this.id = id;
        this.direcoryId = direcoryId;
        this.type = type;
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getDirecoryId() {
        return direcoryId;
    }

    public void setDirecoryId(Long direcoryId) {
        this.direcoryId = direcoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "UploadFileToServerRequest{" +
                "id=" + id +
                ", direcoryId=" + direcoryId +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
