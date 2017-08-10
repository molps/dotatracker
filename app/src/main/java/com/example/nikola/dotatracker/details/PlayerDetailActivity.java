package com.example.nikola.dotatracker.details;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.nikola.dotatracker.R;
import com.example.nikola.dotatracker.adapters.DetailAdapter;

public class PlayerDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_player_detail);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);
        DetailAdapter adapter = new DetailAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }
}
