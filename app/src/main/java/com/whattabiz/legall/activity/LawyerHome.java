package com.whattabiz.legall.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whattabiz.legall.Lawyers;
import com.whattabiz.legall.fragments.FAQ;
import com.whattabiz.legall.fragments.FragmentLawyerProfile;
import com.whattabiz.legall.R;
import com.whattabiz.legall.User;
import com.whattabiz.legall.fragments.AboutUsFragment;
import com.whattabiz.legall.fragments.ContactusFragment;
import com.whattabiz.legall.fragments.DevelopersFragment;
import com.whattabiz.legall.fragments.FragmentLawyerHome;
import com.whattabiz.legall.fragments.UserSettingsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class LawyerHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean doublepressonce = false;

    public Bitmap bitmap;

    CircleImageView propic;

    TextView username;
    FragmentManager fragmentManager;
    Fragment fragment;



    //for the sake of human Kind from old gods and new gods...
    //presenting you the warrior to save us from NAVIGATION DRAWER FORCE CLOSE
    int warrior = 0;






    Class fragmentClass;

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Legall - Lawyer");


        new updateCheck().execute();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

                if (warrior==1) {

                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out).replace(R.id.content, fragment).commit();
                }

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header = navigationView.getHeaderView(0);
        propic = (CircleImageView) header.findViewById(R.id.profile_image);
        username = (TextView) header.findViewById(R.id.username);

        setDp();
        setUsername();


        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new Fragment();
                fragmentClass = FragmentLawyerProfile.class;
                fragmentManager = getSupportFragmentManager();
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    getSupportActionBar().setTitle("Profile");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.i("Legall", "UserHome current view fragment set to UserProfile.class");
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.anim_fade_in,R.anim.anim_fade_out).replace(R.id.content,fragment).commit();
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);

                }
            }
        });


        //Setting the default fragment to view in user home
        fragment = new Fragment();
        fragmentClass = FragmentLawyerHome.class;
        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
        Log.i("Legall", "UserHome current view fragment set to FragmentLawyerHome.class");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (doublepressonce) {
                super.onBackPressed();
                return;
            }
            this.doublepressonce = true;
            Toast.makeText(this, "Press again to Exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doublepressonce = false;
                }
            }, 1500);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.tb_notify) {
            startActivity(new Intent(LawyerHome.this,ActivityNotification.class) );
            return true;
        }
        if (id == R.id.tb_cart) {

            startActivity(new Intent(LawyerHome.this,CartActivity.class) );
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        fragment = new Fragment();
        Class fragmentClass = null;
        fragmentManager = getSupportFragmentManager();


        if (id == R.id.nav_home) {
            fragmentClass = FragmentLawyerHome.class;
            getSupportActionBar().setTitle("Legall - Lawyer");
            warrior=1;
        } else if (id == R.id.nav_profile) {

            fragmentClass = FragmentLawyerProfile.class;
            getSupportActionBar().setTitle("Profile");
            warrior=1;

        } else if (id == R.id.nav_about) {
            fragmentClass = AboutUsFragment.class;
            getSupportActionBar().setTitle("Legall - Lawyer");
            warrior=1;

        } else if (id == R.id.nav_contact) {

            fragmentClass = ContactusFragment.class;
            getSupportActionBar().setTitle("Contact Us");
            warrior=1;

        } else if (id == R.id.nav_settings) {

            fragmentClass = UserSettingsFragment.class;
            getSupportActionBar().setTitle("Settings");
            warrior=1;

        }else if (id == R.id.nav_faq) {

            fragmentClass = FAQ.class;
            getSupportActionBar().setTitle("FAQ");
            warrior=1;

        } else if (id == R.id.nav_developers) {

            fragmentClass = DevelopersFragment.class;
            getSupportActionBar().setTitle("Developers");
            warrior=1;

        }


        try{
            fragment = (Fragment) fragmentClass.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);



        return true;
    }


    public  void setDp(){


        SharedPreferences profile = getSharedPreferences("profile",MODE_PRIVATE);
        String profileUri = profile.getString("propic","");

        Uri imageUri=null;

        if (profileUri!=""){

            try{

                imageUri = Uri.parse(profileUri);


            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }

            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                new LoadProfile().execute();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }




    public Bitmap getResizedBitmap(Bitmap image, int maxSize){
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width/(float)height;

        if (bitmapRatio>1){
            height = maxSize;
            width = (int) (height*bitmapRatio);
        }else{
            height = maxSize;
            width = (int) (height*bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image,width,height,true);
    }


    private class LoadProfile extends AsyncTask<String,Void,String> {



        @Override
        protected String doInBackground(String... strings) {

            bitmap = getResizedBitmap(bitmap,200);

            return null;
        }

        @Override
        protected void onPostExecute(String result){
            propic.setImageBitmap(bitmap);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart, menu);



        return true;
    }

    public void setUsername() {


        username.setText(User.getInstance(LawyerHome.this).getName());
        Log.i("Legall", "Current User : " + User.getInstance(LawyerHome.this).name);
    }

    private class updateCheck extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            final Lawyers ls = Lawyers.getInstance();


            String url = "http://legall.co.in/web/appVersion.php?os=1";



            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Success handle response

                            Log.i("Legall", response);

                            try {
                                JSONObject object = new JSONObject(response);
                                if (object != null) {

                                    Integer latestVersion = object.getInt("version");
                                    if (User.versionCheck()<latestVersion){
                                        AlertDialog.Builder update = new AlertDialog.Builder(LawyerHome.this);
                                        update.setTitle("UPDATE AVAILABLE")
                                                .setMessage("A new version of the app is available on the Playstore, please update to the latest version to get the full features of Legall.")
                                                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.whattabiz.legall")));
                                                        finish();

                                                    }
                                                }).setNegativeButton("LATER",null).show();
                                    }

                                }
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
            Volley.newRequestQueue(LawyerHome.this).add(stringRequest);


            return null;
        }

        @Override
        protected void onPostExecute(String result) {


        }
    }
}
