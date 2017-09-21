package com.example.nikola.dotatracker.btmnavigation;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.nikola.dotatracker.R;
import com.example.nikola.dotatracker.adapters.CurAdapter;
import com.example.nikola.dotatracker.data.DotaContract.DotaFollowing;
import com.example.nikola.dotatracker.details.PlayerDetailActivity;
import com.example.nikola.dotatracker.interfaces.OnItemViewClickListener;

import static com.example.nikola.dotatracker.SearchActivity.INTENT_EXTRA_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnItemViewClickListener {
    private CurAdapter mAdapter;
    private static final String LOG_TAG = FollowingFragment.class.getSimpleName();

    public FollowingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v(LOG_TAG, "fragmentFollowing onCreateView");
        View view = inflater.inflate(R.layout.fragment_following, container, false);
        mAdapter = new CurAdapter(this, getContext(),null, Glide.with(this));
        RecyclerView recView = (RecyclerView) view.findViewById(R.id.fragment_following_recView);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        recView.setAdapter(mAdapter);
        recView.setHasFixedSize(true);
        getLoaderManager().initLoader(0, null, this);
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getContext(),
                DotaFollowing.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemViewClick(long playerId) {
        Intent intent = new Intent(getContext(), PlayerDetailActivity.class);
        intent.putExtra(INTENT_EXTRA_KEY, String.valueOf(playerId));
        startActivity(intent);
    }
}
