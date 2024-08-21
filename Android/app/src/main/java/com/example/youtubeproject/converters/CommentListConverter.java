package com.example.youtubeproject.converters;

import androidx.room.TypeConverter;

import com.example.youtubeproject.entities.Comment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CommentListConverter {

    @TypeConverter
    public static String fromCommentList(List<Comment> comments) {
        if (comments == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Comment>>() {}.getType();
        return gson.toJson(comments, type);
    }

    @TypeConverter
    public static List<Comment> toCommentList(String commentsString) {
        if (commentsString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Comment>>() {}.getType();
        return gson.fromJson(commentsString, type);
    }
}
