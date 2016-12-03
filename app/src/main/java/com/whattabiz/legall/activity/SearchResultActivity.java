package com.whattabiz.legall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.whattabiz.legall.adapters.ContentItemAdapter;
import com.whattabiz.legall.adapters.SearchResultAdapter;
import com.whattabiz.legall.models.ActsListModel;
import com.whattabiz.legall.models.ContentItem;
import com.whattabiz.legall.models.SearchResultModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {


    private List<SearchResultModel> resultList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchResultAdapter mAdapter;

   public static String QUERY;



    ProgressBar loading;

    MaterialSearchView searchView;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        context = SearchResultActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Search Acts and Laws");

        loading = (ProgressBar) findViewById(R.id.loading);

        //getting the query from previous activity
        Bundle b = getIntent().getExtras();
        QUERY = b.getString("QUERY");

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new SearchResultAdapter(resultList,SearchResultActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        loadResult(QUERY);

        //using the material search view
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                if (query.length()>2){

                    loadResult(query);
                    searchView.clearFocus();
                    searchView.closeSearch();

                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length()>2){
                    loadResult(newText);
                }
                return true;
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


    /**
     * method to make the get request
     * and give the arrays to loaddf() and loadSubCat()
     * @param QUERY
     */
    public void loadResult(String QUERY) {

        final String url = "http://www.legall.co.in/web/actSearch.php?key=Legall&q=" + QUERY;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        showLoading();

                        resultList.clear();

                        Log.i("Legall", "search Responce : " + response);

                        //Success handle response
                        try {
                            JSONObject obj = new JSONObject(response);

                            Integer status = obj.getInt("status");

                            if (status == 0) {
                                Toast.makeText(context, "No search results", Toast.LENGTH_LONG).show();

                            } else if (status == 1) {
                                JSONArray pdfFiles = new JSONArray(obj.get("pdfFiles").toString());

                                loadPdfFiles(pdfFiles);


                            } else if (status == 2) {

                                JSONArray subcats = new JSONArray(obj.get("subcategories").toString());

                                loadSubCat(subcats);



                            } else if (status == 3) {

                                JSONArray subcats = new JSONArray(obj.get("subcategories").toString());

                                loadSubCat(subcats);

                                JSONArray pdfFiles = new JSONArray(obj.get("pdfFiles").toString());

                                loadPdfFiles(pdfFiles);

                            }


                        } catch (JSONException ex) {
                            hideLoading();
                            Toast.makeText(context, "Response error!", Toast.LENGTH_SHORT).show();
                            ex.printStackTrace();
                        }

                        mAdapter.notifyDataSetChanged();
                        hideLoading();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideLoading();

                Log.e("Legall", error.toString());


                final AppCompatDialog dialog = new AppCompatDialog(context);
                dialog.setContentView(R.layout.dilog_request_error);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                Button back = (Button) dialog.findViewById(R.id.btn_back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        dialog.dismiss();
                        finish();
                        return;
                    }


                });

                dialog.show();
                return;
            }
        });

        // Add the request to the queue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(stringRequest);

    }


    /**
     * method to parse json array of pdf files
     * @param array
     */
    public void loadPdfFiles(JSONArray array) {

        if (array.length() > 0) {

            try {

                SearchResultModel item;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = new JSONObject(array.get(i).toString());
                    item = new SearchResultModel(1, obj.getString("name"), obj.getString("path").replaceAll("\\\\", "").replaceAll(" ", "%20"));
                    resultList.add(item);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * method to parse json array of subcategories
     * @param array
     */
    public void loadSubCat(JSONArray array) {

        if (array.length() > 0) {

            try {

                SearchResultModel item;
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = new JSONObject(array.get(i).toString());
                    item = new SearchResultModel(0, obj.getString("subname"), obj.getString("sub_id"), obj.getString("cat_id"));
                    resultList.add(item);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * method to show the loading and hide recycler
     */
    public void showLoading(){
        recyclerView.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }


    /**
     *method to show the recycler and hide the loading
     */
    public void hideLoading(){
        recyclerView.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }


}
