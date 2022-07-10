package com.example.projectservicev.request;

public class FileRequest {

    private String name;
    private boolean type;
    private boolean fromInit;

    public FileRequest() {}

    public FileRequest(String name, boolean type, boolean fromInit) {
        this.name = name;
        this.type = type;
        this.fromInit = fromInit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public boolean isFromInit() {
        return fromInit;
    }

    public void setFromInit(boolean fromInit) {
        this.fromInit = fromInit;
    }
}
