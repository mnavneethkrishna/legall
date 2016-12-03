package com.whattabiz.legall.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.whattabiz.legall.fragments.ActsFragment;
import com.whattabiz.legall.adapters.ActsFragmentAdapter;
import com.whattabiz.legall.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActsActivity extends AppCompatActivity implements ActsFragment.OnFragmentInteractionListener {



    public MaterialSearchView searchView;

    private ActsFragmentAdapter mFragmentAdapter;

    ProgressDialog pd;

    public ArrayList<String> suggestions = new ArrayList<>();



    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Acts and Laws");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(3);


        // Set up the ViewPager with the sections adapter.

        mViewPager = (ViewPager) findViewById(R.id.container);


        //to load the main category titles..
        pd = new ProgressDialog(ActsActivity.this);
        pd.setMessage("Please wait..");
        pd.show();

        new LoadMainCat().execute();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        //using the material search view
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()<3)
                    return false;
                searchView.closeSearch();
                startActivity(new Intent(ActsActivity.this,SearchResultActivity.class).putExtra("QUERY",query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });






        //new loadMainCat().execute();




    }


    /**
     * function to create the language option menu in toolbar
     */
    int selection = 0;

    public void createDialogue() {


        final CharSequence[] items = {"English", "Kannada"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose language");
        builder.setSingleChoiceItems(items, selection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        selection = 0;
                        break;
                    case 1:
                        selection = 1;
                        Toast.makeText(ActsActivity.this,"Coming Soon",Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        selection = 2;
                        break;
                }
                dialogInterface.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);


        MenuItem item = menu.findItem(R.id.tb_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.tb_translate) {
            createDialogue();
        }

        if (id == R.id.tb_search) {
            startActivity(new Intent(ActsActivity.this,SearchResultActivity.class).putExtra("QUERY","ABC"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }



    //Async tak to make request for json data to get titles of the content
    private class LoadMainCat extends AsyncTask<String, Void, String> {




        @Override
        protected String doInBackground(String... strings) {

            String url = "http://legall.co.in/web/actsnLawsCat.php?key=Legall";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Success handle response


                            Log.i("Legall",response);

                            try {
                                JSONArray postsArray = new JSONArray(response);

                                if(postsArray!=null) {
                                    // JSONArray postsArray = obj.getJSONArray("posts");
                                    SharedPreferences.Editor prefEdit = getSharedPreferences("ACTS",MODE_PRIVATE).edit();
                                    prefEdit.clear();
                                    prefEdit.putInt("count",postsArray.length());
                                    Log.i("Legall","ACTS HEAD LENGTH "+postsArray.length());


                                    for(int i=0; i<postsArray.length(); i++) {
                                        JSONObject itemObj = postsArray.getJSONObject(i);
                                        prefEdit.putString("head"+i,itemObj.getString("categories"));
                                        prefEdit.putString("id"+i,itemObj.getString("category_id"));
                                        Log.i("Legall","category"+itemObj.getString("categories"));
                                    }

                                    prefEdit.commit();
                                }
                                else {

                                    Toast.makeText(ActsActivity.this,"Againnnn",Toast.LENGTH_LONG).show();
                                }
                            }
                            catch(JSONException ex) {
                                Toast.makeText(ActsActivity.this,"Error",Toast.LENGTH_LONG).show();
                                ex.printStackTrace();
                            }


                            mFragmentAdapter = new ActsFragmentAdapter(getSupportFragmentManager(),ActsActivity.this);
                            mViewPager.setAdapter(mFragmentAdapter);
                            pd.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Error handle error
                }
            });

            // Add the request to the queue
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


            return null;
        }

        @Override
        protected void onPostExecute(String result) {


        }
    }


    @Override
    public void onBackPressed() {

        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
            searchView.clearFocus();
        }else
            super.onBackPressed();
    }



}

