package com.example.nikola.dotatracker.details;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.nikola.dotatracker.R;
import com.example.nikola.dotatracker.SearchActivity;
import com.example.nikola.dotatracker.adapters.RecAdapter;
import com.example.nikola.dotatracker.interfaces.MyListItem;
import com.example.nikola.dotatracker.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeroStatsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<MyListItem>> {
    private String playerId;
    private RecAdapter adapter;
    private static final String LOG_TAG = HeroStatsFragment.class.getSimpleName();
    private ProgressBar pBar;
    private LinearLayout headerLayout;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public HeroStatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hero_stats, container, false);
        pBar = (ProgressBar) view.findViewById(R.id.hero_stats_progressbar);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.hero_stats_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLoaderManager().restartLoader(1, null, HeroStatsFragment.this);
            }
        });
        headerLayout = (LinearLayout) view.findViewById(R.id.hero_stats_header_description);
        playerId = getActivity().getIntent().getStringExtra(SearchActivity.INTENT_EXTRA_KEY);
        adapter = new RecAdapter(getContext(), Glide.with(this), new ArrayList<MyListItem>());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_herostats_recview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        pBar.setVisibility(View.VISIBLE);
        headerLayout.setVisibility(GONE);
        getLoaderManager().initLoader(1, null, this);

        return view;
    }

    @Override
    public Loader<List<MyListItem>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "FRAGMENTLOG HEROSTATS: onCreateLoader");
        return new FragmentAsyncTask(getActivity(), playerId);
    }

    @Override
    public void onLoadFinished(Loader<List<MyListItem>> loader, List<MyListItem> data) {
        pBar.setVisibility(GONE);
        headerLayout.setVisibility(View.VISIBLE);
        adapter.addList(data);
        swipeRefresh.setRefreshing(false);
        Log.v(LOG_TAG, "FRAGMENTLOG HEROSTATS: onLoadFinished");


    }

    @Override
    public void onLoaderReset(Loader<List<MyListItem>> loader) {

    }


    private static class FragmentAsyncTask extends AsyncTaskLoader<List<MyListItem>> {

        private String playerId;
        private List<MyListItem> mData;

        public FragmentAsyncTask(Context context, String playerId) {
            super(context);
            this.playerId = playerId;
        }

        @Override
        public List<MyListItem> loadInBackground() {

            Log.v(LOG_TAG, "FRAGMENTLOG HEROSTATS: loadInBackground");
            return NetworkUtils.fetchHeroStatsData(playerId);
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
