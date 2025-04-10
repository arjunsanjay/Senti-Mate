package com.example.sentimate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    Button btnEditProfile, btnChangeTheme, btnNotifications, btnManageData, btnPrivacy;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnChangeTheme = findViewById(R.id.btnChangeTheme);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnManageData = findViewById(R.id.btnManageData);
        btnPrivacy = findViewById(R.id.btnPrivacy);

        // Click Listeners
        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(SettingsActivity.this, "Edit Profile Clicked!", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class)); // Uncomment when EditProfileActivity is implemented
        });

        btnChangeTheme.setOnClickListener(v -> {
            Toast.makeText(SettingsActivity.this, "Change Theme Clicked!", Toast.LENGTH_SHORT).show();
            // Code to toggle between Light/Dark theme
        });

        btnNotifications.setOnClickListener(v -> {
            Toast.makeText(SettingsActivity.this, "Notification Preferences Clicked!", Toast.LENGTH_SHORT).show();
        });

        btnManageData.setOnClickListener(v -> {
            Toast.makeText(SettingsActivity.this, "Manage Data Clicked!", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(SettingsActivity.this, BackupRestoreActivity.class)); // Uncomment when BackupRestoreActivity is implemented
        });

        btnPrivacy.setOnClickListener(v -> {
            Toast.makeText(SettingsActivity.this, "Privacy Settings Clicked!", Toast.LENGTH_SHORT).show();
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