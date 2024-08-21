package com.example.youtubeproject.converters;

import androidx.room.TypeConverter;

import com.example.youtubeproject.entities.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class UserConverter {

    @TypeConverter
    public static String fromUser(User user) {
        if (user == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(user);
    }

    @TypeConverter
    public static User toUser(String userString) {
        if (userString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<User>() {}.getType();
        return gson.fromJson(userString, type);
    }
}
