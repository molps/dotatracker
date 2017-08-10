package com.example.nikola.dotatracker.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.nikola.dotatracker.details.HeroStatsFragment;
import com.example.nikola.dotatracker.details.RecentFragment;


public class DetailAdapter extends FragmentPagerAdapter {


    public DetailAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new RecentFragment();
        } else {
            return new HeroStatsFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0){
            return "Recent";
        } else {
            return "Hero Stats";
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
