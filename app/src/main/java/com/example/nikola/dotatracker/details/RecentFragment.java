package com.example.nikola.dotatracker.details;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.nikola.dotatracker.MainActivity;
import com.example.nikola.dotatracker.R;
import com.example.nikola.dotatracker.adapters.RecAdapter;
import com.example.nikola.dotatracker.interfaces.MyListItem;
import com.example.nikola.dotatracker.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<MyListItem>> {
    private static final String LOG_TAG = RecentFragment.class.getSimpleName();

    private String playerId;
    private RecAdapter adapter;
    private ProgressBar pBar;
    private SwipeRefreshLayout swipeRrefresh;

    public RecentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "RECENT FRAGMENT: onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "RECENT FRAGMENT: onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(LOG_TAG, "RECENT FRAGMENT: onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(LOG_TAG, "RECENT FRAGMENT: onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "RECENT FRAGMENT: onDestroy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v(LOG_TAG, "RECENT FRAGMENT: onCreateView");
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        swipeRrefresh = (SwipeRefreshLayout) view.findViewById(R.id.recent_swipe_refresh);
        swipeRrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLoaderManager().restartLoader(2, null, RecentFragment.this);
            }
        });
        pBar = (ProgressBar) view.findViewById(R.id.recent_progress_bar);
        playerId = getActivity().getIntent().getStringExtra(MainActivity.INTENT_EXTRA_KEY);
        adapter = new RecAdapter(getContext(), Glide.with(this), new ArrayList<MyListItem>());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recent_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        pBar.setVisibility(View.VISIBLE);
        getLoaderManager().initLoader(2, null, this);


        return view;
    }

    @Override
    public Loader<List<MyListItem>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "FRAGMENTLOG RECENT: onCreateLoader");
        return new RecentMatchesAsync(getContext(), playerId);
    }

    @Override
    public void onLoadFinished(Loader<List<MyListItem>> loader, List<MyListItem> data) {
        pBar.setVisibility(View.GONE);
        adapter.addList(data);
        swipeRrefresh.setRefreshing(false);
        Log.v(LOG_TAG, "FRAGMENTLOG RECENT: onLoadFinished");

    }

    @Override
    public void onLoaderReset(Loader<List<MyListItem>> loader) {

    }

    private static class RecentMatchesAsync extends AsyncTaskLoader<List<MyListItem>> {
        private String playerId;
        private List<MyListItem> mData;

        public RecentMatchesAsync(Context context, String playerId) {
            super(context);
            this.playerId = playerId;
        }


        @Override
        public List<MyListItem> loadInBackground() {
            Log.v(LOG_TAG, "FRAGMENTLOG RECENT: loadInBackground");
            return NetworkUtils.fetchRecentMatchesData(playerId);
        }

        @Override
        protected void onStartLoading() {
            if (mData != null) {
                deliverResult(mData);
            } else {
                forceLoad();
            }
        }

        @Override
        public void deliverResult(List<MyListItem> data) {
            mData = data;
            super.deliverResult(data);
        }
    }


}
