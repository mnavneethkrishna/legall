package com.whattabiz.legall.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.whattabiz.legall.fragments.DraftDeedsFragment;
import com.whattabiz.legall.fragments.PropertyAdvocateFragment;

/**
 * Created by User on 9/20/2016.
 */
public class PropertyFragmentAdapter extends FragmentPagerAdapter {

    public PropertyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch(position){
            case 0:
                return DraftDeedsFragment.newInstance();
            case 1:
                return PropertyAdvocateFragment.newInstance();
        }

        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "DRAFT DEEDS";
            case 1:
                return "ADVOCATES";


        }
        return null;
    }
}

