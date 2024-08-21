package com.example.youtubeproject.api;

public class CommentRequest {
    private String username;
    private String userID;
    private String content;

    public CommentRequest(String comment, String userID, String username) {
        this.username = username;
        this.userID = userID;
        this.content = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getComment() {
        return content;
    }

    public void setComment(String comment) {
        this.content = comment;
    }
}