package com.example.nikola.dotatracker;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nikola.dotatracker.adapters.RecAdapter;
import com.example.nikola.dotatracker.details.PlayerDetailActivity;
import com.example.nikola.dotatracker.interfaces.MyListItem;
import com.example.nikola.dotatracker.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MyListItem>>, RecAdapter.OnItemViewClickListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String INTENT_EXTRA_KEY = "playerID";


    private RecAdapter mAdapter;
    private SearchView searchView;
    private ProgressBar pBar;
    private Toast toast;
    private MenuItem searchItem;
    private static final String QUERY_BUNDLE_KEY = "bundle_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");
        pBar = (ProgressBar) findViewById(R.id.pbarId);
        mAdapter = new RecAdapter(Glide.with(this), new ArrayList<MyListItem>(), this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recViewId);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public Loader<List<MyListItem>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "TIMECHECKING, onCreateLoader");
        return new AsyncNetworkLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<List<MyListItem>> loader, List<MyListItem> data) {
        pBar.setVisibility(View.GONE);
        mAdapter.addList(data);
        Log.v(LOG_TAG, "TIMECHECKING, onLoadFinished ");

    }

    @Override
    public void onLoaderReset(Loader<List<MyListItem>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        searchItem = menu.findItem(R.id.searchView_id);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.searchView_id:
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        MenuItemCompat.collapseActionView(searchItem);
                        mAdapter.removeData();
                        Bundle bundle = new Bundle();
                        bundle.putString(QUERY_BUNDLE_KEY, query);
                        if (isConnected()) {
                            pBar.setVisibility(View.VISIBLE);
                            getLoaderManager().restartLoader(0, bundle, MainActivity.this);
                        } else {
                            showToast(MainActivity.this, "Check your network connection");
                        }
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
        }


        return super.onOptionsItemSelected(item);
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnected());

    }

    private void showToast(Context context, String text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onItemViewClick(long playerId) {
        Intent intent = new Intent(this, PlayerDetailActivity.class);
        intent.putExtra(INTENT_EXTRA_KEY, String.valueOf(playerId));
        startActivity(intent);
    }

    private static class AsyncNetworkLoader extends AsyncTaskLoader<List<MyListItem>> {
        private Bundle args;
        private List<MyListItem> mData;

        AsyncNetworkLoader(Context context, Bundle args) {
            super(context);
            this.args = args;
        }

        @Override
        protected void onStartLoading() {
            Log.v(LOG_TAG, "TIMECHECKING, onStartLoading");
            if (args == null) {
                Log.v(LOG_TAG, "TIMECHECKING, noForceLoad");
                return;
            }
            if (mData != null) {
                Log.v(LOG_TAG, "TIMECHECKING, Loading Cached Data");
                deliverResult(mData);
            } else {
                forceLoad();
            }
        }

        @Override
        public List<MyListItem> loadInBackground() {
            Log.v(LOG_TAG, "TIMECHECKING, loadInBackground");
            String query;
            query = args.getString(QUERY_BUNDLE_KEY);
            return NetworkUtils.fetchSearchData(query);
        }

        @Override
        public void deliverResult(List<MyListItem> data) {
            mData = data;
            super.deliverResult(data);
        }
    }
}
