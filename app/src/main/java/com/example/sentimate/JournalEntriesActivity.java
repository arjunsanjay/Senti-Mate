package com.example.sentimate;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JournalEntriesActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private FirebaseDatabase fDatabase;
    private TextView entriesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entries);

        entriesTextView = findViewById(R.id.entriesTextView);

        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();

        fetchJournalEntries();
    }

    private void fetchJournalEntries() {
        String userId = fAuth.getCurrentUser().getUid();
        DatabaseReference entriesRef = fDatabase.getReference("users").child(userId).child("journal_entries");

        entriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder entriesText = new StringBuilder();
                for (DataSnapshot entrySnapshot : snapshot.getChildren()) {
                    String entry = entrySnapshot.getValue(String.class);
                    if (entry != null) {
                        entriesText.append(entry).append("\n\n");
                    }
                }
                entriesTextView.setText(entriesText.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(JournalEntriesActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
