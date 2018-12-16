package com.example.ag.bedsoreapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override  // called by the ViewPager to obtain a title string to describe the specified page.
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override // Return the Fragment associated with a specified position.
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override // Return the number of views available.
    public int getCount() {
        return mFragmentList.size();
    }
}