package com.whattabiz.legall.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whattabiz.legall.adapters.LitigationMainCatAdapter;
import com.whattabiz.legall.R;
import com.whattabiz.legall.models.LitigationCategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LitigationCategoryFragment extends Fragment {

    private List<LitigationCategoryModel> categoryModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LitigationMainCatAdapter mAdapter;
    LitigationCategoryModel content;

    ProgressBar loading;


    public LitigationCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_litigation_category, container, false);

        loading = (ProgressBar) rootView.findViewById(R.id.loading);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        mAdapter = new LitigationMainCatAdapter(categoryModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setVisibility(View.GONE);



        prepareMainCategoryList();
        
        
        return rootView;
    }

    public static LitigationCategoryFragment newInstance() {
        LitigationCategoryFragment fragment = new LitigationCategoryFragment();
        return fragment;
    }
    
    public void prepareMainCategoryList(){

        categoryModelList.clear();

        content = new LitigationCategoryModel();


        String url = "http://legall.in/web/categories.php?id=1";

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

                                    JSONArray postsArray = object.getJSONArray("category");


                                    for(int i=0; i<postsArray.length(); i++) {
                                        JSONObject itemObj = postsArray.getJSONObject(i);

                                        //adding each json data to list
                                        content = new LitigationCategoryModel(itemObj.getString("title"),itemObj.getString("description"),itemObj.getString("id"));
                                        categoryModelList.add(content);
                                    }


                                }
                                else {

                                    Toast.makeText(getContext(),"Againnnn",Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(getContext()).add(stringRequest);




        mAdapter.notifyDataSetChanged();
    }

}
