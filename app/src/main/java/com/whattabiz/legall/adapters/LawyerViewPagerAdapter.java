package com.whattabiz.legall.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.whattabiz.legall.fragments.CalendarFragment;
import com.whattabiz.legall.fragments.CasesFragment;

/**
 * Created by Rumaan on 11-11-2016.
 */

public class LawyerViewPagerAdapter extends FragmentPagerAdapter {

    /* Number of Pages for the ViewPager */
    private static final int sNUM_PAGES = 2;

    private Context mContext;

    public LawyerViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CasesFragment.newInstance();
            case 1:
                return CalendarFragment.newInstance();
            default:
                Log.e("WTH", "Someone SCREWED UP WITH THE FRAGMENTS?");
                return null;
        }
    }

    @Override
    public int getCount() {
        return sNUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Cases";
            case 1:
                return "Calendar";
            default:
                return super.getPageTitle(position);
        }
    }
}
