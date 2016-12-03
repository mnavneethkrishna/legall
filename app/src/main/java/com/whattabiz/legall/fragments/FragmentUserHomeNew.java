package com.whattabiz.legall.fragments;

/**
 * New Fragment for userHome
 */


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whattabiz.legall.R;
import com.whattabiz.legall.User;
import com.whattabiz.legall.adapters.ViewPagerAdapter;
import com.whattabiz.legall.activity.ActsActivity;
import com.whattabiz.legall.activity.ArticlesActivity;
import com.whattabiz.legall.activity.FindLawyerActivity;
import com.whattabiz.legall.activity.InternationalActivity;
import com.whattabiz.legall.activity.PropertyActivity;
import com.whattabiz.legall.activity.StartupActivity;
import com.whattabiz.legall.models.SlideModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentUserHomeNew extends Fragment {


    //for horizontal Slider


    FrameLayout sliderPart;
    LinearLayout connecting;

    private static List<SlideModel> slideList = new ArrayList<>();
    private ViewPager intro_images;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;

    public FragmentUserHomeNew() {
        // Required empty public constructor
    }

    public static FragmentUserHomeNew newInstance() {
        FragmentUserHomeNew fragment = new FragmentUserHomeNew();

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (User.name == null){
            User.loadUserData(getContext());
        }

        // Log.i("Legall", " Fragment created : FragmentUserHomeNew");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView = inflater.inflate(R.layout.fragment_user_home, container, false);

        sliderPart = (FrameLayout) rootView.findViewById(R.id.slider_part);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Startup tile
        view.findViewById(R.id.tile_startup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), StartupActivity.class));
            }
        });

        //Real estate
        view.findViewById(R.id.tile_realestate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), PropertyActivity.class));
            }
        });

        //Acts and Laws
        view.findViewById(R.id.tile_acts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), ActsActivity.class));
            }
        });


        //Articles
        view.findViewById(R.id.tile_articles).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), ArticlesActivity.class));
            }
        });


        //international relations
        view.findViewById(R.id.tile_International).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), InternationalActivity.class));
            }
        });


        //Litigation
        view.findViewById(R.id.tile_litigtion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), FindLawyerActivity.class));
            }
        });



        connecting = (LinearLayout) view.findViewById(R.id.connecting);

        mAdapter = new ViewPagerAdapter(getContext(), slideList);
        intro_images = (ViewPager) view.findViewById(R.id.pager_introduction);

        //intro_images.setCurrentItem(0);



        dots = new ImageView[4];
        dots[0] = (ImageView) view.findViewById(R.id.b1);
        dots[1] = (ImageView) view.findViewById(R.id.b2);
        dots[2] = (ImageView) view.findViewById(R.id.b3);
        dots[3] = (ImageView) view.findViewById(R.id.b4);



        intro_images.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Log.i("Legall","pos "+position);

                for (int i = 0; i < 4; i++) {

                    dots[i].setImageResource(R.drawable.unselected_dot);
                }

                dots[position].setImageResource(R.drawable.selected_dot);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (slideList.size()==0) {
            sliderPart.setVisibility(View.GONE);
            new LoadSlider().execute();
        }else {
            intro_images.setAdapter(mAdapter);
            connecting.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }









    }



    int i;
    User us;
    //method to get the slider
    private class LoadSlider extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... strings) {


            slideList.clear();

            String url = "http://legall.co.in/web/articles.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Success handle response

                            Log.i("Legall", response);

                            try {




                                JSONObject object = new JSONObject(response);
                                JSONArray postsArray = new JSONArray(object.getJSONArray("top").toString());


                                if (postsArray != null) {
                                    // JSONArray postsArray = obj.getJSONArray("posts");


                                    for (int i = 0; i < 4; i++) {
                                        final JSONObject itemObj = postsArray.getJSONObject(i);
                                        SlideModel slide = new SlideModel(itemObj.getString("title"),itemObj.getString("description"),itemObj.getString("img").replaceAll(" ","%20"),itemObj.getString("pdf").replaceAll(" ","%20").replaceAll(" ","%20"));
                                        slideList.add(slide);
                                    }






                                    intro_images.setAdapter(mAdapter);

                                } else {

                                    Toast.makeText(getContext(), "Againnnn", Toast.LENGTH_LONG).show();
                                }


                                User.getInstance(getContext()).status=1;
                                mAdapter.notifyDataSetChanged();
                                sliderPart.setVisibility(View.VISIBLE);
                                connecting.setVisibility(View.GONE);

                            } catch (JSONException ex) {
                                // Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
                                ex.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Error handle error
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> param = new HashMap<String, String>();


                    param.put("key", "Legall");
                    param.put("cname", "0");

                    Log.i("Legall", "index " + 0);


                    return param;
                }

            };;

            // Add the request to the queue
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getContext()).add(stringRequest);



            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //propic.setImageBitmap(bitmap);
        }
    }



/*
    int i;
    User us;
    //method to get the slider
    private class LoadSlider extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... strings) {


            slideList.clear();

            String url = "http://whattabiz.com/Legall2/Android/articles.php?key=Whattabizlegall&cat=top";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Success handle response

                            Log.i("Legall", response);

                            try {
                                JSONArray postsArray = new JSONArray(response);

                                if (postsArray != null) {
                                    // JSONArray postsArray = obj.getJSONArray("posts");


                                    us = User.getInstance(getContext());

                                    slideList.clear();
                                    for (i = 0; i<4; i++) {

                                        final JSONObject itemObj = postsArray.getJSONObject(i);
                                        SlideModel slide = new SlideModel(itemObj.getString("title"),itemObj.getString("description"),itemObj.getString("img").replaceAll(" ","%20"),itemObj.getString("pdf").replaceAll(" ","%20").replaceAll(" ","%20"));
                                        slideList.add(slide);

                                    }


                                    intro_images.setAdapter(mAdapter);

                                } else {

                                    Toast.makeText(getContext(), "Againnnn", Toast.LENGTH_LONG).show();
                                }


                                User.getInstance(getContext()).status=1;
                                mAdapter.notifyDataSetChanged();
                                sliderPart.setVisibility(View.VISIBLE);
                                connecting.setVisibility(View.GONE);

                            } catch (JSONException ex) {
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



            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //propic.setImageBitmap(bitmap);
        }
    }

*/


    /*

    int i;
    User us;
    //method to get the slider images and titles
    private class LoadSlider extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... strings) {


            slideList.clear();

            String url = "http://whattabiz.com/Legall2/Android/articles.php?key=Whattabizlegall&cat=top";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Success handle response

                            Log.i("Legall", response);

                            try {
                                JSONArray postsArray = new JSONArray(response);

                                if (postsArray != null) {
                                    // JSONArray postsArray = obj.getJSONArray("posts");


                                    us = User.getInstance(getContext());

                                    slideList.clear();

                                    for (i = 0; i<4; i++) {

                                        final JSONObject itemObj = postsArray.getJSONObject(i);
                                        final  SlideModel slide = new SlideModel(itemObj.getString("title"),itemObj.getString("img"),itemObj.getString("pdf"));
                                        if (User.getInstance(getContext()).slideImages.size() < 4) {

                                            Log.i("Legall", "inside if");

                                            Picasso.with(getContext()).load(Uri.parse(itemObj.getString("img"))).into(new Target() {


                                                @Override
                                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                    User.getInstance(getContext()).slideImages.add(i,bitmap);

                                                    slideList.add(i,slide);

                                                    if (User.slideImages.size()==4) {
                                                        User.getInstance(getContext()).status = 1;
                                                        mAdapter.notifyDataSetChanged();
                                                        User.getInstance(getContext()).status=1;
                                                        sliderPart.setVisibility(View.VISIBLE);
                                                    }

                                                }

                                                @Override
                                                public void onBitmapFailed(Drawable errorDrawable) {

                                                }

                                                @Override
                                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                }
                                            });

                                        }


                                    }



                                } else {

                                }



                            } catch (JSONException ex) {
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



            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //propic.setImageBitmap(bitmap);
        }
    }


*/

}
