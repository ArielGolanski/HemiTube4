package com.example.youtubeproject.pages;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.youtubeproject.MainActivity;
import com.example.youtubeproject.R;
import com.example.youtubeproject.entities.SessionManager;
import com.example.youtubeproject.entities.User;
import com.example.youtubeproject.viewmodels.UserViewModel;

public class LogInPage extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        editTextUsername = findViewById(R.id.editTextLogInUsername);
        editTextPassword = findViewById(R.id.editTextLogInPassword);
        Button btnLogIn = findViewById(R.id.btnLogIn);
        Button btnLogUp = findViewById(R.id.btnJoin);


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        btnLogIn.setOnClickListener(view -> {
            if (validateInput()) {
                loginUser();
            }
        });

        btnLogUp.setOnClickListener(v -> {
            Intent i = new Intent(this, RegistrationPage.class);
            startActivity(i);
        });

        userViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    String userId = user.getId();
                    String token = user.getToken();
                    SessionManager.getInstance().setUserId(userId);
                    SessionManager.getInstance().setToken(token);
                    user.setToken(token);


                    Toast.makeText(LogInPage.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LogInPage.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LogInPage.this, "Login Failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateInput() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Username is required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required");
            return false;
        }

        return true;
    }

    private void loginUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        User user = new User(username, password);
        userViewModel.loginUser(user);
        userViewModel.getUser(username).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    SessionManager.getInstance().setLogedIn(true);
                } else {
                    System.exit(1);
                }
            }
        });
    }

}
