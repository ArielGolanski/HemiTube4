package com.example.youtubeproject.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.youtubeproject.api.UserUpdateRequest;
import com.example.youtubeproject.entities.User;
import com.example.youtubeproject.entities.Video;
import com.example.youtubeproject.repositories.UsersRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UsersRepository userRepository;

    private MutableLiveData<User> userLiveData;

    private MutableLiveData<List<User>> users;

    public UserViewModel(Application application) {
        super(application);
        userRepository = new UsersRepository(application);
        userLiveData = new MutableLiveData<>();
    }

    public void registerUser(User user) {
        userRepository.registerUser(user, userLiveData);
    }

    public void loginUser(User user) {
        userRepository.loginUser(user, userLiveData);
    }

    public LiveData<User> getUser(String username) {
        return userRepository.getUser(username);
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<Boolean> deleteUser(String username, String token) {
        return userRepository.deleteUser(username, token);
    }

    public LiveData<Void> updateUser(String username, String newPic, String newNickName) {
        Log.d("TAG", "Request to update user: " + username + " with newPic: " + newPic + " and newNickName: " + newNickName);
        UserUpdateRequest updateRequest = new UserUpdateRequest(newPic, newNickName);
        return userRepository.updateUser(username, updateRequest);
    }

    public MutableLiveData<List<User>> getAllUsers() {
        if (users == null) {
            users = userRepository.getAllUsers();
        }
        return users;
    }
}
