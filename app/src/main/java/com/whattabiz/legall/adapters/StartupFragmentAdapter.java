package com.whattabiz.legall.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.whattabiz.legall.fragments.CompanyDescriptionFragment;
import com.whattabiz.legall.fragments.InternationalFragment;
import com.whattabiz.legall.fragments.StartupDocumentListFragment;

/**
 * Created by User on 9/20/2016.
 */
public class StartupFragmentAdapter extends FragmentPagerAdapter {

    public StartupFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return CompanyDescriptionFragment.newInstance();
            case 1:
                return StartupDocumentListFragment.newInstance();




        }
        return InternationalFragment.newInstance();
    }



    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "OVERVIEW";
            case 1:
                return "SERVICES";


        }
        return null;
    }
}

