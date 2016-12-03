package com.whattabiz.legall.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whattabiz.legall.adapters.InternationalFragmentAdapter;
import com.whattabiz.legall.fragments.InternationalLawyerFragment;
import com.whattabiz.legall.Lawyers;
import com.whattabiz.legall.R;
import com.whattabiz.legall.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InternationalActivity extends AppCompatActivity {

    FloatingActionButton fab ;
    public Integer loadState=0;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private InternationalFragmentAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_international);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("International Relations");



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new InternationalFragmentAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialogue();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                if(loadState == 1) {
                    if (position == 0) {
                        fab.hide();
                    } else if (position == 1) {
                        fab.show();
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        loadCityList();

    }


    //background task to load city list to Lawyer singleton
    public void loadCityList(){
            final Lawyers ls = Lawyers.getInstance();

            String url = "http://legall.co.in/web/otherLawyersAvailableList.php?key=Legall&q=1";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Success handle response

                            Log.i("Legall", response);

                            try {
                                JSONArray object = new JSONArray(response);
                                if (object != null) {

                                    ls.allLawyers.clear();
                                    ls.sortedLawyer.clear();
                                    ls.cityList.clear();

                                    Log.i("Legall", "Creating City List.....");

                                    //storing cities to the cityList in Lawyers singleton
                                    for (int i = 0; i < object.length(); i++) {

                                        JSONObject city = object.getJSONObject(i);
                                        ls.cityList.add(city.getString("city"));
                                    }


                                    Log.i("Legall", "Cities " + ls.cityList.size());
                                    Log.i("Legall", "Citylist " + ls.cityList.toString());
                                }


                                //flag to show that city list is loaded
                                ls.state = 1;


                                //checking for lawyers availability in user's city
                                if (Lawyers.getInstance().cityList.contains(User.city)) {
                                    InternationalLawyerFragment fragment = (InternationalLawyerFragment) InternationalLawyerFragment.getInstance();
                                    fragment.createList(User.getInstance(InternationalActivity.this).city);
                                }
                                else {
                                    InternationalLawyerFragment fragment = (InternationalLawyerFragment) InternationalLawyerFragment.getInstance();
                                    fragment.createList(Lawyers.getInstance().cityList.get(0));

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Error handle error
                }
            });

            // Add the request to the queue
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(InternationalActivity.this).add(stringRequest);



        }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
            return true;
        }

    int selection = 0;
    public void createDialogue() {


        final CharSequence[] items = Lawyers.getInstance().cityList.toArray(new CharSequence[Lawyers.getInstance().cityList.size()]);


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Location");
        builder.setSingleChoiceItems(items, selection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                InternationalLawyerFragment fragment = (InternationalLawyerFragment) InternationalLawyerFragment.getInstance();
                fragment.createList(Lawyers.getInstance().cityList.get(i));

                Log.i("Legall",Lawyers.getInstance().cityList.get(i));
                selection = i;

                dialogInterface.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();


    }


    //to be called from the fragment to show load comleted
    public void loadSuccess(){
        loadState=1;
        showFab();
    }

    public void showFab(){
        //fab.show();
    }







}
