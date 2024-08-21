package com.example.youtubeproject.entities;

public class ServerComment {
    private String id;
    private String content;  // Ensure this matches the server's expected field
    private String videoId;
    private String publisherId;


    public ServerComment(){

    }
    public ServerComment(String id, String content, String videoId, String publisherId){
        this.id = id;
        this.publisherId = publisherId;
        this.content = content;
        this.videoId = videoId;
    }

    // Getters and setters for the fields
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

}
