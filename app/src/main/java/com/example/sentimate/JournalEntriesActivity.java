package com.example.sentimate;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.*;

public class JournalEntriesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JournalEntryAdapter adapter;
    private List<JournalEntry> entryList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entries);

        recyclerView = findViewById(R.id.recyclerViewEntries);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new JournalEntryAdapter(entryList);
        recyclerView.setAdapter(adapter);

        // 🟢 Set up FAB here
        FloatingActionButton fabAddEntry = findViewById(R.id.fabAddEntry);
        fabAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JournalEntriesActivity.this, JournalActivity.class);
                startActivity(intent);
            }
        });

        fetchAllJournalEntries();
    }

    private void fetchAllJournalEntries() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        String userId = currentUser.getUid();
        DatabaseReference entriesRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("journal_entries");

        entriesRef.orderByChild("timestamp").get()
                .addOnSuccessListener(snapshot -> {
                    entryList.clear();
                    for (DataSnapshot entrySnapshot : snapshot.getChildren()) {
                        String text = entrySnapshot.child("text").getValue(String.class);
                        Long timestamp = entrySnapshot.child("timestamp").getValue(Long.class);
                        if (text != null && timestamp != null) {
                            // Format the timestamp into a human-readable string
                            String formattedTimestamp = formatTimestamp(timestamp);
                            entryList.add(new JournalEntry(text, formattedTimestamp));
                        }
                    }
                    Collections.reverse(entryList); // Most recent first
                    adapter.notifyDataSetChanged();
                });
    }

    private String formatTimestamp(Long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        return dateFormat.format(date);
    }
}
