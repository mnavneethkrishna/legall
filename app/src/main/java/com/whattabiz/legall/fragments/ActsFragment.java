package com.whattabiz.legall.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.whattabiz.legall.activity.ActsActivity;
import com.whattabiz.legall.adapters.ActsListAdapter;
import com.whattabiz.legall.models.ActsListModel;
import com.whattabiz.legall.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActsFragment extends Fragment {



    ProgressBar loadingBar;

    private List<ActsListModel> actsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ActsListAdapter mAdapter;

    private static final String ARG_SECTION_NUMBER = "section_number";



    // TODO: Rename and change types of parameters
    private int mParam1;

    private OnFragmentInteractionListener mListener;

    public ActsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ActsFragment newInstance(int sectionNumber) {

        ActsFragment fragment = new ActsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // use this argument to generate diferent data for seperate fragments
        //retrieve data needed data from the backend and update the new fragment before returning
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_SECTION_NUMBER);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_acts, container, false);

        loadingBar = (ProgressBar) rootView.findViewById(R.id.loading);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        mAdapter = new ActsListAdapter(actsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        //recyclerView.setVisibility(View.GONE);



        //replace....... prepare the recycler data or each fragment according to the parameters got from the activity
       prepareContentListReg();
        return rootView;
    }



    //replace this with switched data for the parameter so as different data for each fragment thts
    //being sent back to the activity
    private void prepareContentListReg() {


        SharedPreferences preferences = getContext().getSharedPreferences("ACTS",Context.MODE_PRIVATE);
        String title = preferences.getString("head"+(mParam1-1),"null");
        final String id = preferences.getString("id"+(mParam1-1),"null");
        final String requestString = title.trim().replaceAll(" ","%20");
        Toast.makeText(getContext(),""+requestString,Toast.LENGTH_LONG);
        actsList.clear();



        final String url = "http://legall.co.in/web/actsnLawsSubCat.php?key=Legall&cat="+id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Success handle response

                        Log.i("Legall",url.toString());
                        Log.i("Legall",response);

                        try {
                            JSONArray postsArray = new JSONArray(response);

                            if(postsArray!=null) {
                                // JSONArray postsArray = obj.getJSONArray("posts");


                                for(int i=0; i<postsArray.length(); i++) {
                                    JSONObject itemObj = postsArray.getJSONObject(i);

                                    //adding each json data to list
                                    ActsListModel content = new ActsListModel(itemObj.getString("subcategory"),itemObj.getString("id"),"",id);
                                    actsList.add(content);

                                    ((ActsActivity)getContext()).suggestions.add(itemObj.getString("subcategory"));

                                }

                                ((ActsActivity)getContext()).searchView.setSuggestions(((ActsActivity)getContext()).suggestions.toArray(new String[0]));

                            }
                            else {

                                Toast.makeText(getContext(),"Againnnn",Toast.LENGTH_LONG).show();
                            }

                            mAdapter.notifyDataSetChanged();
                            loadingBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
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


    }

 

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
