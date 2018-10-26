package com.example.ayman.cherish.View.activities.accountSetup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ayman.cherish.View.Setupfragments.Done;

class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Fragment getItem(int position) {
        return Done.newInstance(position + 1, position == getCount() - 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}