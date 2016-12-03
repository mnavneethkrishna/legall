package com.whattabiz.legall.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whattabiz.legall.R;
import com.whattabiz.legall.adapters.ArticleListAdapter;
import com.whattabiz.legall.models.ArticleListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ArticlesListFragment extends Fragment {

    // TODO: Rename and change types of parameters

    private List<ArticleListModel> articleList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ArticleListAdapter mAdapter;

    ProgressBar loading;
    LinearLayout nofile;
    String header = null;


    private static final String ARG_SECTION_NUMBER = "section_number";

    private int mParam;
    private OnFragmentInteractionListener mListener;

    public ArticlesListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ArticlesListFragment newInstance(int sectionNumber) {
        ArticlesListFragment fragment = new ArticlesListFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getInt(ARG_SECTION_NUMBER);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_articles_list, container, false);

        loading = (ProgressBar) rootView.findViewById(R.id.loading);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        mAdapter = new ArticleListAdapter(articleList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        nofile = (LinearLayout) rootView.findViewById(R.id.no_file);
        nofile.setVisibility(View.GONE);

        prepareArticleList("" + (mParam - 1));
        return rootView;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void prepareArticleList(final String index) {

        articleList.clear();


        switch (Integer.valueOf(index)){

            case 0:
                header = "top";
                break;
            case 1:
                header = "featured";
                break;
            case 2:
                header = "trending";
                break;

        }
        //params title, description,img,pdf


        final String url = "http://legall.co.in/web/articles.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Success handle response


                        Log.i("Legall","Request : "+url+"  cname : "+index+" h : "+header);
                        Log.i("Legall", response);

                        try {


                            JSONObject object = new JSONObject(response);
                            JSONArray postsArray = new JSONArray(object.getJSONArray(header).toString());


                            if (postsArray != null) {
                                // JSONArray postsArray = obj.getJSONArray("posts");


                                for (int i = 0; i < postsArray.length(); i++) {
                                    JSONObject itemObj = postsArray.getJSONObject(i);

                                    //adding each json data to list
                                    ArticleListModel content = new ArticleListModel(itemObj.getString("title"), itemObj.getString("description"), itemObj.getString("img").replaceAll(" ", "%20").replaceAll("\\\\",""),itemObj.getString("pdf").replaceAll(" ", "%20").replaceAll("\\\\",""), itemObj.getString("date"));
                                    articleList.add(content);
                                }

                                mAdapter.notifyDataSetChanged();

                            } else {


                                //registration form is being rejected by the server


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


                        } catch (JSONException e) {
                            e.printStackTrace();

                            nofile.setVisibility(View.VISIBLE);
                        }

                        loading.setVisibility(View.GONE);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Error handle error


                loading.setVisibility(View.GONE);
                nofile.setVisibility(View.VISIBLE);


                final AppCompatDialog dialog = new AppCompatDialog(getContext());
                dialog.setContentView(R.layout.dialog_no_internet);
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();


                param.put("key", "Legall");
                param.put("cname", index);

                Log.i("Legall", "index " + index);


                return param;
            }

        };

        // Add the request to the queue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getContext()).add(stringRequest);


//             String url = "http://whattabiz.com/Legall2/Android/articles.php?key=Whattabizlegall&cat=top";
//
//             StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                     new Response.Listener<String>() {
//                         @Override
//                         public void onResponse(String response) {
//                             //Success handle response
//
//                             Log.i("Legall", response);
//
//                             try {
//                                 JSONArray postsArray = new JSONArray(response);
//
//                                 if (postsArray != null) {
//                                     // JSONArray postsArray = obj.getJSONArray("posts");
//
//
//                                     for (int i = 0; i < postsArray.length(); i++) {
//                                         JSONObject itemObj = postsArray.getJSONObject(i);
//
//                                         //adding each json data to list
//                                         ArticleListModel content = new ArticleListModel(itemObj.getString("title"), itemObj.getString("description"), itemObj.getString("img").replaceAll(" ","%20"), itemObj.getString("pdf").replaceAll(" ","%20"), itemObj.getString("date"));
//                                         articleList.add(content);
//
//                                     }
//
//
//                                 } else {
//
//                                     Toast.makeText(getContext(), "Againnnn", Toast.LENGTH_LONG).show();
//                                 }
//
//                                 mAdapter.notifyDataSetChanged();
//                                 loading.setVisibility(View.GONE);
//                                 recyclerView.setVisibility(View.VISIBLE);
//                             } catch (JSONException ex) {
//                                 // Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
//                                 ex.printStackTrace();
//                             }
//
//
//                         }
//                     }, new Response.ErrorListener() {
//                 @Override
//                 public void onErrorResponse(VolleyError error) {
//                     //Error handle error
//                 }
//             });
//
//             // Add the request to the queue
//             stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//             Volley.newRequestQueue(getContext()).add(stringRequest);
//


    }
}
