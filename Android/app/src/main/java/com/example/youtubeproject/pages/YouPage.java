package com.example.youtubeproject.pages;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.youtubeproject.adapters.VideosListAdapter;
import com.example.youtubeproject.entities.SessionManager;
import com.example.youtubeproject.entities.UserVideo;
import com.example.youtubeproject.entities.Video;
import com.example.youtubeproject.viewmodels.VideoViewModel;

import java.util.List;

public class YouPage extends AppCompatActivity {

    private TextView textViewWelcome;
    private TextView textViewMyVideos;
    private Button uploadButton;
    private Button logOutButton;
    private Button btnSignIn;
    private Button btnDetails;
    private ImageView profilePic;
    private VideoViewModel videoViewModel;
    private final SessionManager sessionManager = SessionManager.getInstance(); //made it not finale

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_page);

        textViewMyVideos = findViewById(R.id.userVideos);
        uploadButton = findViewById(R.id.btnAddVideo);
        logOutButton = findViewById(R.id.btnLogOut);
       btnDetails = findViewById(R.id.btnDetails);
        btnSignIn = findViewById(R.id.btnSignIn);
        profilePic = findViewById(R.id.profilePic);

        uploadButton.setOnClickListener(v -> {
            Intent i = new Intent(this, UploadVideoPage.class);
            startActivity(i);
        });

        btnSignIn.setOnClickListener(v -> {
            Intent i = new Intent(this, LogInPage.class);
            startActivity(i);
        });



        btnDetails.setOnClickListener(v -> {
            Intent i = new Intent(this, DetailsPage.class);
            startActivity(i);
        });

        logOutButton.setOnClickListener(v -> {
            sessionManager.setLogedIn(false);
            sessionManager.setLoggedUser(null);
            sessionManager.setToken(null);
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        });

        RecyclerView lstVideos = findViewById(R.id.lstMyVideos);
        final UserVideoListAdapter adapter = new UserVideoListAdapter(this);
        lstVideos.setAdapter(adapter);
        lstVideos.setLayoutManager(new LinearLayoutManager(this));

        if (sessionManager.isLogedIn()) {
            textViewWelcome = findViewById(R.id.textViewWelcomeUser);
            String username = sessionManager.getLoggedUser().getUsername();
            sessionManager.setUsernameInPage(username);
            textViewWelcome.setText("Welcome, " + username + "!");
            logOutButton.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.GONE);
            textViewMyVideos.setVisibility(View.VISIBLE);
            uploadButton.setVisibility(View.VISIBLE);
            profilePic.setVisibility(View.VISIBLE);
            btnDetails.setVisibility(View.VISIBLE);

            try {
                videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);

                videoViewModel.getUserVideos(username).observe(this, new Observer<List<UserVideo>>() {
                    @Override
                    public void onChanged(List<UserVideo> videos) {
                        if (videos != null) {
                            adapter.setVideos(videos);
                        } else {
                            Log.e("TAG", "Failed to fetch user videos");
                            Toast.makeText(YouPage.this, "Failed to fetch videos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                Log.e("TAG", "Exception in onCreate: ", e);
            }



             profilePic.setImageBitmap(base64ToBitmap(sessionManager.getLoggedUser().getProfilePic()));

        } else {
            logOutButton.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.VISIBLE);
            textViewMyVideos.setVisibility(View.GONE);
            uploadButton.setVisibility(View.GONE);
            profilePic.setVisibility(View.GONE);
             btnDetails.setVisibility(View.GONE);

        }

        ImageButton btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        });
    }

    public static Bitmap base64ToBitmap(String base64String) {
        try {
            // Remove the prefix if present
            if (base64String.startsWith("data:image/")) {
                base64String = base64String.substring(base64String.indexOf(",") + 1);
            }

            // Clean the base64 string by removing any extraneous characters or whitespace
            base64String = base64String.trim();
            base64String = base64String.replaceAll("\n", "").replaceAll("\r", "");

            // Decode the base64 string into bytes
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            // Convert the bytes into a bitmap
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            // Handle the case where the base64 string is invalid
            Log.e("TAG", "Invalid base64 string", e);
            return null;
        }
    }
}
