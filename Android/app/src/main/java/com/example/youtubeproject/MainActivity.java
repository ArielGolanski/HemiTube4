package com.example.youtubeproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeproject.adapters.VideosListAdapter;
import com.example.youtubeproject.entities.SessionManager;
import com.example.youtubeproject.entities.Video;
import com.example.youtubeproject.pages.YouPage;
import com.example.youtubeproject.viewmodels.VideoViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SwitchCompat switchMode;
    private boolean isNightMode;
    private SearchView searchView;
    private List<Video> filteredVideos = new ArrayList<>(); // Initialize the list
    private List<Video> videoslst = new ArrayList<>();

    private VideoViewModel videoViewModel;

    private final SessionManager sessionManager = SessionManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchMode = findViewById(R.id.switchMode);
        searchView = findViewById(R.id.searchView);

        isNightMode = sessionManager.isNightModeOn();

        if (isNightMode) {
            switchMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNightMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sessionManager.setNightModeOn(false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sessionManager.setNightModeOn(true);
                }
            }
        });

        RecyclerView lstVideos = findViewById(R.id.lstVideos);
        final VideosListAdapter adapter = new VideosListAdapter(this);
        lstVideos.setAdapter(adapter);
        lstVideos.setLayoutManager(new LinearLayoutManager(this));



        try {
            videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);

            videoViewModel.getVideos().observe(this, new Observer<List<Video>>() {
                @Override
                public void onChanged(List<Video> videos) {
                    if (videos != null) {
                        Log.d("TAG", "Videos fetched successfully: " + videos.size());
                        videoslst = videos;
                        adapter.setVideos(videoslst);
                    } else {
                        Log.e("TAG", "Failed to fetch videos");
                        Toast.makeText(MainActivity.this, "Failed to fetch videos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Log.e("TAG", "Exception in onCreate: ", e);
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


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // We handle the filtering as the text changes
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterVideos(newText, adapter);
                return true;
            }
        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        return;
    }


    private void filterVideos(String query, VideosListAdapter adapter) {
        filteredVideos.clear();
        if (query.isEmpty()) {
            filteredVideos.addAll(videoslst);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Video video : videoslst) {
                if (video.getTitle().toLowerCase().contains(lowerCaseQuery) || video.getPublisher().getUsername().toLowerCase().contains(lowerCaseQuery)) {
                    filteredVideos.add(video);
                }
            }
        }
        adapter.setVideos(filteredVideos);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MainActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MainActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MainActivity", "onStop");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity", "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("MainActivity", "onRestart");
    }
}





