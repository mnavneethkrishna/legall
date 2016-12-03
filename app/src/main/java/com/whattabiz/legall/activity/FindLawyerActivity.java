package com.whattabiz.legall.activity;

import android.content.Intent;
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
import com.whattabiz.legall.adapters.LitigationMainCatAdapter;
import com.whattabiz.legall.R;
import com.whattabiz.legall.models.LitigationCategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class  FindLawyerActivity extends AppCompatActivity {


    private List<LitigationCategoryModel> categoryModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LitigationMainCatAdapter mAdapter;
    LitigationCategoryModel content;

    MaterialSearchView searchView;

    ProgressBar loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_lawyer);


        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find A Lawyer");


        loading = (ProgressBar) findViewById(R.id.loading);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new LitigationMainCatAdapter(categoryModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(FindLawyerActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.setVisibility(View.GONE);


        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query.length()>1){
                    Intent i = new Intent(FindLawyerActivity.this,LawyerSearchActivity.class);
                    i.putExtra("QUERY",query);
                    searchView.closeSearch();
                    startActivity(i);
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



            }
        });



        prepareMainCategoryList();



    }


    public void prepareMainCategoryList(){

        categoryModelList.clear();

        content = new LitigationCategoryModel();


        String url = "http://legall.co.in/web/categories.php?id=1";
        //Log.i("Legall",url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Success handle response

                        //Log.i("Legall",response);

                        try {
                            JSONObject object = new JSONObject(response);

                            String message = object.getString("message");

                            if (message.matches("Success")){

                                if(object!=null) {

                                    JSONArray postsArray = object.getJSONArray("category");


                                    for(int i=0; i<postsArray.length(); i++) {
                                        JSONObject itemObj = postsArray.getJSONObject(i);

                                        //adding each json data to list
                                        content = new LitigationCategoryModel(itemObj.getString("title"),itemObj.getString("description"),itemObj.getString("id"));
                                        categoryModelList.add(content);
                                    }


                                }
                                else {

                                    Toast.makeText(FindLawyerActivity.this,"Againnnn",Toast.LENGTH_LONG).show();
                                }

                                mAdapter.notifyDataSetChanged();
                                loading.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);


                            }



                        }
                        catch(JSONException ex) {
                            // Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
                            ex.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Error handle error
            }
        });

        // Add the request to the queue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(FindLawyerActivity.this).add(stringRequest);




        mAdapter.notifyDataSetChanged();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);


        MenuItem item = menu.findItem(R.id.tb_search);
        searchView.setMenuItem(item);

        return true;
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
