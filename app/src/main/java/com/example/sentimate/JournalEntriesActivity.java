package com.example.sentimate;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    private List<JournalEntry> filteredList = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entries);

        recyclerView = findViewById(R.id.recyclerViewEntries);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new JournalEntryAdapter(filteredList);
        recyclerView.setAdapter(adapter);
        toolbar = findViewById(R.id.journalToolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FloatingActionButton fabAddEntry = findViewById(R.id.fabAddEntry);
        fabAddEntry.setOnClickListener(v -> {
            Intent intent = new Intent(JournalEntriesActivity.this, JournalActivity.class);
            startActivity(intent);
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
                            String formattedTimestamp = formatTimestamp(timestamp);
                            entryList.add(new JournalEntry(text, formattedTimestamp));
                        }
                    }
                    Collections.reverse(entryList);
                    filteredList.clear();
                    filteredList.addAll(entryList);
                    adapter.notifyDataSetChanged();
                });
    }

    private String formatTimestamp(Long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        return dateFormat.format(date);
    }

    private void filterByDate(int year, int month, int day) {
        Calendar selected = Calendar.getInstance();
        selected.set(year, month, day);

        List<JournalEntry> result = new ArrayList<>();
        for (JournalEntry entry : entryList) {
            try {
                Date entryDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).parse(entry.getTimestamp());
                Calendar entryCal = Calendar.getInstance();
                entryCal.setTime(entryDate);

                if (entryCal.get(Calendar.YEAR) == year &&
                        entryCal.get(Calendar.MONTH) == month &&
                        entryCal.get(Calendar.DAY_OF_MONTH) == day) {
                    result.add(entry);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        filteredList.clear();
        filteredList.addAll(result);
        adapter.notifyDataSetChanged();
    }

    private void filterByWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date startOfWeek = cal.getTime();

        List<JournalEntry> result = new ArrayList<>();
        for (JournalEntry entry : entryList) {
            try {
                Date entryDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).parse(entry.getTimestamp());
                if (!entryDate.before(startOfWeek)) {
                    result.add(entry);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        filteredList.clear();
        filteredList.addAll(result);
        adapter.notifyDataSetChanged();
    }

    private void filterByMonth() {
        Calendar now = Calendar.getInstance();
        int currentMonth = now.get(Calendar.MONTH);
        int currentYear = now.get(Calendar.YEAR);

        List<JournalEntry> result = new ArrayList<>();
        for (JournalEntry entry : entryList) {
            try {
                Date entryDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).parse(entry.getTimestamp());
                Calendar entryCal = Calendar.getInstance();
                entryCal.setTime(entryDate);

                if (entryCal.get(Calendar.YEAR) == currentYear && entryCal.get(Calendar.MONTH) == currentMonth) {
                    result.add(entry);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        filteredList.clear();
        filteredList.addAll(result);
        adapter.notifyDataSetChanged();
    }

    private void resetFilter() {
        filteredList.clear();
        filteredList.addAll(entryList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journal_entries_menu, menu);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            SpannableString s = new SpannableString(menuItem.getTitle());
            s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), 0);
            menuItem.setTitle(s);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter_date) {
            showDatePicker();
            return true;
        } else if (id == R.id.action_filter_week) {
            filterByWeek();
            return true;
        } else if (id == R.id.action_filter_month) {
            filterByMonth();
            return true;
        } else if (id == R.id.action_reset_filter) {
            resetFilter();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            filterByDate(year, month, dayOfMonth);
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}