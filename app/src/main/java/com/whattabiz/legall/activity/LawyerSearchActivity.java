package com.whattabiz.legall.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.whattabiz.legall.R;
import com.whattabiz.legall.adapters.LawyerListAdapter;
import com.whattabiz.legall.models.AdvocateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LawyerSearchActivity extends AppCompatActivity {


    MaterialSearchView searchView;

    private List<AdvocateModel> advocateModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LawyerListAdapter mAdapter;
    ProgressBar loading;

    Context context;
    String QUERY;

    CreateList search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_search);

        context = LawyerSearchActivity.this;


        Bundle b = getIntent().getExtras();
        QUERY = b.getString("QUERY");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Lawyer Search");


        loading = (ProgressBar) findViewById(R.id.loading);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new LawyerListAdapter(advocateModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        showLoading();

        search = new CreateList();
        search.execute();

        //using the material search view
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                QUERY = query;
                if (query.length() > 1) {
                    showLoading();


                    if (search.getStatus().equals(AsyncTask.Status.RUNNING))
                        search.cancel(true);
                    new CreateList().execute();
                }
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

                finish();

            }
        });


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

        if (id == R.id.tb_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
            searchView.clearFocus();
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);


        MenuItem item = menu.findItem(R.id.tb_search);
        searchView.setMenuItem(item);

        return true;
    }

    private class CreateList extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {


            advocateModelList.clear();

            Log.i("Legall", QUERY);

            String url = "http://legall.co.in/web/lawyerSearch.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Success handle response

                            Log.i("Legall", "responce : " + response);

                            try {
                                JSONObject object = new JSONObject(response);
                                if (object.getInt("status") == 1) {


                                    JSONArray array = new JSONArray(object.getJSONArray("lawyers").toString());


                                    Log.i("Legall", "Creating Lawyers for List....." + QUERY);


                                    for (int i = 0; i < array.length(); i++) {

                                        JSONObject lawyer = array.getJSONObject(i);
                                        AdvocateModel advocateModel = new AdvocateModel(lawyer.getString("lawyerId"), lawyer.getString("name"), lawyer.getString("exp"), lawyer.getString("city"), lawyer.getString("ratings"));
                                        advocateModelList.add(advocateModel);

                                    }

                                    Log.i("Legall", "Total no result : " + advocateModelList.size());

                                    mAdapter.notifyDataSetChanged();

                                    //advocateModelList = newLawyers;

                                    loading.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);

                                } else {
                                    Toast.makeText(LawyerSearchActivity.this, "No results found", Toast.LENGTH_LONG).show();
                                    advocateModelList.clear();
                                    hideLoading();
                                    return;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            mAdapter.notifyDataSetChanged();

                            hideLoading();


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Error handle error
                    Toast.makeText(context, "Something Went wrong ", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> param = new HashMap<String, String>();

                    param.put("key", "Legall");
                    param.put("q", QUERY);

                    return param;

                }
            };

            // Add the request to the queue
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(context).add(stringRequest);


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            mAdapter.notifyDataSetChanged();

        }
    }


    /**
     * method to show the loading and hide recycler
     */
    public void showLoading() {
        recyclerView.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }


    /**
     * method to show the recycler and hide the loading
     */
    public void hideLoading() {
        recyclerView.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }


}


