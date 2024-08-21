package com.example.youtubeproject.pages;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.youtubeproject.MainActivity;
import com.example.youtubeproject.R;
import com.example.youtubeproject.entities.SessionManager;
import com.example.youtubeproject.entities.User;
import com.example.youtubeproject.viewmodels.UserViewModel;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public class DetailsPage extends AppCompatActivity {

    private static final String TAG = "DetailsPage";
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profilePic;
    private TextView usernameTextView;
    private EditText nicknameEditText;
    private Button updateButton, deleteButton , backButton;
    private Uri profilePicUri;

    private UserViewModel userViewModel;
    private final SessionManager sessionManager = SessionManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);

        profilePic = findViewById(R.id.profilePic);
        usernameTextView = findViewById(R.id.username);
        nicknameEditText = findViewById(R.id.nickname);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        if (sessionManager.isLogedIn()) {
            User loggedUser = sessionManager.getLoggedUser();
            if (loggedUser.getProfilePic() != null && !loggedUser.getProfilePic().isEmpty()) {

                profilePicUri = Uri.parse(loggedUser.getProfilePic());

                Glide.with(this)
                        .load(profilePicUri)
                        .placeholder(R.drawable.ic_logo)
                        .error(R.drawable.ic_logo)
                        .into(profilePic);
            } else {
                Log.d("TAG", "Profile picture URL is null or empty, showing placeholder");
                profilePic.setImageResource(R.drawable.ic_logo);
            }
            usernameTextView.setText(loggedUser.getUsername());

            nicknameEditText.setText(loggedUser.getNickName());

        }

        profilePic.setOnClickListener(view -> openImagePicker());

        profilePic.setOnClickListener(view -> openImagePicker());

        backButton.setOnClickListener(v -> {
            Intent i = new Intent(this, YouPage.class);
            startActivity(i);
        });
        updateButton.setOnClickListener(view -> {
            String username = sessionManager.getLoggedUser().getUsername();
            String nickname = nicknameEditText.getText().toString();
            String profilePicBase64 = convertImageViewToBase64WithPrefix(profilePic);
            sessionManager.setProfilepic(profilePicBase64);
            sessionManager.setNickname(nickname);

            userViewModel.updateUser(username, profilePicBase64, nickname).observe(DetailsPage.this, new Observer<Void>() {
                @Override
                public void onChanged(Void aVoid) {
                    Toast.makeText(DetailsPage.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                }
            });
        });
        deleteButton.setOnClickListener(view -> {
            String username = sessionManager.getLoggedUser().getUsername();
            String token = sessionManager.getToken();

            userViewModel.deleteUser(username, token).observe(this, success -> {
                if (success) {
                    Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show();
                    sessionManager.setLogedIn(false);
                    sessionManager.setToken(null);
                    sessionManager.setLoggedUser(null);
                    // Navigate to login page
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                }
            });
        });

// Observe for user updates
        userViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                sessionManager.setLoggedUser(user);
                Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "User update failed", Toast.LENGTH_SHORT).show();
            }
        });

        userViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                sessionManager.setLoggedUser(user);
                Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "User update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            profilePicUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(profilePicUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                profilePic.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String convertImageViewToBase64WithPrefix(ImageView imageView) {
        // Extract Bitmap from ImageView
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();

        // Convert Bitmap to Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream); // Use PNG or JPEG as needed
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);

        // Add prefix to Base64 string
        return "data:image/png;base64," + base64String; // Adjust MIME type if needed
    }
}