package com.example.youtubeproject.api;

import com.example.youtubeproject.entities.Comment;
import com.example.youtubeproject.entities.ServerComment;
import com.example.youtubeproject.entities.User;
import com.example.youtubeproject.entities.UserVideo;
import com.example.youtubeproject.entities.Video;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @POST("/api/users")
    Call<User> registerUser(@Body User user);

    @POST("/api/tokens")
    Call<User> loginUser(@Body User user);

    @GET("/api/users/{username}")
    Call<User> getUser(@Path("username") String username);

    @DELETE("/api/users/{username}")
    Call<Void> deleteUser(@Path("username") String username, @Header("Authorization") String token);

    @PUT("users/{username}")
    Call<Void> updateUser(@Header("Authorization") String token, @Path("username") String username, @Body UserUpdateRequest updateRequest);

    @Multipart
    @POST("/api/users/{username}/videos")
    Call<Void> uploadVideo(
            @Header("Authorization") String token,
            @Part MultipartBody.Part video,
            @Part MultipartBody.Part thumbnail,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("publisher") RequestBody publisher
    );

    @GET("/api/videosHemi")
    Call<List<Video>> getVideos();

    @GET("/api/users/{username}/videos/{pid}")
    Call<UserVideo> getVideo(@Path("username") String username, @Path("pid") String videoId);

    @GET("users/{username}/videos")
    Call<List<UserVideo>> getUserVideos(@Path("username") String username);

    //delete
    @DELETE("/api/users/{username}/videos/{videoId}")
    Call<Void> deleteVideo(
            @Path("username") String username,
            @Path("videoId") String videoId,
            @Header("Authorization") String token);

    @PUT("/api/users/{username}/videos/{videoId}")
    Call<UserVideo> updateVideo(
            @Header("Authorization") String token,
            @Path("username") String username,
            @Path("videoId") String videoId,
            @Body UserVideo userVideo
    );

    @GET("/api/users")
    Call<List<User>> getAllUsers();

    @POST("/api/users/{username}/videos/{videoId}/comments")
    Call<Comment> addComment(
            @Path("username") String username,
            @Path("videoId") String videoId,
            @Body CommentRequest commentRequest
    );

    @DELETE("/api/users/{publisherId}/videos/{vid}/comments/{cid}")
    Call<Void> deleteComment(
            @Path("publisherId") String publisherId,
            @Path("vid") String videoId,
            @Path("cid") String commentId,
            @Header("authorization") String authToken
    );

    @PUT("/api/users/{publisherId}/videos/{vid}/comments/{cid}")
    Call<ServerComment> updateComment(
            @Path("publisherId") String publisherId,
            @Path("vid") String videoId,
            @Path("cid") String commentId,
            @Body ServerComment updatedComment,
            @Header("authorization") String authToken
    );

    @GET("/api/users/{username}/videos/{videoId}/recommended")
    Call<List<Video>> getRecommendedVideos(
            @Path("username") String username,
            @Path("videoId") String videoId,
            @Header("authorization") String authToken
    );
}
