package com.example.youtubeproject.pages;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.youtubeproject.MainActivity;
import com.example.youtubeproject.R;
import com.example.youtubeproject.entities.SessionManager;
import com.example.youtubeproject.viewmodels.VideoViewModel;

import java.io.IOException;

public class UploadVideoPage extends AppCompatActivity {
    private static final int PICK_VIDEO_REQUEST = 1;
    private static final int PICK_THUMBNAIL_REQUEST = 2;
    private static final int REQUEST_PERMISSIONS = 123;

    private EditText videoTitle, videoDescription;
    private Button selectVideoButton, selectThumbnailButton, uploadButton, backButton;
    private ImageView thumbnailPreview;
    private ProgressBar progressBar;

    private Uri videoUri, thumbnailUri;
    private VideoViewModel videoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video_page);

        videoTitle = findViewById(R.id.videoTitle);
        videoDescription = findViewById(R.id.videoDescription);
        selectVideoButton = findViewById(R.id.uploadVideoSelectVideoBtn);
        selectThumbnailButton = findViewById(R.id.uploadVidSelectPhotoBtn);
        uploadButton = findViewById(R.id.btnUpload);
        backButton = findViewById(R.id.backButton);
        thumbnailPreview = findViewById(R.id.imageViewVideoPic);
        progressBar = findViewById(R.id.progressBar);

        videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);

        selectVideoButton.setOnClickListener(v -> openVideoChooser());
        selectThumbnailButton.setOnClickListener(v -> openThumbnailChooser());
        uploadButton.setOnClickListener(v -> uploadVideo());
        backButton.setOnClickListener(v -> onBackPressed());

        videoViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        videoViewModel.getUploadResult().observe(this, result -> {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions();
        }
    }

    private void requestPermissions() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted
                Log.d("TAG", "Permissions granted");
            } else {
                // Permissions denied
                Log.d("TAG", "Permissions denied");
                Toast.makeText(this, "Permissions denied. App cannot function without the required permissions.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openVideoChooser() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
    }

    private void openThumbnailChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Thumbnail"), PICK_THUMBNAIL_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == PICK_VIDEO_REQUEST) {
                videoUri = data.getData();
                VideoView videoView = findViewById(R.id.videoViewVideo);
                videoView.setVideoURI(videoUri);
                videoView.start();
            } else if (requestCode == PICK_THUMBNAIL_REQUEST) {
                thumbnailUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), thumbnailUri);
                    thumbnailPreview.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Log.e("TAG", "Error loading thumbnail", e);
                }
            }
        }
    }

    private void uploadVideo() {
        String title = videoTitle.getText().toString();
        String description = videoDescription.getText().toString();
        String publisher = SessionManager.getInstance().getLoggedUser().getId(); // Assuming this method retrieves the publisher ID

        if (videoUri != null && thumbnailUri != null) {

            // Create the observer
            Observer<String> uploadObserver = new Observer<String>() {
                @Override
                public void onChanged(String uploadResult) {
                    if ("Video uploaded successfully!".equals(uploadResult)) { // Match the exact string
                        Toast.makeText(UploadVideoPage.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UploadVideoPage.this, YouPage.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(UploadVideoPage.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                    // Remove the observer to avoid multiple triggers
                    videoViewModel.getUploadResult().removeObserver(this);
                }
            };

            // Observe the upload result
            videoViewModel.getUploadResult().observe(this, uploadObserver);

            // Call the upload method
            videoViewModel.uploadVideo(videoUri, thumbnailUri, title, description, publisher, this);
        } else {
            Toast.makeText(this, "Please select both video and thumbnail", Toast.LENGTH_SHORT).show();
        }
    }
}


