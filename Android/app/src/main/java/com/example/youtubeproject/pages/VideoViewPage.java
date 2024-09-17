package com.example.youtubeproject.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeproject.MainActivity;
import com.example.youtubeproject.R;
import com.example.youtubeproject.adapters.CommentsListAdapter;
import com.example.youtubeproject.adapters.VideosListAdapter;
import com.example.youtubeproject.api.CommentRequest;
import com.example.youtubeproject.entities.Comment;
import com.example.youtubeproject.entities.SessionManager;
import com.example.youtubeproject.entities.UserVideo;
import com.example.youtubeproject.entities.Video;
import com.example.youtubeproject.viewmodels.UserViewModel;
import com.example.youtubeproject.viewmodels.VideoViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;

public class VideoViewPage extends AppCompatActivity {

    private VideoView videoView;
    private CommentsListAdapter adapter;
    private VideoViewModel videoViewModel;
    private UserViewModel userViewModel;
    private UserVideo userVideo;
    private String uploaderUsername;
    private final SessionManager sessionManager = SessionManager.getInstance();

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view_page);

        Intent intent = getIntent();
        String[] data = intent.getStringArrayExtra("data");
        MediaController mediaController = new MediaController(this);

        videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        uploaderUsername = data[1];

        videoViewModel.getVideo(data[1], data[0]).observe(this, new Observer<UserVideo>() {
            @Override
            public void onChanged(UserVideo receivedUserVideo) {
                if (receivedUserVideo != null) {
                    userVideo = receivedUserVideo;
                    TextView title = findViewById(R.id.videoTitle);
                    TextView content = findViewById(R.id.videoContent);
                    TextView uploader = findViewById(R.id.videoUploader);
                    TextView views = findViewById(R.id.videoViews);
                    TextView timePassed = findViewById(R.id.videoPassedTime);

                    title.setText(userVideo.getTitle());
                    content.setText(userVideo.getDescription());
                    uploader.setText(data[1] + " . ");
                    views.setText(userVideo.get__v() + " views . ");
                    String formattedDate = convertDate(userVideo.getUploadDate());
                    timePassed.setText(formattedDate);

                    RecyclerView lstComments = findViewById(R.id.lstComments);
                    adapter = new CommentsListAdapter(VideoViewPage.this, userViewModel, VideoViewPage.this, userVideo.getId(), userVideo.getPublisher());
                    lstComments.setAdapter(adapter);
                    lstComments.setLayoutManager(new LinearLayoutManager(VideoViewPage.this));

                    List<Comment> comments = userVideo.getComments();
                    adapter.setComments(comments);

                    uploader.setOnClickListener(v -> {
                        Intent intent = new Intent(VideoViewPage.this, UserVideos.class);
                        intent.putExtra("username", data[1]);
                        SessionManager.getInstance().setUsernameInPage(data[1]);
                        startActivity(intent);
                    });

                    videoView = findViewById(R.id.videoPlayer);
                    mediaController.setMediaPlayer(videoView);
                    videoView.setMediaController(mediaController);
                    videoView.setVideoPath("http://10.0.2.2:5001/uploads/" + userVideo.getFileName());
                    videoView.start();

                    setupButtons();
                } else {
                    Log.e("VideoViewPage", "Failed to receive video data");
                }
            }
        });


        RecyclerView lstVideos = findViewById(R.id.lstRecommendedVideos);
        final VideosListAdapter adapter = new VideosListAdapter(this);
        lstVideos.setAdapter(adapter);
        lstVideos.setLayoutManager(new LinearLayoutManager(this));



        try {
            videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);

            if(sessionManager.isLogedIn()){
                videoViewModel.getRecommendedVideos(sessionManager.getLoggedUser().getUsername(), data[0]).observe(this, new Observer<List<Video>>() {
                    @Override
                    public void onChanged(List<Video> videos) {
                        if (videos != null) {
                            Log.d("TAG", "Videos fetched successfully: " + videos.size());
                            adapter.setVideos(videos);
                        } else {
                            Log.e("TAG", "Failed to fetch videos");
                            Toast.makeText(VideoViewPage.this, "Failed to fetch videos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else{
                videoViewModel.getVideos().observe(this, new Observer<List<Video>>() {
                    @Override
                    public void onChanged(List<Video> videos) {
                        if (videos != null) {
                            Log.d("TAG", "Videos fetched successfully: " + videos.size());
                            adapter.setVideos(videos);
                        } else {
                            Log.e("TAG", "Failed to fetch videos");
                            Toast.makeText(VideoViewPage.this, "Failed to fetch videos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (Exception e) {
            Log.e("TAG", "Exception in onCreate: ", e);
        }

        ImageButton btnLike = findViewById(R.id.btnLike);
        btnLike.setOnClickListener(v -> {
            btnLike.setImageResource(R.drawable.ic_pushed_like);
        });

        ImageButton btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(v -> {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        });

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

        ImageButton btnComment = findViewById(R.id.btnComment);
        btnComment.setOnClickListener(v -> {
            if (sessionManager.isLogedIn()) {
                showCommentDialog();
            } else {
                Toast.makeText(this, "In Order To Comment You Must Log-In", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupButtons() {
        Button btnDelete = findViewById(R.id.videoDeleteBtn);
        Button btnEdit = findViewById(R.id.videoEditBtn);

        if (sessionManager.isLogedIn() && userVideo != null &&
                sessionManager.getLoggedUser().getId().equals(userVideo.getPublisher())) {
            btnDelete.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);

            btnDelete.setOnClickListener(v -> deleteVideo());
            btnEdit.setOnClickListener(v -> showEditDialog());
        } else {
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        }
    }

    private void deleteVideo() {
        videoViewModel.deleteVideo(sessionManager.getLoggedUser().getUsername(), userVideo.getId()).observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(this, "Video deleted successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, YouPage.class);
                startActivity(i);
            }
        });
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit, null);
        builder.setView(dialogView);

        EditText videoTitleInput = dialogView.findViewById(R.id.editVideoTitle);
        EditText videoContentInput = dialogView.findViewById(R.id.editVideoContent);

        videoTitleInput.setText(userVideo.getTitle());
        videoContentInput.setText(userVideo.getDescription());

        builder.setPositiveButton("Edit", null);
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                String titleText = videoTitleInput.getText().toString().trim();
                String contentText = videoContentInput.getText().toString().trim();

                if (titleText.isEmpty() || contentText.isEmpty()) {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    updateVideoDetails(titleText, contentText);
                    dialog.dismiss();
                    Intent i = new Intent(this, YouPage.class);
                    startActivity(i);
                }
            });
        });

        dialog.show();
    }

    private void showCommentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_comment, null))
                .setPositiveButton("Comment", (dialog, id) -> {
                    AlertDialog alertDialog = (AlertDialog) dialog;
                    EditText commentInput = alertDialog.findViewById(R.id.editComment);
                    String commentText = commentInput.getText().toString().trim();
                    if (!commentText.isEmpty()) {
                        String userId = sessionManager.getLoggedUser().getId();
                        String username = sessionManager.getLoggedUser().getUsername();
                        CommentRequest commentRequest = new CommentRequest(commentText, userId, username);

                        // Add the comment and observe the response
                        videoViewModel.addComment(userVideo.getPublisher(), userVideo.getId(), commentRequest).observe(this, new Observer<Comment>() {
                            @Override
                            public void onChanged(Comment comment) {
                                if (comment != null) {
                                    runOnUiThread(() -> {
                                        Toast.makeText(VideoViewPage.this, "Comment added", Toast.LENGTH_SHORT).show();
                                        adapter.addComment(comment);
                                    });
                                } else {
                                    runOnUiThread(() -> {
                                        Toast.makeText(VideoViewPage.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
                                    });
                                }
                            }
                        });
                    } else {
                        Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        builder.create().show();
    }

    private void updateVideoDetails(String titleText, String contentText) {
        String token = sessionManager.getToken();
        userVideo.setTitle(titleText);
        userVideo.setDescription(contentText);

        videoViewModel.updateVideo(token, userVideo.getPublisher(), userVideo).observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(this, "Video updated successfully", Toast.LENGTH_SHORT).show();
                recreate();
            }
        });
    }

    private String convertDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            Date date = inputFormat.parse(dateString);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        return;
    }
}
