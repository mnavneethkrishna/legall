package com.whattabiz.legall.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.whattabiz.legall.fragments.ArticlesListFragment;

/**
 * Created by User on 9/20/2016.
 */
public class ArticlesFragmentAdapter extends FragmentPagerAdapter {

    public ArticlesFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return ArticlesListFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 3  total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "TOP";
            case 1:
                return "FEATURED";
            case 2:
                return "TRENDING ";

        }
        return null;
    }
}

