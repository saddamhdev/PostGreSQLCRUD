package com.PostGreSQL.model;
import jakarta.persistence.Embeddable;

@Embeddable
public class DemoLink {
    private String type;
    private String url;

    public DemoLink() {}

    public DemoLink(String type, String url) {
        this.type = type;
        this.url = url;
    }

    // Getters and setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
