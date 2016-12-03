package com.whattabiz.legall.activity;

/**
 * Activity for login
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.input.InputManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whattabiz.legall.R;
import com.whattabiz.legall.User;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    Button btn_login, need_help;
    EditText email,password;
    ProgressBar loading;
    LinearLayout items,helpView;
    String url;
    Boolean emailFocusState=false,passFocusState=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = (EditText) findViewById(R.id.etxt_email);
        password = (EditText) findViewById(R.id.etxt_password);
        loading = (ProgressBar) findViewById(R.id.loading);
        items = (LinearLayout) findViewById(R.id.items);
        helpView = (LinearLayout) findViewById(R.id.help_view);





        //initially hiding the loading bar
        loading.setVisibility(View.GONE);

        need_help = (Button) findViewById(R.id.need_help);
        need_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,LoginHelpActivity.class));
            }
        });

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (IsConnected()) {


                    if (email.getText().toString().trim().matches("")) {
                        email.setError("Enter Email");
                        email.requestFocus();
                        return;
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                        email.setError("Invalid Email");
                        email.requestFocus();
                        return;
                    } else if (password.getText().toString().matches("")) {
                        password.setError("Enter Password");
                        password.requestFocus();
                        return;
                    } else {

                        //send data to server for verification
                        if (email.isFocused() || password.isFocused()) {
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        }
                        startLoading();
                        login(email.getText().toString(), password.getText().toString());

                    }
                }else{
                    final AppCompatDialog dialog = new AppCompatDialog(LoginActivity.this);
                    dialog.setContentView(R.layout.dialog_no_internet);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                   Button back =(Button) dialog.findViewById(R.id.btn_back);
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {

                            dialog.dismiss();
                            return;
                        }



                });

                    dialog.show();
                    return;
                }

            }
        });


    }


    //disable edit text and show loading bar
    public  void startLoading(){
        items.setVisibility(View.GONE);
        btn_login.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }


    //enable edit text and show button
    public void showItems(){
        items.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    //connect to server for login
    public void login(String email, String password){


        url = "http://www.legall.co.in/web/login.php?key=Legall&email="+email.trim()+"&password="+password;
            //String url = "http://data.colorado.gov/resource/4ykn-tg5h.json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("Legall","Login responce : "+response);

                        //Success handle response
                        try {
                            JSONObject obj = new JSONObject(response);

                            Integer status = obj.getInt("status");

                            if(status==0){
                                Toast.makeText(LoginActivity.this,"Invalid Email / Password",Toast.LENGTH_LONG).show();
                                showItems();
                            }else if (status == 1){
                                saveUserData(obj);
                                selectUser(obj);
                            }else if (status == 2){

                                final AppCompatDialog dialog = new AppCompatDialog(LoginActivity.this);
                                dialog.setContentView(R.layout.dialog_verify_email);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                                Button back =(Button) dialog.findViewById(R.id.btn_back);
                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View view) {

                                        dialog.dismiss();
                                        showItems();
                                        return;
                                    }



                                });

                                dialog.show();
                                return;
                            }


                        }
                        catch(JSONException ex) {
                            ex.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                showItems();

                Log.e("Legall",error.toString());


                final AppCompatDialog dialog = new AppCompatDialog(LoginActivity.this);
                dialog.setContentView(R.layout.dilog_request_error);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                Button back =(Button) dialog.findViewById(R.id.btn_back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        dialog.dismiss();
                        return;
                    }



                });

                dialog.show();
                return;
            }
        });

        // Add the request to the queue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(stringRequest);



    }



    //for saving user data to shared preferences
    public void saveUserData(JSONObject object){


        SharedPreferences.Editor user = getSharedPreferences("user",MODE_PRIVATE).edit();
        user.clear();
        user.putString("status","true");
        user.commit();
        try{

            Log.i("Legall",object.toString());
            user.putString("userData",object.toString());
            user.putString("update","yes");

            user.putString("status","true");

            String profession = object.getString("profession");
            User.loadUserData(LoginActivity.this);
            if (Integer.valueOf(profession) == 0){
                user.putString("userType","0");
            }else if (Integer.valueOf(profession) == 1){
                user.putString("userType","1");
            }else {

                user.putString("userType","2");
            }

            user.commit();


        }catch (JSONException e){
            e.printStackTrace();
        }

        user.commit();
    }

    @Override
    public void onBackPressed() {

       startActivity(new Intent(LoginActivity.this,VerificationWelcome.class));
        finish();
    }


    public  void selectUser(JSONObject obj) {

        try {

            if (Integer.valueOf(obj.getInt("profession")) == 0) {
                Intent intent = new Intent(getApplicationContext(), UserHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else{

                Intent intent = new Intent(getApplicationContext(), LawyerHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }catch (JSONException e ){
            e.printStackTrace();
        }


    }

    //for internet connection checking
    public boolean IsConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // Connected to the Internet
                return true;
            }
        }
        return false;
    }
}
