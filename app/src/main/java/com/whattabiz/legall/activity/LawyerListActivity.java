package com.whattabiz.legall.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whattabiz.legall.Lawyers;
import com.whattabiz.legall.R;
import com.whattabiz.legall.User;
import com.whattabiz.legall.adapters.LawyerListAdapter;
import com.whattabiz.legall.models.AdvocateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class  LawyerListActivity extends AppCompatActivity {

    private List<AdvocateModel> advocateModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LawyerListAdapter mAdapter;

    FloatingActionButton btn_location;


    ProgressBar loading;



    String catId, subCatId,url,type,city_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advocate_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Advocates");

        Bundle b = getIntent().getExtras();
        catId = b.getString("CAT_ID",null);
        subCatId = b.getString("SUBCAT_ID",null);
        type = b.getString("TYPE","");

        if (type.matches("")){
            url = "http://legall.co.in/web/lawyerDetails.php?key=Legall&cat_id="+catId+"&subcat_id="+subCatId;
            city_url = "http://legall.co.in/web/lawyerAvailableCities.php?key=Legall&cat_id="+catId+"&subcat_id="+subCatId;
        }else{
            url = "http://legall.co.in/web/realEstate.php?key=Legall";
            city_url = "http://legall.co.in/web/otherLawyersAvailableList.php?key=Legall&q=0";
        }


        loading = (ProgressBar) findViewById(R.id.loading);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new LawyerListAdapter(advocateModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LawyerListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        new LoadCities().execute();

       // Log.i("Legall",Lawyers.getInstance().state.toString());



        btn_location = (FloatingActionButton) findViewById(R.id.fab);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    btn_location.hide();
                else if (dy < 0)
                    btn_location.show();
            }
        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createDialogue();
            }
        });


    }



    //background task to load city list to Lawyer singleton
    private class LoadCities extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            final Lawyers ls = Lawyers.getInstance();


            Log.i("Legall","url : "+city_url);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, city_url,
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
                                if (Lawyers.getInstance().cityList.contains(User.city))
                                    createList(User.city);
                                else
                                    createList(Lawyers.getInstance().cityList.get(0));

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
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(LawyerListActivity.this).add(stringRequest);


            return null;
        }

        @Override
        protected void onPostExecute(String result) {


        }
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

        return super.onOptionsItemSelected(item);
    }


    int selection = 0;

    public void createDialogue() {


        final CharSequence[] items = Lawyers.getInstance().cityList.toArray(new CharSequence[Lawyers.getInstance().cityList.size()]);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Location");
        builder.setSingleChoiceItems(items, selection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                loading.setVisibility(View.VISIBLE);
                createList(items[i].toString());

                Log.i("Legall", Lawyers.getInstance().cityList.get(i));
                selection = i;

                dialogInterface.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();


    }


    //to generate lawyer list according to city
    public void createList(final String city) {

        recyclerView.setVisibility(View.GONE);
       //loading.setVisibility(View.VISIBLE);

        final String getUrl = url+"&city="+city;

        advocateModelList.clear();

        Log.i("Legall", city);


        final Lawyers ls = Lawyers.getInstance();





            StringRequest stringRequest = new StringRequest(Request.Method.GET, getUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Success handle response

                            Log.i("Legall","url : "+getUrl);
                            Log.i("Legall", response);

                            try {
                                JSONArray object = new JSONArray(response);
                                if (object != null) {





                                    Log.i("Legall", "Creating Lawyers for List....."+city);


                                    ArrayList<AdvocateModel> newLawyers = new ArrayList<>();
                                    for (int i = 0; i < object.length(); i++) {

                                        JSONObject lawyer = object.getJSONObject(i);
                                        AdvocateModel advocateModel = new AdvocateModel(lawyer.getString("id"),lawyer.getString("name"),lawyer.getString("exp"),lawyer.getString("city"),lawyer.getString("ratings"));
                                        advocateModelList.add(advocateModel);

                                    }

                                    mAdapter.notifyDataSetChanged();
                                    ls.sortedLawyer.put(city,newLawyers);
                                    //advocateModelList = newLawyers;

                                    Log.i("Legall","city : "+city+" data : "+ newLawyers.size()+" stored size : "+ls.sortedLawyer.get(city).size());
                                    Log.i("Legall", "Cities " + ls.cityList.size());
                                    Log.i("Legall", "Citylist " + ls.cityList.toString());
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            mAdapter.notifyDataSetChanged();
                            recyclerView.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Error handle error
                }
            });

            // Add the request to the queue
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(LawyerListActivity.this).add(stringRequest);





        Log.i("Legall","tits  "+ Lawyers.getInstance().sortedLawyer.keySet().toString());



        Log.i("Legall",""+advocateModelList.size());



    }


}
