package com.example.nikola.dotatracker.adapters;


import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.nikola.dotatracker.R;
import com.example.nikola.dotatracker.data.DotaContract.DotaEntry;
import com.example.nikola.dotatracker.data.DotaContract.DotaFollowing;
import com.example.nikola.dotatracker.interfaces.TableType;

import de.hdodenhof.circleimageview.CircleImageView;


public class CurAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Cursor cursor;
    private OnSuggestionClickListener suggestionListener;
    private RequestManager glide;

    public CurAdapter(Cursor cursor, OnSuggestionClickListener suggestionListener) {
        this.cursor = cursor;
        this.suggestionListener = suggestionListener;
    }

    public CurAdapter(Cursor cursor, RequestManager glide) {
        this.cursor = cursor;
        this.glide = glide;
    }

    @Override
    public int getItemViewType(int position) {
        cursor.moveToPosition(position);
        return cursor.getInt(cursor.getColumnIndex(TableType.TYPE));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TableType.SUGGESTION_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_recent_entry, parent, false);
                return new SuggestionViewHolder(view);
            case TableType.FOLLOWING_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
                return new PlayerViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position))
            return;
        switch (holder.getItemViewType()) {
            case TableType.SUGGESTION_TYPE:
                ((SuggestionViewHolder) holder).bindType();
                break;
            case TableType.FOLLOWING_TYPE:
                ((PlayerViewHolder) holder).bindType();
                break;
        }

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

    public class SuggestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView entryTextView;

        public SuggestionViewHolder(View itemView) {
            super(itemView);
            entryTextView = (TextView) itemView.findViewById(R.id.recEntryTextView);
            itemView.setOnClickListener(this);
        }

        public void bindType() {
            String entry = cursor.getString(cursor.getColumnIndex(DotaEntry.COLUMN_ENTRY));
            entryTextView.setText(entry);
        }

        @Override
        public void onClick(View v) {
            String query = entryTextView.getText().toString();
            suggestionListener.onSuggestionClick(query);
        }
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvId;
        private CircleImageView ivImage;
        private Button followButton;

        public PlayerViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_userName);
            tvId = (TextView) itemView.findViewById(R.id.tv_userId);
            ivImage = (CircleImageView) itemView.findViewById(R.id.iv_userImage);
            followButton = (Button) itemView.findViewById(R.id.follow_button);

        }

        private void bindType() {
            tvName.setText(cursor.getString(cursor.getColumnIndex(DotaFollowing.COLUMN_NAME)));
            tvId.setText(cursor.getString(cursor.getColumnIndex(DotaFollowing.COLUMN_PLAYER_ID)));
            glide.load(cursor.getString(cursor.getColumnIndex(DotaFollowing.COLUMN_IMAGE_URL))).into(ivImage);
            followButton.setVisibility(View.GONE);

        }
    }
}
