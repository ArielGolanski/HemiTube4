package com.example.youtubeproject;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.youtubeproject.converters.UserConverter;
import com.example.youtubeproject.converters.CommentListConverter;
import com.example.youtubeproject.dao.UserDao;
import com.example.youtubeproject.dao.VideoDao;
import com.example.youtubeproject.entities.User;
import com.example.youtubeproject.entities.Video;

@Database(entities = {User.class, Video.class}, version = 1, exportSchema = false)
@TypeConverters({UserConverter.class, CommentListConverter.class})
public abstract class AppDB extends RoomDatabase {

    private static volatile AppDB INSTANCE;

    public abstract UserDao userDao();
    public abstract VideoDao videoDao();

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Add any required initialization here
        }
    };

    public static AppDB getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDB.class, "app_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
