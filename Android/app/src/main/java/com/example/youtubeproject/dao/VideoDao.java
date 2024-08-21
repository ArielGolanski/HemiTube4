package com.example.youtubeproject.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.youtubeproject.entities.Video;

import java.util.List;

@Dao
public interface VideoDao {
    @Insert
    void insertVideo(Video video);

    @Query("SELECT * FROM videos")
    LiveData<List<Video>> getAllVideos();

}

