package com.example.nikola.dotatracker.adapters;


import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nikola.dotatracker.R;
import com.example.nikola.dotatracker.data.DotaContract.DotaEntry;

public class CurAdapter extends RecyclerView.Adapter<CurAdapter.MyViewHolder> {

    private Cursor cursor;
    private OnSuggestionClickListener suggestionListener;

    public CurAdapter(Cursor cursor, OnSuggestionClickListener suggestionListener) {
        this.cursor = cursor;
        this.suggestionListener = suggestionListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_recent_entry, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (!cursor.moveToPosition(position))
            return;
        holder.bindType(cursor);
    }

    @Override
    public int getItemCount() {
        if (cursor == null)
            return 0;
        else
            return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public interface OnSuggestionClickListener {
        void onSuggestionClick(String query);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView entryTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            entryTextView = (TextView) itemView.findViewById(R.id.recEntryTextView);
            itemView.setOnClickListener(this);
        }

        public void bindType(Cursor cursor) {
            String entry = cursor.getString(cursor.getColumnIndex(DotaEntry.COLUMN_ENTRY));
            entryTextView.setText(entry);
        }

        @Override
        public void onClick(View v) {
            String query = entryTextView.getText().toString();
            suggestionListener.onSuggestionClick(query);
        }
    }
}
