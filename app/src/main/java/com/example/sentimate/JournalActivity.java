package com.example.sentimate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;

public class JournalActivity extends AppCompatActivity {

    private EditText entryEditText;
    private Button saveButton;
    private RecyclerView recyclerView;
    private List<JournalEntry> journalEntriesList;
    private JournalEntryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        entryEditText = findViewById(R.id.entryEditText);
        saveButton = findViewById(R.id.saveButton);
        recyclerView = findViewById(R.id.recyclerView);

        // Initialize the list and adapter
        journalEntriesList = new ArrayList<>();
        adapter = new JournalEntryAdapter(journalEntriesList);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Set up the save button click listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the journal entry text
                String entry = entryEditText.getText().toString().trim();
                if (!entry.isEmpty()) {
                    // Create a new journal entry with the current timestamp
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm a", Locale.getDefault());
                    String formattedTimestamp = sdf.format(new Date());
                    JournalEntry journalEntryObj = new JournalEntry(entry, formattedTimestamp);
                    // Add the entry to the list
                    journalEntriesList.add(0, journalEntryObj);
                    // Notify the adapter that data has changed
                    adapter.notifyDataSetChanged();
                    // Clear the EditText after saving the entry
                    entryEditText.setText("");
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        DatabaseReference entryRef = FirebaseDatabase.getInstance()
                                .getReference("users")
                                .child(user.getUid())
                                .child("journal_entries")
                                .push();

                        long timestamp = System.currentTimeMillis();
                        entryRef.child("text").setValue(entry);
                        entryRef.child("timestamp").setValue(timestamp);
                    }

                }
            }
        });
    }
}