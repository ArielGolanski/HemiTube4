package com.example.youtubeproject.viewmodels;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.youtubeproject.api.CommentRequest;
import com.example.youtubeproject.entities.Comment;
import com.example.youtubeproject.entities.ServerComment;
import com.example.youtubeproject.entities.SessionManager;
import com.example.youtubeproject.entities.UserVideo;
import com.example.youtubeproject.entities.Video;
import com.example.youtubeproject.repositories.VideosRepository;

import com.example.youtubeproject.viewmodels.VideoViewModel;

import java.util.List;

public class VideoViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> uploadResult;
    private final VideosRepository videosRepository;
    private final MutableLiveData<Boolean> deleteResult = new MutableLiveData<>();

    private MutableLiveData<UserVideo> video;

    private MutableLiveData<List<Video>> videos;

    private MutableLiveData<List<UserVideo>> userVideos;




    public VideoViewModel(@NonNull Application application) {
        super(application);
        isLoading = new MutableLiveData<>();
        uploadResult = new MutableLiveData<>();
        videosRepository = new VideosRepository(application);
        videos = videosRepository.getVideos();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getUploadResult() {
        return uploadResult;
    }

    public void uploadVideo(Uri videoUri, Uri thumbnailUri, String title, String description, String publisher, Context context) {
        isLoading.setValue(true);
        LiveData<String> result = videosRepository.uploadVideo(videoUri, thumbnailUri, title, description, publisher, context);
        result.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String uploadResult) {
                Log.d("TAG", "Upload result received in ViewModel: " + uploadResult);
                isLoading.setValue(false);
                VideoViewModel.this.uploadResult.setValue(uploadResult);
                result.removeObserver(this); // Ensure the observer is removed after use
            }
        });
        Log.d("TAG", "Upload video initiated");
    }

    public LiveData<List<Video>> getVideos() {
        if (videos == null) {

            videos = videosRepository.getVideos();
        }
        return videos;
    }

    public MutableLiveData<UserVideo> getVideo(String username, String videoId) {
        if (video == null) {
            Log.d("TAG", "Fetching video for username: " + username + ", videoId: " + videoId);
            video = videosRepository.getVideo(username, videoId);
        }
        return video;
    }

    public MutableLiveData<List<UserVideo>> getUserVideos(String username) {
        if (userVideos == null) {
            userVideos = videosRepository.getUserVideos(username);
        }
        return userVideos;
    }

//    // delete
//    public LiveData<String> getDeleteResult() {
//        return deleteResult;
//    }

    public LiveData<Boolean> deleteVideo(String username, String videoId) {
        isLoading.setValue(true);
        LiveData<Boolean> result = videosRepository.deleteVideo(username, videoId);
        result.observeForever(success -> {
            deleteResult.setValue(success);
            isLoading.setValue(false);
        });
        return deleteResult;
    }

    public LiveData<Boolean> updateVideo(String token, String username, UserVideo userVideo) {
        return videosRepository.updateVideo(token, username, userVideo);
    }
    public LiveData<Comment> addComment(String username, String videoId, CommentRequest commentRequest) {
        return videosRepository.addComment(username, videoId, commentRequest);
    }

    public LiveData<Boolean> deleteComment(String publisherId, String videoId, String commentId) {
        MutableLiveData<Boolean> success = new MutableLiveData<>();
        String authToken = "Bearer " + SessionManager.getInstance().getToken();
        videosRepository.deleteComment(publisherId, videoId, commentId, authToken, new VideosRepository.OnCommentDeleteCallback() {
            @Override
            public void onSuccess() {
                Log.d("VideoViewModel", "Comment deleted successfully");
                success.setValue(true);
            }

            @Override
            public void onFailure() {
                Log.e("VideoViewModel", "Failed to delete comment");
                success.setValue(false);
            }
        });
        return success;
    }

    public LiveData<ServerComment> updateComment(String publisherId, String videoId, String commentId, String updatedText) {
        MutableLiveData<ServerComment> updatedComment = new MutableLiveData<>();
        String authToken = "Bearer " + SessionManager.getInstance().getToken();
        ServerComment comment = new ServerComment();
        comment.setContent(updatedText);
        videosRepository.updateComment(publisherId, videoId, commentId, comment, authToken, new VideosRepository.OnCommentUpdateCallback() {
            @Override
            public void onSuccess(ServerComment updatedCommentResponse) {
                Log.d("TAG", "Comment updated successfully");
                updatedComment.setValue(updatedCommentResponse);
            }

            @Override
            public void onFailure() {
                Log.e("TAG", "Failed to update comment");
                updatedComment.setValue(null);
            }
        });
        return updatedComment;
    }

    public LiveData<List<Video>> getRecommendedVideos(String username, String videoId) {
        MutableLiveData<List<Video>> recommendedVideos = new MutableLiveData<>();
        String authToken = "Bearer " + SessionManager.getInstance().getToken();  // Retrieve the JWT token from the session manager

        videosRepository.getRecommendedVideos(username, videoId, authToken, new VideosRepository.OnRecommendedVideosCallback() {
            @Override
            public void onSuccess(List<Video> videos) {
                Log.d("TAG", "Recommended videos fetched successfully");
                recommendedVideos.setValue(videos);
            }

            @Override
            public void onFailure() {
                Log.e("TAG", "Failed to fetch recommended videos");
                recommendedVideos.setValue(null);
            }
        });
        return recommendedVideos;
    }

}






