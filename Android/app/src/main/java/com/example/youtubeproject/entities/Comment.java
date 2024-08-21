package com.example.youtubeproject.entities;

import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("_id")
    private String id;

    @SerializedName("userID")
    private String userId;

    @SerializedName("text")
    private String text;

    public Comment(){

    }

    public Comment(String userId, String text){
        this.userId = userId;
        this.text = text;
    }


    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
