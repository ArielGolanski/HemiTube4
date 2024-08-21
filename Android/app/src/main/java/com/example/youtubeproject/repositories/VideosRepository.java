package com.example.youtubeproject.repositories;

import android.app.Application;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtubeproject.AppDB;
import com.example.youtubeproject.api.ApiService;
import com.example.youtubeproject.api.CommentRequest;
import com.example.youtubeproject.api.RetrofitClient;
import com.example.youtubeproject.dao.VideoDao;
import com.example.youtubeproject.entities.Comment;
import com.example.youtubeproject.entities.ServerComment;
import com.example.youtubeproject.entities.SessionManager;
import com.example.youtubeproject.entities.UserVideo;
import com.example.youtubeproject.entities.Video;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideosRepository {
    private final ApiService apiService;
    private VideoDao videoDao;
    private LiveData<List<Video>> allVideos;

    public VideosRepository(Application application) {
        String token = SessionManager.getInstance().getToken(); // Fetch the token from SessionManager
        apiService = RetrofitClient.getAuthenticatedApiService(token);
        AppDB db = AppDB.getInstance(application);
        videoDao = db.videoDao();
        allVideos = videoDao.getAllVideos();
    }

    public LiveData<String> uploadVideo(Uri videoUri, Uri thumbnailUri, String title, String description, String publisher, Context context) {
        MutableLiveData<String> result = new MutableLiveData<>();


        File videoFile = new File(getPathFromUri(videoUri, context));
        File thumbnailFile = new File(getPathFromUri(thumbnailUri, context));

        Log.d("TAG", "amazinggggg2");

        RequestBody videoRequestBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        RequestBody thumbnailRequestBody = RequestBody.create(MediaType.parse("image/*"), thumbnailFile);

        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("file", videoFile.getName(), videoRequestBody);
        MultipartBody.Part thumbnailPart = MultipartBody.Part.createFormData("thumbnail", thumbnailFile.getName(), thumbnailRequestBody);

        Log.d("TAG", "amazinggggg3");

        RequestBody titleRequestBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody descriptionRequestBody = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody publisherRequestBody = RequestBody.create(MediaType.parse("text/plain"), publisher);

        Log.d("TAG", "Upload video request prepared");

        String token = SessionManager.getInstance().getToken();

        Call<Void> call = apiService.uploadVideo("Bearer " + token, videoPart, thumbnailPart, titleRequestBody, descriptionRequestBody, publisherRequestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    result.setValue("Video uploaded successfully!");
                    Log.d("TAG", "Video uploaded successfully");
                } else {
                    result.setValue("Failed to upload video");
                    Log.d("TAG", "Failed to upload video: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                result.setValue("Error: " + t.getMessage());
                Log.e("TAG", "Error uploading video", t);
            }
        });

        return result;
    }


    public MutableLiveData<List<Video>> getVideos() {
        MutableLiveData<List<Video>> videosData = new MutableLiveData<>();

        Log.d("TAG", "amazing");
        try {
            apiService.getVideos().enqueue(new Callback<List<Video>>() {
                @Override
                public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                    Log.d("TAG", "amazing2");
                    if (response.isSuccessful()) {
                        videosData.setValue(response.body());
                        Log.d("TAG", "Videos fetched: " + response.body().size());
                    } else {
                        Log.e("TAG", "Failed to fetch videos: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<Video>> call, Throwable t) {
                    Log.e("TAG", "Error fetching videos", t);
                    videosData.setValue(null);
                }
            });
        } catch (Exception e) {
            Log.e("TAG", "Exception in enqueue", e);
        }
        return videosData;
    }


    public MutableLiveData<UserVideo> getVideo(String username, String videoId) {
        final MutableLiveData<UserVideo> data = new MutableLiveData<>();
        Log.d("TAG", "Fetching video with username: " + username + ", videoId: " + videoId);
        Call<UserVideo> call = apiService.getVideo(username, videoId);

        // Log the URL
        Log.d("TAG", "Request URL: " + call.request().url());

        call.enqueue(new Callback<UserVideo>() {
            @Override
            public void onResponse(Call<UserVideo> call, Response<UserVideo> response) {
                Log.d("TAG", "Response received");
                if (response.isSuccessful()) {
                    Log.d("TAG", "Video fetched successfully: " + response.body());
                    data.setValue(response.body());
                } else {
                    try {
                        Log.e("TAG", "Error fetching video: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("TAG", "Error reading error body", e);
                    }
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserVideo> call, Throwable t) {
                Log.e("TAG", "Failed to fetch video", t);
                data.setValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<List<UserVideo>> getUserVideos(String username) {
        MutableLiveData<List<UserVideo>> videosData = new MutableLiveData<>();
        Log.d("TAG", "(repo) Start fetching user videos for: " + username);

        Call<List<UserVideo>> call = apiService.getUserVideos(username);

        // Log the URL
        Log.d("TAG", "(repo) Request URL: " + call.request().url());

        call.enqueue(new Callback<List<UserVideo>>() {
            @Override
            public void onResponse(Call<List<UserVideo>> call, Response<List<UserVideo>> response) {
                Log.d("TAG", "(repo) Response received");
                if (response.isSuccessful() && response.body() != null) {
                    videosData.setValue(response.body());
                    Log.d("TAG", "(repo) User videos fetched successfully: " + response.body().size());
                } else {
                    videosData.setValue(null);
                    Log.d("TAG", "(repo) Response body: " + response.body());
                    Log.e("TAG", "(repo) Failed to get user videos: " + response.message() + " - " + response.code());
                    try {
                        Log.e("TAG", "(repo) Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("TAG", "(repo) Error reading response body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserVideo>> call, Throwable t) {
                videosData.setValue(null);
                Log.e("TAG", "(repo) Network request failed: " + t.getMessage(), t);
            }
        });
        return videosData;
    }




    private String getPathFromUri(Uri uri, Context context) {
        Log.d("TAG", "Getting path from URI: " + uri.toString());
        String path = null;

        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    path = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    path = id.replaceFirst("raw:", "");
                } else {
                    try {
                        path = getDownloadPath(uri, context);
                    } catch (Exception e) {
                        Log.e("TAG", "Error resolving download URI", e);
                        return null;
                    }
                }
            } else if (isMediaDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                String selection = "_id=?";
                String[] selectionArgs = new String[]{split[1]};

                path = getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            path = getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }

        if (path != null && !new File(path).exists()) {
            Log.e("TAG", "File does not exist at path: " + path);
            return null;
        }

        Log.d("TAG", "Path from URI: " + path);
        return path;
    }

    private String getDownloadPath(Uri uri, Context context) {
        Cursor cursor = null;
        String path = null;
        try {
            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME};
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String fileName = cursor.getString(0);
                File file = new File(Environment.getExternalStorageDirectory() + "/Download/" + fileName);
                path = file.getAbsolutePath();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public LiveData<Boolean> deleteVideo(String username, String videoId) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        String token = SessionManager.getInstance().getToken();

        Call<Void> call = apiService.deleteVideo(username, videoId, "Bearer " + token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    result.setValue(true);
                    Log.d("Tag", "delete worked");
                } else {
                    result.setValue(false);
                    Log.d("Tag", "delete didnt work");

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                result.setValue(false);
            }
        });

        return result;
    }


    public LiveData<Boolean> updateVideo(String token, String username, UserVideo userVideo) {
        MutableLiveData<Boolean> success = new MutableLiveData<>();

        Call<UserVideo> call = apiService.updateVideo("Bearer " + token, username, userVideo.getId(), userVideo);
        call.enqueue(new Callback<UserVideo>() {
            @Override
            public void onResponse(Call<UserVideo> call, Response<UserVideo> response) {
                success.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<UserVideo> call, Throwable t) {
                success.setValue(false);
            }
        });

        return success;
    }

    public MutableLiveData<Comment> addComment(String username, String videoId, CommentRequest commentRequest) {
        Log.d("TAG", "in repo about to add" + commentRequest.getComment());
        MutableLiveData<Comment> liveData = new MutableLiveData<>();
        Call<Comment> call = apiService.addComment(username, videoId, commentRequest);
        Log.d("TAG", "trying to add comment: " + commentRequest.getUsername() + commentRequest.getUserID());


        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                    Log.d("TAG", "repo Comment added successfully: " + response.body().getText());
                } else {
                    Log.e("VideosRepository", "Error adding comment. Response code: " + response.code());
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("VideosRepository", "Error details: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.e("VideosRepository", "Failed to add comment: " + t.getMessage());
                liveData.setValue(null);
            }
        });

        return liveData;
    }

    public interface OnCommentDeleteCallback {
        void onSuccess();
        void onFailure();
    }

    public void deleteComment(String publisherId, String videoId, String commentId, String authToken, OnCommentDeleteCallback callback) {
        Call<Void> call = apiService.deleteComment(publisherId, videoId, commentId, authToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("VideosRepository", "Comment deleted successfully");
                    callback.onSuccess();
                } else {
                    Log.e("VideosRepository", "Failed to delete comment. Response code: " + response.code());
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("VideosRepository", "Error deleting comment: " + t.getMessage());
                callback.onFailure();
            }
        });
    }


    public void updateComment(String publisherId, String videoId, String commentId, ServerComment updatedComment, String authToken, OnCommentUpdateCallback callback) {
        String requestUrl = String.format("/api/users/%s/videos/%s/comments/%s", publisherId, videoId, commentId);
        Log.d("TAG", "Request URL: " + requestUrl);
        Log.d("TAG", "Updated Comment: " + updatedComment.getContent());

        Call<ServerComment> call = apiService.updateComment(publisherId, videoId, commentId, updatedComment, authToken);
        call.enqueue(new Callback<ServerComment>() {
            @Override
            public void onResponse(Call<ServerComment> call, Response<ServerComment> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "Comment updated successfully");
                    callback.onSuccess(response.body());
                } else {
                    Log.e("TAG", "Failed to update comment. Response code: " + response.code());
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ServerComment> call, Throwable t) {
                Log.e("TAG", "Error updating comment: " + t.getMessage());
                callback.onFailure();
            }
        });
    }

    public interface OnCommentUpdateCallback {
        void onSuccess(ServerComment updatedComment);
        void onFailure();
    }




}


