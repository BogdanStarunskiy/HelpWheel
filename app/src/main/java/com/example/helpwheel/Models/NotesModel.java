package com.example.helpwheel.Models;

public class NotesModel {
    String title, description, id, webUrl;

    public NotesModel(String id, String title, String description, String webUrl) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.webUrl = webUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
