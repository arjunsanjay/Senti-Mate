package com.example.sentimate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sentimate.InsightsActivity;
import com.example.sentimate.JournalActivity;
import com.example.sentimate.SettingsActivity;

public class HomeActivity extends AppCompatActivity {
    Button btnJournal, btnInsights, btnSettings, btnViewEntries;
    TextView journalEntriesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnJournal = findViewById(R.id.btnJournal);
        btnInsights = findViewById(R.id.btnInsights);
        btnSettings = findViewById(R.id.btnSettings);
        btnViewEntries = findViewById(R.id.btnViewEntries);
        journalEntriesTextView = findViewById(R.id.journalEntriesTextView);

        btnJournal.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, JournalActivity.class)));
        btnInsights.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, InsightsActivity.class)));
        btnSettings.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, SettingsActivity.class)));
        btnViewEntries.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, JournalEntriesActivity.class);
            startActivity(intent);
        });

        fetchJournalEntries();
    }

    private void fetchJournalEntries() {
        // Fetch and display recent journal entries here
        // For simplicity, this example just displays a placeholder text
        journalEntriesTextView.setText("Recent Journal Entries will be displayed here.");
    }
}
