package com.example.youtubeproject.pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeproject.MainActivity;
import com.example.youtubeproject.R;
import com.example.youtubeproject.adapters.UserVideoListAdapter;
import com.example.youtubeproject.entities.SessionManager;
import com.example.youtubeproject.entities.UserVideo;
import com.example.youtubeproject.viewmodels.VideoViewModel;

import java.util.List;

public class UserVideos extends AppCompatActivity {

    private VideoViewModel videoViewModel;
    private UserVideoListAdapter adapter;
    private TextView textViewUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_videos_page);

        textViewUsername = findViewById(R.id.textViewUserVideos);

        RecyclerView lstVideos = findViewById(R.id.lstUserVideos);
        adapter = new UserVideoListAdapter(this);
        lstVideos.setAdapter(adapter);
        lstVideos.setLayoutManager(new LinearLayoutManager(this));

        String username = getIntent().getStringExtra("username");
        SessionManager.getInstance().setUsernameInPage(username);
        if (username != null) {
            textViewUsername.setText(username + " Videos:");
            videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);

            videoViewModel.getUserVideos(username).observe(this, new Observer<List<UserVideo>>() {
                @Override
                public void onChanged(List<UserVideo> videos) {
                    if (videos != null) {
                        adapter.setVideos(videos);
                    } else {
                        Log.e("TAG", "Failed to fetch user videos");
                        Toast.makeText(UserVideos.this, "Failed to fetch videos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Log.e("TAG", "Username not provided");
            Toast.makeText(this, "Username not provided", Toast.LENGTH_SHORT).show();
        }

        ImageButton btnYou = findViewById(R.id.btnYou);
        btnYou.setOnClickListener(v -> {
            Intent i = new Intent(this, YouPage.class);
            startActivity(i);
        });


        ImageButton btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        });

    }


}
