package com.example.sentimate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JournalActivity extends AppCompatActivity {
    EditText journalEntry;
    Button btnSaveEntry;
    private FirebaseDatabase fDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        journalEntry = findViewById(R.id.journalEntry);
        btnSaveEntry = findViewById(R.id.btnSaveEntry);

        fDatabase = FirebaseDatabase.getInstance();

        btnSaveEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entry = journalEntry.getText().toString();
                if (!entry.isEmpty()) {
                    // Generate a unique ID for each entry
                    String entryId = String.valueOf(System.currentTimeMillis());

                    // Save journal entry to Firebase Realtime Database
                    DatabaseReference entriesRef = fDatabase.getReference("journal_entries").child(entryId);
                    entriesRef.setValue(entry);

                    Toast.makeText(JournalActivity.this, "Entry Saved!", Toast.LENGTH_SHORT).show();
                    journalEntry.setText(""); // Clear the entry field
                } else {
                    Toast.makeText(JournalActivity.this, "Please write something!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
