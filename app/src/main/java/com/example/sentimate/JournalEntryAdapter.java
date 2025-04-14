package com.example.sentimate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class JournalEntryAdapter extends RecyclerView.Adapter<JournalEntryAdapter.ViewHolder> {

    private List<JournalEntry> entries;

    public JournalEntryAdapter(List<JournalEntry> entries) {
        this.entries = entries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.journal_entry_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JournalEntry entry = entries.get(position);
        holder.textEntry.setText(entry.getText());
        holder.textTimestamp.setText(entry.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textEntry, textTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textEntry = itemView.findViewById(R.id.textEntry);
            textTimestamp = itemView.findViewById(R.id.textTimestamp);
        }
    }
}
