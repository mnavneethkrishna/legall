package com.whattabiz.legall.fragments;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.whattabiz.legall.User;
import com.whattabiz.legall.activity.StartupActivity;
import com.whattabiz.legall.adapters.BulletTextAdapter;
import com.whattabiz.legall.adapters.FaqListAdapter;
import com.whattabiz.legall.models.BulletText;
import com.whattabiz.legall.models.FaqModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FAQ extends Fragment {


    String url="http://legall.co.in/web/faq.php?key=Legall";

    
    private RecyclerView recyclerView;
    private FaqListAdapter mAdapter;

    ProgressBar loading;

    public FAQ() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_faq, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        loading = (ProgressBar) rootView.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);

        mAdapter = new FaqListAdapter(User.faqList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        if (User.faqList.isEmpty()) prepareFaqList();


        return rootView;

    }


    public void prepareFaqList(){


        loading.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Success handle response


                        Log.i("Legall", response);

                        try {

                            JSONArray array = new JSONArray(response);

                            for ( int i = 0; i<array.length(); i++){

                                JSONObject obj = new JSONObject(array.get(i).toString());
                                FaqModel faq = new FaqModel(obj.getString("question"),obj.getString("answer"));
                                User.faqList.add(faq);
                                
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




                final AppCompatDialog dialog = new AppCompatDialog(getContext());
                dialog.setContentView(R.layout.dilog_request_error);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                Button back = (Button) dialog.findViewById(R.id.btn_back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {


                        return;
                    }


                });

                dialog.show();
                return;


            }
        });

        // Add the request to the queue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getContext()).add(stringRequest);




    }

}
