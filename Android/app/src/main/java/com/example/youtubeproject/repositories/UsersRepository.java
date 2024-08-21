package com.example.youtubeproject.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtubeproject.AppDB;
import com.example.youtubeproject.api.ApiService;
import com.example.youtubeproject.api.RetrofitClient;
import com.example.youtubeproject.api.UserUpdateRequest;
import com.example.youtubeproject.dao.UserDao;
import com.example.youtubeproject.entities.SessionManager;
import com.example.youtubeproject.entities.User;
import com.example.youtubeproject.entities.Video;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Application;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsersRepository {
    private ApiService apiService;
    private UserDao userDao;
    private LiveData<List<User>> allUsers;
    private ExecutorService executorService;

    public UsersRepository(Application application) {
        AppDB db = AppDB.getInstance(application);
        userDao = db.userDao();
        allUsers = userDao.getAllUsers();
        executorService = Executors.newSingleThreadExecutor();
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void registerUser(User user, MutableLiveData<User> userLiveData) {
        Call<User> call = apiService.registerUser(user);
        Log.d("TAG", "Request URL: " + call.request().url());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    userLiveData.setValue(response.body());
                } else {
                    Log.e("TAG", "Error in registerUser: " + response.message() + " - " + response.code());
                    userLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("TAG", "Failure in registerUser: " + t.getMessage());
                userLiveData.setValue(null);
            }
        });
    }

    public void loginUser(User user, MutableLiveData<User> userLiveData) {
        Call<User> call = apiService.loginUser(user);
        Log.d("TAG", "Request URL: " + call.request().url());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User loggedInUser = response.body();
                    if (loggedInUser != null) {
                        Log.d("TAG", "Login successful");
                        SessionManager.getInstance().setLoggedUser(loggedInUser);
                        userLiveData.setValue(loggedInUser);
                    } else {
                        Log.e("TAG", "Logged in user is null");
                        userLiveData.setValue(null);
                    }
                } else {
                    Log.e("TAG", "Error in loginUser: " + response.message() + " - " + response.code());
                    userLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("TAG", "Failure in loginUser: " + t.getMessage());
                userLiveData.setValue(null);
            }
        });
    }

    public LiveData<User> getUser(String username) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();

        Call<User> call = apiService.getUser(username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    userLiveData.setValue(response.body());
                } else {
                    Log.e("TAG", "Error response: " + response.message());
                    userLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("TAG", "Failure response: " + t.getMessage());
                userLiveData.setValue(null);
            }
        });

        return userLiveData;
    }

    public LiveData<Void> updateUser(String username, UserUpdateRequest updateRequest) {
        final MutableLiveData<Void> data = new MutableLiveData<>();
        String token = "Bearer " + SessionManager.getInstance().getToken();

        Log.d("TAG", "(repository)Updating user: " + username + " with token: " + token);
        apiService.updateUser(token, username, updateRequest).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "(repository)User updated successfully: " + response.message());
                    data.setValue(null); // Indicate success
                } else {
                    Log.d("TAG", "(repository)Failed to update user: " + response.message());
                    Log.e("TAG", "(repo)Error in updateUser: " + response.message() + " - " + response.code());
                    try {
                        Log.d("TAG", "(repo)Response body string: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("TAG", "(repo)Error reading response body", e);
                    }
                    data.setValue(null); // Indicate failure
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TAG", "Error updating user", t);
                data.setValue(null); // Indicate error
            }
        });

        return data;
    }



    public LiveData<Boolean> deleteUser(String username, String token) {
        MutableLiveData<Boolean> deletionStatus = new MutableLiveData<>();

        apiService.deleteUser(username, "Bearer " + token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                deletionStatus.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                deletionStatus.setValue(false); // Set deletion status as false on failure
            }
        });

        return deletionStatus;
    }

    public MutableLiveData<List<User>> getAllUsers() {
        MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>();

        apiService.getAllUsers().enqueue(new Callback<List<User>>() {

            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d("TAG", "amazing2");
                if (response.isSuccessful()) {
                    usersLiveData.setValue(response.body());
                    Log.d("TAG", "Videos fetched: " + response.body().size());
                } else {
                    Log.e("TAG", "Failed to fetch videos: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("TAG", "Error fetching videos", t);
                usersLiveData.setValue(null);
            }
        });
        return usersLiveData;
    }
}
