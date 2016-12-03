package com.whattabiz.legall.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whattabiz.legall.Lawyers;
import com.whattabiz.legall.R;
import com.whattabiz.legall.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
*       welcome screen Activity
 */

public class VerificationWelcome extends AppCompatActivity {

    TextView splashText;
    Button btn_signup,btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_welcome);
        Log.i("Legall","Current activity: VerificationWelcome");
        Log.i("Legall","Layout set: activity_verification_welcome.xml");



        //setting typeface for legall text
        splashText=(TextView) findViewById(R.id.legall_text);
        String fontPath="fonts/PoiretOne.ttf";
        Typeface tf=Typeface.createFromAsset(getAssets(),fontPath);
        splashText.setTypeface(tf);


        new updateCheck().execute();


        btn_signup=(Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VerificationWelcome.this,Registration.class));
                finish();
            }
        });

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VerificationWelcome.this,LoginActivity.class));
                finish();
            }
        });


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
                                        AlertDialog.Builder update = new AlertDialog.Builder(VerificationWelcome.this);
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
            Volley.newRequestQueue(VerificationWelcome.this).add(stringRequest);


            return null;
        }

        @Override
        protected void onPostExecute(String result) {


        }
    }

}
