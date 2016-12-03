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
import com.whattabiz.legall.R;
import com.whattabiz.legall.adapters.TemplateAdapter;
import com.whattabiz.legall.models.TemplateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TemplateViewActivity extends AppCompatActivity {


    private List<TemplateModel> templateModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TemplateAdapter mAdapter;

    ProgressBar loading;

    MaterialSearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_view);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Templates");

        loading = (ProgressBar) findViewById(R.id.loading);


        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new TemplateAdapter(templateModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.setVisibility(View.GONE);

        prepareTemplateList();


        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                prepareSearchList(newText);
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

    public void prepareTemplateList(){



        templateModelList.clear();




        String url = "http://legall.co.in/web/draft_deeds.php?id=1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Success handle response

                        Log.i("Legall",response);

                        try {
                            JSONObject object = new JSONObject(response);

                            String message = object.getString("message");

                            if (message.matches("Success")){

                                if(object!=null) {

                                    JSONArray postsArray = object.getJSONArray("draft_deed");


                                    for(int i=0; i<postsArray.length(); i++) {
                                        JSONObject itemObj = postsArray.getJSONObject(i);

                                        //adding each json data to list
                                        TemplateModel template = new TemplateModel(itemObj.getString("id"),itemObj.getString("title"),itemObj.getString("price"));
                                        templateModelList.add(template);
                                    }


                                    Log.i("Legall","Total templates : "+templateModelList.size());
                                }
                                else {

                                    Toast.makeText(TemplateViewActivity.this,"Againnnn",Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(TemplateViewActivity.this).add(stringRequest);




        mAdapter.notifyDataSetChanged();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.tb_cart:
                startActivity(new Intent(TemplateViewActivity.this,CartActivity.class));
                break;
            case  R.id.tb_search:
                return true;





        }
        return true;
    }


    public void prepareSearchList(String query){

        Log.i("Legall","Search List, Query : "+query);
        if (query.matches("")){
            mAdapter = new TemplateAdapter(templateModelList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }else{

            List<TemplateModel> searchResultList = new ArrayList<>();

            searchResultList.clear();
            for (TemplateModel temp : templateModelList){

                if (temp.getTitle().toLowerCase().contains(query.toLowerCase())){
                    searchResultList.add(temp);
                }

                Log.i("Legall","search list size : "+searchResultList.size());
            }

            mAdapter = new TemplateAdapter(searchResultList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart, menu);

        MenuItem item = menu.findItem(R.id.tb_search);
        searchView.setMenuItem(item);

        return true;
    }

}
