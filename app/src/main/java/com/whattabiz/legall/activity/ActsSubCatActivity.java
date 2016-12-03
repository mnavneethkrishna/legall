package com.whattabiz.legall.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.whattabiz.legall.R;
import com.whattabiz.legall.adapters.ActsSubListAdapter;
import com.whattabiz.legall.models.ActsListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActsSubCatActivity extends AppCompatActivity {

    ProgressBar loadingBar;
    private List<ActsListModel> actsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ActsSubListAdapter mAdapter;
    ActsListModel content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acts_sub_cat);

        Bundle b = getIntent().getExtras();
        String TITLE = b.getString("WINDOW_TITLE");
        String LINK = b.getString("LINK_ID");
        String ID = b.getString("LIST_ID");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(TITLE);

        loadingBar = (ProgressBar) findViewById(R.id.loading);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new ActsSubListAdapter(actsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ActsSubCatActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setVisibility(View.GONE);

        prepareContentListReg(ID, LINK);
    }

    private void prepareContentListReg(String requestParam, String requestCat) {

        actsList.clear();



        //replacing backslash and spaces in the urls
        //String requestString = requestParam.replaceAll(" ", "%20");


        String url = "http://legall.co.in/web/actsnLawsFiles.php?key=legall&cat=" + requestCat + "&subcat=" + requestParam ;

        Log.i("Legall",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Success handle response

                        Log.i("Legall", response);

                        try {
                            JSONArray postsArray = new JSONArray(response);


                            if (postsArray != null) {
                                // JSONArray postsArray = obj.getJSONArray("posts");

                                Log.i("Legall","we are in");

                                for (int i = 0; i < postsArray.length(); i++) {
                                    JSONObject itemObj = postsArray.getJSONObject(i);
                                    Log.i("Legall",itemObj.toString());
                                    //adding each json data to list
                                    content = new ActsListModel(itemObj.getString("name"), "", itemObj.getString("path"));
                                    actsList.add(content);

                                }
                                mAdapter.notifyDataSetChanged();

                            } else {

                                Toast.makeText(ActsSubCatActivity.this, "Againnnn", Toast.LENGTH_LONG).show();
                            }


                            loadingBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        } catch (JSONException ex) {
                            // Toast.makeText(ActsSubCatActivity.this),"Error",Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(ActsSubCatActivity.this).add(stringRequest);


        mAdapter.notifyDataSetChanged();
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
}
