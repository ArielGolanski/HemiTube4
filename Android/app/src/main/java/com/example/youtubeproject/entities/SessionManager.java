package com.example.youtubeproject.entities;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.youtubeproject.R;
import com.example.youtubeproject.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private static SessionManager ourInstance;

    public SessionManager() {
    }

    private boolean isNightModeOn = false;

    private boolean isLogedIn = false;

    private User loggedUser;

    List<Video> videos;
    private String token;
    private String userId;
    private String usernameInPage;
    private UserViewModel userViewModel;


    public static SessionManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new SessionManager();
        }

        return ourInstance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        Log.d("SessionManager", "Token set: " + token);
    }

    public String getUsernameInPage() {
        return usernameInPage;
    }

    public void setUsernameInPage(String username) {
        this.usernameInPage = username;
        Log.d("TAG", "(SessionManager)usernameInPage set: " + usernameInPage);
    }

    public String getUserId() { // Add this getter
        return userId;
    }

    public void setUserId(String userId) { // Add this setter
        this.userId = userId;
        Log.d("SessionManager", "UserId set: " + userId);
    }

    public User getLoggedUser() {
        return loggedUser;
    }


    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }


    public boolean isLogedIn() {
        return isLogedIn;
    }

    public void setLogedIn(boolean logedIn) {
        isLogedIn = logedIn;
    }

    public void setNickname(String nickname){ this.loggedUser.setNickName(nickname);}
    public void setProfilepic(String pic){ this.loggedUser.setProfilePic(pic);}

    public boolean isNightModeOn() {
        return isNightModeOn;
    }

    public void setNightModeOn(boolean nightModeOn) {
        isNightModeOn = nightModeOn;
    }

    public List<Video> getVideos() {
        return videos;
    }


    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }


    public void addComment(Comment comment, Video video) {
        for (int i = 0; i < videos.size(); i++) {
            if (videos.get(i).getId().equals(video.getId())) {
                Log.i("i", video.getId());
                videos.get(i).addComment(comment);
            }
        }
    }


    public void replaceVideo(Video oldVideo, Video video) {
            videos.remove(oldVideo);
            videos.add(video);
        }

    public MutableLiveData<List<User>> getUsers() {
        userViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(UserViewModel.class);
        return userViewModel.getAllUsers();
    }
}
