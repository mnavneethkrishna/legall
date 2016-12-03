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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whattabiz.legall.Lawyers;
import com.whattabiz.legall.R;
import com.whattabiz.legall.activity.InternationalActivity;
import com.whattabiz.legall.adapters.LawyerListAdapter;
import com.whattabiz.legall.models.AdvocateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class InternationalLawyerFragment extends Fragment {

    private List<AdvocateModel> advocateModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LawyerListAdapter mAdapter;

    ProgressBar loading;

    public static InternationalLawyerFragment fragment = new InternationalLawyerFragment();

    public InternationalLawyerFragment() {
        // Required empty public constructor
    }

    public static InternationalLawyerFragment getInstance() {

        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_international_lawyer, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        mAdapter = new LawyerListAdapter(advocateModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        loading = (ProgressBar) rootView.findViewById(R.id.loading);

        if (Lawyers.getInstance().state!=null){
            createList(Lawyers.getInstance().cityList.get(1));
        }else {
           // new LoadLawyers().execute();
        }

        return rootView;
    }

    public static InternationalLawyerFragment newInstance() {
        InternationalLawyerFragment fragment = new InternationalLawyerFragment();
        return fragment;
    }



    public void createList(final String city){

        recyclerView.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        advocateModelList.clear();

        Log.i("Legall", city);


        final Lawyers ls = Lawyers.getInstance();


        {

            String url = "http://legall.co.in/web/internationalLawyer.php?key=Legall&city="+city;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Success handle response

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
            Volley.newRequestQueue(getContext()).add(stringRequest);


        }


        Log.i("Legall","tits  "+ Lawyers.getInstance().sortedLawyer.keySet().toString());



        Log.i("Legall",""+advocateModelList.size());



        ((InternationalActivity)getActivity()).loadSuccess();
        ((InternationalActivity)getActivity()).showFab();

    }






}
