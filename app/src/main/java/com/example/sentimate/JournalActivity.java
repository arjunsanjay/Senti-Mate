package com.example.sentimate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

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
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        entryEditText = findViewById(R.id.entryEditText);
        saveButton = findViewById(R.id.saveButton);
        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.journalToolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        journalEntriesList = new ArrayList<>();
        adapter = new JournalEntryAdapter(journalEntriesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entry = entryEditText.getText().toString().trim();
                if (!entry.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm a", Locale.getDefault());
                    String formattedTimestamp = sdf.format(new Date());
                    JournalEntry journalEntryObj = new JournalEntry(entry, formattedTimestamp);

                    journalEntriesList.add(0, journalEntryObj);
                    adapter.notifyDataSetChanged();
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

    // ✅ This method must be outside onCreate
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
