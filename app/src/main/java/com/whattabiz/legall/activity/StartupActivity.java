package com.whattabiz.legall.activity;

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
import android.widget.ScrollView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whattabiz.legall.R;
import com.whattabiz.legall.adapters.BulletTextAdapter;
import com.whattabiz.legall.models.BulletText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartupActivity extends AppCompatActivity {



    private List<BulletText> bulletTextList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BulletTextAdapter mAdapter;
    ScrollView scroll;
    ProgressBar loading;


    String url = "http://legall.co.in/web/startupcat.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("StartUp");

        loading = (ProgressBar) findViewById(R.id.loading);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new BulletTextAdapter(bulletTextList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        createlist();

        //for removing focus issue to bottom
        scroll=(ScrollView) findViewById(R.id.view_scroll);
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(View.FOCUS_UP);
            }
        });





    }


    public void createlist(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Success handle response


                        Log.i("Legall", response);

                        try {

                            JSONArray array = new JSONArray(response);

                            for ( int i = 0; i<array.length(); i++){

                                JSONObject obj = array.getJSONObject(i);
                                String title = obj.getString("category");
                                String description = obj.getString("description");
                                String id = obj.getString("category_id");


                                BulletText bullet  = new BulletText(title,description ,id);
                                bulletTextList.add(bullet);

                            }


                            mAdapter.notifyDataSetChanged();
                            loading.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Error handle error




                final AppCompatDialog dialog = new AppCompatDialog(StartupActivity.this);
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();


                param.put("key", "Legall");


                return param;
            }

        };

        // Add the request to the queue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(StartupActivity.this).add(stringRequest);


    }



    public void prepareBulletText(){
        BulletText bullet = new BulletText("Proprietorship Firm","","1");
        bulletTextList.add(bullet);

        bullet  = new BulletText("Partnership Firm","" ,"2");
        bulletTextList.add(bullet);

        bullet  = new BulletText("One Person Company", "","3");
        bulletTextList.add(bullet);

        bullet  = new BulletText("Limited Liability Partnership", "","4");
        bulletTextList.add(bullet);

        bullet  = new BulletText("Private Limited Company", "","5");
        bulletTextList.add(bullet);

        bullet  = new BulletText("Public Limited Company", "","6");
        bulletTextList.add(bullet);



        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.tb_cart:
                startActivity(new Intent(StartupActivity.this,CartActivity.class));



        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart, menu);

        return true;
    }







}
