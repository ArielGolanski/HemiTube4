package com.example.youtubeproject.pages;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.youtubeproject.R;
import com.example.youtubeproject.entities.User;
import com.example.youtubeproject.viewmodels.UserViewModel;
import com.example.youtubeproject.entities.SessionManager;

import java.io.ByteArrayOutputStream;

public class RegistrationPage extends AppCompatActivity {

    private EditText editTextPasswordRepeat, editTextNickname, editTextUsername, editTextPassword;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageViewProfilePicture;
    private Uri imageUri;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        editTextUsername = findViewById(R.id.editTextLogUpUsername);
        editTextPassword = findViewById(R.id.editTextLogUpPassword);
        editTextPasswordRepeat = findViewById(R.id.editTextLogUpPasswordRepeat);
        editTextNickname = findViewById(R.id.editTextLogUpNickname);
        imageViewProfilePicture = findViewById(R.id.imageViewProfilePicture);
        Button buttonSelectPhoto = findViewById(R.id.buttonSelectPhoto);
        Button btnLogUp = findViewById(R.id.btnLogUp);

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        buttonSelectPhoto.setOnClickListener(v -> openImagePicker());

        btnLogUp.setOnClickListener(view -> {
            if (validateInput()) {
                registerUser();
            }
        });

        userViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    // Save user ID and token to SessionManager
                    SessionManager.getInstance().setUserId(user.getId());
                    SessionManager.getInstance().setToken(user.getToken());
                    SessionManager.getInstance().setLoggedUser(user);

                    Toast.makeText(RegistrationPage.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RegistrationPage.this, LogInPage.class);
                    startActivity(i);
                } else {
                    Toast.makeText(RegistrationPage.this, "Registration Failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
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
            imageUri = data.getData();
            imageViewProfilePicture.setImageURI(imageUri);
        }
    }

    private boolean validateInput() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String repeatPassword = editTextPasswordRepeat.getText().toString().trim();
        String nickname = editTextNickname.getText().toString().trim();

        if (TextUtils.isEmpty(username) || username.length() < 4 || username.contains(" ")) {
            editTextUsername.setError("Username must be at least 4 characters long and contain no spaces");
            return false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 8 || !password.matches(".*[A-Z].*") ||
                !password.matches(".*[a-z].*") || !password.matches(".*\\d.*")) {
            editTextPassword.setError("Password must be at least 8 characters long, and include uppercase, lowercase and a digit");
            return false;
        }

        if (TextUtils.isEmpty(repeatPassword) || !password.equals(repeatPassword)) {
            editTextPasswordRepeat.setError("Passwords do not match!");
            return false;
        }

        if (TextUtils.isEmpty(nickname)) {
            editTextNickname.setError("You must enter a nickname!");
            return false;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Add a profile picture", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void registerUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String nickName = editTextNickname.getText().toString().trim();
        String profilePicBase64 = convertImageViewToBase64WithPrefix(imageViewProfilePicture);
        String tempId = "";

        User user = new User(tempId, username, nickName, password, profilePicBase64);
        userViewModel.registerUser(user);
    }

    public static String convertImageViewToBase64WithPrefix(ImageView imageView) {
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
