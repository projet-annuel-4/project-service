package fr.esgi.projectservice.dto;

public class FileFromDownloadService {

    private Long id;
    private byte[] file;
    private String title;
    private String link;
    private String description;
    private String details;

    public FileFromDownloadService() {
    }

    ;

    public FileFromDownloadService(Long id, byte[] file, String title, String link, String description, String details) {
        this.id = id;
        this.file = file;
        this.title = title;
        this.link = link;
        this.description = description;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
