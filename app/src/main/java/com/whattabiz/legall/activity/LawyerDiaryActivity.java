package com.whattabiz.legall.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.whattabiz.legall.GsonConverter;
import com.whattabiz.legall.Lawyers;
import com.whattabiz.legall.R;
import com.whattabiz.legall.User;
import com.whattabiz.legall.adapters.LawyerViewPagerAdapter;
import com.whattabiz.legall.models.CaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LawyerDiaryActivity extends AppCompatActivity {
    private static final String LAWYER_DIARY_SEND_JSON_URL = "http://legall.co.in/web/lawyerDairy.php";
    private static final String LAWYER_DIARY_GET_JSON_URL = "http://www.legall.co.in/web/getLawyerDairy.php";
    public static String json = null;
    private static String responseJSON;
    SharedPreferences user;
    // Main ArrayList here
    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private Type type = new TypeToken<ArrayList<CaseModel>>() {
    }.getType();

    /**
     * Get the JSON array as String from Server
     * after getting JSON:
     * (ASSUMING USER INVOKES THE ACTIVITY FOR THE FIRST TIME)
     * (ALSO USER HAS THE DIARY DETAILS IN SERVER FROM PREVIOUS LOGIN)
     * 1. Save it in SharedPrefs
     * 2. Populate the ArrayList
     */
    public static void getJsonFromServer(final Context context) {
        /* do this asynchronously, don't wait for bs */
        /* Also the activity is doing way too much work in Main Thread */
        AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Log.d("SYNC", "Initiated");

                Log.d("UID", User.Id);
                Log.d("PROF", User.type);
                Log.d("diary", json);

                final String user_id = User.Id;
                final String profession = User.type;

                /* make request here */
                StringRequest stringRequest = new StringRequest(Request.Method.POST, LAWYER_DIARY_GET_JSON_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Resp diary", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getInt("status") == 1) {
                                responseJSON = response;
                            } else {
                                responseJSON = null;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", Arrays.toString(error.getStackTrace()));
                        Toast.makeText(context, "Error while syncing with the server!", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("user_id", user_id);
                        params.put("profession", profession);
                        params.put("key", "Legall");
                        return params;
                    }
                };

                Volley.newRequestQueue(context).add(stringRequest)
                        .setRetryPolicy(new DefaultRetryPolicy(
                                5000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        ));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (responseJSON == null) {
                    Log.d("RESP_diary", "JSON IS EMPTY");
                }
            }
        });

    }

    /**
     * Sends the JSON array as a String from SharedPrefs to Server
     * Required: user_id, profession, diary
     */
    public static void sendJsonToServer(final Context context) {
        Log.d("UID", User.Id);
        Log.d("PROF", User.type);
        Log.d("diary", json);

        AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, LAWYER_DIARY_SEND_JSON_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Legall", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("LD_REQ_ERROR", error.getLocalizedMessage());
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        String user_id = User.Id;
                        String profession = User.type;
                        String diary = json;

                        Map<String, String> params = new HashMap<>();
                        params.put("user_id", user_id);
                        params.put("profession", profession);
                        params.put("diary", diary);
                        params.put("key", "Legall");
                        return params;
                    }
                };

                stringRequest.setRetryPolicy(
                        new DefaultRetryPolicy(5000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                );
                Volley.newRequestQueue(context).add(stringRequest);
                return null;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_diary);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // set the toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Lawyer Dairy");

        // user = getSharedPreferences("user", Context.MODE_PRIVATE);
        // get the sharedprefs
        json = getSharedPreferences(CasesFragment.SHARED_PREF_KEY_CASE, Context.MODE_PRIVATE).getString("CASE_JSON", "");

        // if not empty
        if (!(json.isEmpty() || json.equals("") || json.equals("[]"))) {
            Lawyers.getInstance().caseModelArrayList = new ArrayList<>(GsonConverter.fromJsonToPOJO(type, json));
        } else {
            /* If SharedPrefs is empty assume user hasn't added any cases
             * or user is opening the activity for the first time (may have used this activity elsewhere
             * in other device) */
            getJsonFromServer(LawyerDiaryActivity.this);
            if (responseJSON != null) {
                /* Assume everything is fine */
                Lawyers.getInstance().caseModelArrayList = new ArrayList<>(GsonConverter.fromJsonToPOJO(type, responseJSON));
                /* Save the acquired JSON */
                CasesFragment.saveToSharedPrefs(LawyerDiaryActivity.this);
            }
        }
        // getJsonFromServer();
        Log.d("JSON ", json);

        // set up ViewPager
        viewPager.setAdapter(new LawyerViewPagerAdapter(getSupportFragmentManager(), this));

        // combine tab layout with the view pager
        // tabLayout.setupWithViewPager(viewPager);

        // on click listener to fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LawyerDiaryActivity.this, AddCaseActivity.class);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(LawyerDiaryActivity.this, fab, "fab_translate");
                startActivity(intent, activityOptionsCompat.toBundle());
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CasesFragment.saveToSharedPrefs(LawyerDiaryActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* After you resume this from AddCaseActivity */
        sendJsonToServer(LawyerDiaryActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
