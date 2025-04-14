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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    Button btnJournal, btnInsights, btnSettings, btnViewEntries;
    TextView journalEntriesTextView;
    FloatingActionButton fabWriteEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fabWriteEntry = findViewById(R.id.fabWriteEntry);
        btnInsights = findViewById(R.id.btnInsights);
        btnSettings = findViewById(R.id.btnSettings);
        btnViewEntries = findViewById(R.id.btnViewEntries);
        journalEntriesTextView = findViewById(R.id.journalEntriesTextView);
        fabWriteEntry.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, JournalActivity.class);
            startActivity(intent);
        });

        btnViewEntries.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, JournalEntriesActivity.class);
            startActivity(intent);
        });
        btnInsights.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, InsightsActivity.class)));
        btnSettings.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, SettingsActivity.class)));

        fetchJournalEntries();
    }

    private void fetchJournalEntries() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            journalEntriesTextView.setText("User not logged in!");
            return;
        }

        String userId = currentUser.getUid();
        DatabaseReference entriesRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("journal_entries");

        entriesRef.orderByChild("timestamp").limitToLast(3)
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<DataSnapshot> entriesList = new ArrayList<>();
                    for (DataSnapshot entrySnapshot : snapshot.getChildren()) {
                        entriesList.add(entrySnapshot);
                    }

                    // Reverse to show most recent entry at the top
                    Collections.reverse(entriesList);

                    StringBuilder entriesText = new StringBuilder();
                    for (DataSnapshot entrySnapshot : entriesList) {
                        String entry = entrySnapshot.child("text").getValue(String.class);
                        Long timestamp = entrySnapshot.child("timestamp").getValue(Long.class);

                        // Format: dd/MM/yy hh:mm a (e.g. 13/04/25 02:15 PM)
                        String formattedTime = android.text.format.DateFormat
                                .format("dd/MM/yy hh:mm a", new java.util.Date(timestamp))
                                .toString();

                        entriesText.append("• ")
                                .append(entry)
                                .append("\n🕒 ")
                                .append(formattedTime)
                                .append("\n\n");
                    }

                    if (entriesText.length() == 0) {
                        journalEntriesTextView.setText("No entries yet.");
                    } else {
                        journalEntriesTextView.setText(entriesText.toString());
                    }
                })
                .addOnFailureListener(e -> {
                    journalEntriesTextView.setText("Failed to load entries: " + e.getMessage());
                });
    }




}
