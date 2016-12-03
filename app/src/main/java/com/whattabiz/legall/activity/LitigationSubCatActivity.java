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
import com.whattabiz.legall.adapters.LitigationSubCatAdapter;
import com.whattabiz.legall.models.LitigationCategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LitigationSubCatActivity extends AppCompatActivity {

    private List<LitigationCategoryModel> categoryModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LitigationSubCatAdapter mAdapter;

    LitigationCategoryModel content;

    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_litigation_sub_cat);

        Bundle b = getIntent().getExtras();
        String WINDOW_TITLE = b.getString("WINDOW_TITLE");
        String LINK_ID = b.getString("LINK_ID");

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(WINDOW_TITLE);


        loading = (ProgressBar) findViewById(R.id.loading);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new LitigationSubCatAdapter(categoryModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LitigationSubCatActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.setVisibility(View.GONE);

        prepareSubCategoryList(LINK_ID);

    }
     
    
    public void prepareSubCategoryList(String id){

        categoryModelList.clear();

        content = new LitigationCategoryModel();



        final String url = "http://legall.co.in/web/sub_categories.php?categoryid="+id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Success handle response

//                        Log.i("Legall",url);
//                        Log.i("Legall",response);

                        try {
                            JSONObject object = new JSONObject(response);

                            String message = object.getString("message");

                            if (message.matches("Success")){

                                if(object!=null) {

                                    JSONArray postsArray = object.getJSONArray("subcategory");


                                    for(int i=0; i<postsArray.length(); i++) {
                                        JSONObject itemObj = postsArray.getJSONObject(i);

                                        //adding each json data to list
                                        content = new LitigationCategoryModel(itemObj.getString("title"),itemObj.getString("description"),itemObj.getString("id"),itemObj.getString("category_id"));
                                        categoryModelList.add(content);
                                    }


                                }
                                else {

                                    Toast.makeText(LitigationSubCatActivity.this,"Againnnn",Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(LitigationSubCatActivity.this).add(stringRequest);

        mAdapter.notifyDataSetChanged();
         
         
     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;


        }
        return true;
    }
}
