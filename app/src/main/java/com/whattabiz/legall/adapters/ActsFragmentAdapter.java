package com.whattabiz.legall.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.whattabiz.legall.fragments.ActsFragment;

import java.util.ArrayList;

/**
 * Created by User on 9/20/2016.
 */
public class ActsFragmentAdapter extends FragmentPagerAdapter {

    private static ArrayList<String> head = new ArrayList<>();
    private static ArrayList<String> id = new ArrayList<>();
    private static int count;
    Context context;


    public ActsFragmentAdapter(FragmentManager fm,Context c) {
        super(fm);
        this.context = c;

        //getting the stored data from the pref
        SharedPreferences preferences = context.getSharedPreferences("ACTS",Context.MODE_PRIVATE);
        count = preferences.getInt("count",0);
        for (int i=0;i<count;i++){
            head.add(preferences.getString("head"+i,null));
            id.add(preferences.getString("id"+i,null));
            Log.i("Legall",preferences.getString("id"+i,"empty"));
        }
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return ActsFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show no of total pages.
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return head.get(position).toString();
    }
}

