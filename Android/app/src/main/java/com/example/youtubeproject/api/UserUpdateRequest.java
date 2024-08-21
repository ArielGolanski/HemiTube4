package com.example.youtubeproject.api;

public class UserUpdateRequest {
    private String newPic;
    private String newNickName;

    public UserUpdateRequest(String newPic, String newNickName) {
        this.newPic = newPic;
        this.newNickName = newNickName;
    }

    public String getNewPic() {
        return newPic;
    }

    public void setNewPic(String newPic) {
        this.newPic = newPic;
    }

    public String getNewNickName() {
        return newNickName;
    }

    public void setNewNickName(String newNickName) {
        this.newNickName = newNickName;
    }
}