package com.example.sentimate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    Button btnEditProfile, btnChangeTheme, btnNotifications, btnManageData, btnPrivacy, btnGoToLogin;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //btnEditProfile = findViewById(R.id.btnEditProfile);
        btnChangeTheme = findViewById(R.id.btnChangeTheme);
        //btnNotifications = findViewById(R.id.btnNotifications);
        //btnManageData = findViewById(R.id.btnManageData);
       // btnPrivacy = findViewById(R.id.btnPrivacy);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        // Click Listeners


        btnChangeTheme.setOnClickListener(v -> {
            Toast.makeText(SettingsActivity.this, "Change Theme Clicked!", Toast.LENGTH_SHORT).show();
            // Code to toggle between Light/Dark theme
        });
        btnGoToLogin.setOnClickListener(v -> {
            // Clear user session data
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // This clears all shared preferences (e.g., user info, authentication data, etc.)
            editor.apply();

            // Optionally, show a Toast message to indicate successful logout
            Toast.makeText(SettingsActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();

            // Start MainActivity (Login screen)
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close SettingsActivity so the user cannot go back
        });




        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("DarkMode", false);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_settings);

        btnChangeTheme = findViewById(R.id.btnChangeTheme);

        btnChangeTheme.setOnClickListener(v -> toggleTheme());
    }

    private void toggleTheme() {
        boolean isDarkMode = sharedPreferences.getBoolean("DarkMode", false);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putBoolean("DarkMode", false);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putBoolean("DarkMode", true);
        }
        editor.apply();

        // Restart activity to apply theme change
        recreate();
    }
}