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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whattabiz.legall.models.ContentItem;
import com.whattabiz.legall.adapters.ContentItemAdapter;
import com.whattabiz.legall.R;
import com.whattabiz.legall.activity.startupCompanyActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartupDocumentListFragment extends Fragment {


    private List<ContentItem> contentItemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContentItemAdapter mAdapter;
    private ProgressBar loading;

    String url = "http://legall.co.in/web/startupsubcat.php?key=Legall&cat="+ startupCompanyActivity.id;



    public StartupDocumentListFragment() {
        // Required empty public constructor
    }


    public static StartupDocumentListFragment newInstance() {
        StartupDocumentListFragment fragment = new StartupDocumentListFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_startup_document_list, container, false);



        loading = (ProgressBar) rootView.findViewById(R.id.loading);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        mAdapter = new ContentItemAdapter(contentItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        createlist();


        return rootView;


    }

    public void createlist(){

        {


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Success handle response

                            loading.setVisibility(View.GONE);

                            Log.i("Legall", response);

                            try {

                                JSONArray array = new JSONArray(response);

                                for (int i=0; i<array.length(); i++){

                                    JSONObject object = new JSONObject(array.get(i).toString());

                                    String name = object.getString("subcategory");
                                    String cost = object.getString("price");
                                    String id = object.getString("subcategory_id");

                                    ContentItem content = new ContentItem(name, "00",cost,id,startupCompanyActivity.title);
                                    contentItemList.add(content);

                                }

                                mAdapter.notifyDataSetChanged();

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

                            dialog.dismiss();
                            getActivity().finish();
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

}
