package com.example.youtubeproject.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserVideo {

    @SerializedName("_id")
    private String id;

    @SerializedName("publisher")
    private String publisher; //  string ID of the publisher

    @SerializedName("comments")
    private List<Comment> comments;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("thumbnail_name")
    private String thumbnailName;

    @SerializedName("thumbnail_data")
    private String thumbnailData;

    @SerializedName("file_name")
    private String fileName;

    @SerializedName("file_data")
    private String fileData;

    @SerializedName("uploadDate")
    private String uploadDate;

    @SerializedName("__v")
    private int __v;


    public UserVideo(String publisher, List<Comment> comments, String title, String description, String thumbnailName, String thumbnailData, String fileName, String fileData, String uploadDate, int __v) {
        this.publisher = publisher;
        this.comments = comments;
        this.title = title;
        this.description = description;
        this.thumbnailName = thumbnailName;
        this.thumbnailData = thumbnailData;
        this.fileName = fileName;
        this.fileData = fileData;
        this.uploadDate = uploadDate;
        this.__v = __v;
    }


    // Constructor
    public UserVideo(String publisher, List<Comment> comments, String title, String description, String thumbnailName, String thumbnailData, String fileName, String fileData, String uploadDate) {
        this.publisher = publisher;
        this.comments = comments;
        this.title = title;
        this.description = description;
        this.thumbnailName = thumbnailName;
        this.thumbnailData = thumbnailData;
        this.fileName = fileName;
        this.fileData = fileData;
        this.uploadDate = uploadDate;
        this.__v = 0;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getThumbnailName() {
        return thumbnailName;
    }

    public void setThumbnailName(String thumbnailName) {
        this.thumbnailName = thumbnailName;
    }

    public String getThumbnailData() {
        return thumbnailData;
    }

    public void setThumbnailData(String thumbnailData) {
        this.thumbnailData = thumbnailData;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String content) {
        this.description = content;
    }

    public String getDescription() {
        return description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

}
