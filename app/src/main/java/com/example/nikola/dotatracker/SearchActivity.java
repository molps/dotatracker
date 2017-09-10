package com.example.nikola.dotatracker;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nikola.dotatracker.adapters.CurAdapter;
import com.example.nikola.dotatracker.adapters.RecAdapter;
import com.example.nikola.dotatracker.data.DotaContract.DotaEntry;
import com.example.nikola.dotatracker.details.PlayerDetailActivity;
import com.example.nikola.dotatracker.interfaces.MyListItem;
import com.example.nikola.dotatracker.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MyListItem>>, RecAdapter.OnItemViewClickListener {
    private Toast toast;
    private static final String LOG_TAG = SearchActivity.class.getSimpleName();
    public static final String INTENT_EXTRA_KEY = "playerID";
    private RecAdapter mAdapter;
    private ProgressBar pBarSearch;
    private static final String QUERY_BUNDLE_KEY = "bundle_key";
    private View dimView;
    private SearchView searchView;
    private CurAdapter cursorAdapter;
    private static final int RECENT_ENTRY_LOADER_ID = 1;
    private static final String BUNDLE_QUERY_KEY = "queryKey";
    private RecyclerView recentEntryRecView;

    private LoaderManager.LoaderCallbacks<Cursor> recentEntryLoader = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (args != null) {
                String query = args.getString(BUNDLE_QUERY_KEY);
                Uri uri = DotaEntry.CONTENT_URI.buildUpon().appendPath(query).build();
                return new CursorLoader(
                        SearchActivity.this,
                        uri,
                        null,
                        null,
                        null,
                        null
                );
            } else {
                return new CursorLoader(
                        SearchActivity.this,
                        DotaEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
            }

        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            cursorAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        dimView = findViewById(R.id.dim_View);
        pBarSearch = (ProgressBar) findViewById(R.id.pBarSearch);
        mAdapter = new RecAdapter(Glide.with(this), new ArrayList<MyListItem>(), this);

        cursorAdapter = new CurAdapter(null);

        recentEntryRecView = (RecyclerView) findViewById(R.id.recentEntry_recView);
        recentEntryRecView.setLayoutManager(new LinearLayoutManager(this));
        recentEntryRecView.setAdapter(cursorAdapter);
        recentEntryRecView.setHasFixedSize(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.search_recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        dimView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (searchView.hasFocus())
                    searchView.clearFocus();
                return false;
            }
        });
        final int startColor = ContextCompat.getColor(SearchActivity.this, R.color.startColor);
        final int endColor = ContextCompat.getColor(SearchActivity.this, R.color.endColor);
        animateDimView(startColor, endColor, 300);


        getLoaderManager().initLoader(0, null, this);
        getLoaderManager().initLoader(RECENT_ENTRY_LOADER_ID, null, recentEntryLoader);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_activity_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchactivity_menu);

        // kada idem back button iz SearchView-a pojavljuju se one 3 tackice ili ikonica
        //ovo je resilo problem, tako sto stavim da se ne vidi uopste
        searchItem.setVisible(false);

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setQueryHint("Search Players");

        // Da je uvek rasireno i spremno za kucanje
        searchItem.expandActionView();

        // Videle se linije od edittexta, problem resen tako sto je postavljena
        // ista pozadina koja je i na toolbaru (u ovom slucaju bela)
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.WHITE);

        // Da bih back button iz searchviewa koristio da odmah izadjem iz Aktivnosti, a ne da mi pre toga
        // spusta tastaturu, pa skupi searchview pa tek onda da izadje
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                mAdapter.removeData();
                ContentValues contentValues = new ContentValues();
                contentValues.put(DotaEntry.COLUMN_ENTRY, query);
                getContentResolver().insert(DotaEntry.CONTENT_URI, contentValues);
                Bundle bundle = new Bundle();
                bundle.putString(QUERY_BUNDLE_KEY, query);
                if (isConnected()) {
                    pBarSearch.setVisibility(View.VISIBLE);
                    getLoaderManager().restartLoader(0, bundle, SearchActivity.this);
                } else {
                    showToast(SearchActivity.this, "Check your network connection");
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(BUNDLE_QUERY_KEY, newText);
                    recentEntryRecView.setVisibility(View.VISIBLE);
                    getLoaderManager().restartLoader(RECENT_ENTRY_LOADER_ID, bundle, recentEntryLoader);
                } else {
                    // Da se obrise lista kada nema nikakvog texta u searchu
                    cursorAdapter.swapCursor(null);
                    recentEntryRecView.setVisibility(View.GONE);
                }
                return true;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int startColor = ContextCompat.getColor(SearchActivity.this, R.color.startColor);
                int endColor = ContextCompat.getColor(SearchActivity.this, R.color.endColor);
                if (hasFocus) {
                    recentEntryRecView.setVisibility(View.VISIBLE);
                    animateDimView(startColor, endColor, 0);
                } else {
                    recentEntryRecView.setVisibility(View.GONE);
                    animateDimView(endColor, startColor, 0);
                }

            }
        });


        return true;
    }


    @Override
    public Loader<List<MyListItem>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "TIMECHECKING, onCreateLoader");
        return new AsyncNetworkLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<List<MyListItem>> loader, List<MyListItem> data) {
        pBarSearch.setVisibility(View.GONE);
        mAdapter.addList(data);
        Log.v(LOG_TAG, "TIMECHECKING, onLoadFinished ");

    }

    @Override
    public void onLoaderReset(Loader<List<MyListItem>> loader) {

    }


    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnected());

    }

    private void animateDimView(int startColor, int endColor, long startDelay) {
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
        colorAnimator.setDuration(200);
        colorAnimator.setStartDelay(startDelay);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dimView.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
        colorAnimator.start();
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
