package com.example.ayman.cherish.activities.profileAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ayman.cherish.Profilefragments.Family;
import com.example.ayman.cherish.Profilefragments.Messages;
import com.example.ayman.cherish.Profilefragments.MyChildren;
import com.example.ayman.cherish.Profilefragments.Notifications;
import com.example.ayman.cherish.Profilefragments.Timeline;

public class TitleApdapter extends FragmentPagerAdapter {

    public final String[] title =new String[]{"Timeline","My Children","Family"
            ,"Messages","Notifications"};
    private final Fragment fragment[]=new Fragment[title.length];

    public TitleApdapter(FragmentManager fm) {
        super(fm);

        fragment[0]=new Timeline();
        fragment[1]=new MyChildren();
        fragment[2]=new Family();
        fragment[3]=new Messages();
        fragment[4]=new Notifications();

    }

    @Override
    public Fragment getItem(int position) {
        return fragment[position];
    }

    @Override
    public int getCount() {
        return fragment.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return title[position];

    }
}
