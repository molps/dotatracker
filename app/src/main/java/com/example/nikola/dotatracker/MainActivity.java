package com.example.nikola.dotatracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nikola.dotatracker.btmnavigation.FollowingFragment;
import com.example.nikola.dotatracker.btmnavigation.HomeFragment;
import com.example.nikola.dotatracker.btmnavigation.MatchesFragment;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.btm_navigation_view);
        final Fragment homeFragment = new HomeFragment();
        final Fragment matchesFragment = new MatchesFragment();
        final Fragment followingFragment = new FollowingFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder_main_activity, homeFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        switchFragments(homeFragment, followingFragment, matchesFragment);
                        return true;
                    case R.id.action_matches:
                        switchFragments(matchesFragment, homeFragment, followingFragment);
                        return true;
                    case R.id.action_following:
                        switchFragments(followingFragment, homeFragment, matchesFragment);
                        return true;
                }
                return true;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.searchView_id) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param fragment only 1st fragment is shown, rest of them are hidden
     */
    private void switchFragments(Fragment... fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragment[0].isAdded())
            transaction.add(R.id.fragment_placeholder_main_activity, fragment[0]);
        for (int i = 1; i < fragment.length; i++) {
            transaction.hide(fragment[i]);
        }
        transaction.show(fragment[0]);
        transaction.commit();

    }


}
