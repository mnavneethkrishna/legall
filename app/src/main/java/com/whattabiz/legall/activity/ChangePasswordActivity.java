package com.whattabiz.legall.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePasswordActivity extends AppCompatActivity {


    EditText password, repass, oldpass;
    Button changepass;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Change Password");

        password = (EditText) findViewById(R.id.etxt_new_password);
        repass = (EditText) findViewById(R.id.etxt_confirm_new_password);
        oldpass = (EditText) findViewById(R.id.etxt_password);


        changepass = (Button) findViewById(R.id.change_pass);
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if (oldpass.getText().toString().matches("")){
                    oldpass.setError("Invalid password");
                    oldpass.requestFocus();
                    return;
                }else if (oldpass.getText().toString().length()<6){
                    oldpass.setError("too short");
                    oldpass.requestFocus();
                    return;
                }else if (password.getText().toString().matches("")){
                    password.setError("Invalid password");
                    password.requestFocus();
                    return;
                }else if (password.getText().toString().length()<6){
                    password.setError("too short");
                    password.requestFocus();
                    return;
                }else if (repass.getText().toString().matches("")){
                    repass.setError("Invalid password");
                    repass.requestFocus();
                    return;
                }else if (!repass.getText().toString().matches(password.getText().toString())){
                    repass.setError("Password dosen't match");
                    repass.requestFocus();
                    return;
                }


                final ProgressDialog pd = new ProgressDialog(ChangePasswordActivity.this);
                pd.setMessage("Changing Password...");
                pd.setCancelable(false);

                if (IsConnected()){

                    pd.show();

                    url = "http://www.legall.co.in/web/passwordReset.php?key=Legall&user_id="+ User.getInstance(ChangePasswordActivity.this).Id+"&old="+oldpass.getText().toString();
                    url = url+"&new="+password.getText().toString()+"&profession="+User.getInstance(ChangePasswordActivity.this).type;


                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    pd.dismiss();
                                    Log.i("Legall","Login URL : "+url);
                                    Log.i("Legall","Login responce : "+response);

                                    //Success handle response

                                    try{
                                        JSONObject obj = new JSONObject(response);
                                        if (obj.getInt("status")==1){

                                            Toast.makeText(ChangePasswordActivity.this,"Password successfully changed",Toast.LENGTH_LONG);
                                            finish();

                                        }else{
                                            oldpass.setError("Wrong Password");
                                            oldpass.requestFocus();
                                            Toast.makeText(ChangePasswordActivity.this,"wrong old password dosen't match",Toast.LENGTH_LONG);
                                            return;
                                        }
                                    }catch(JSONException e){
                                        e.printStackTrace();
                                    }



                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                          pd.dismiss();

                            Log.e("Legall",error.toString());


                            final AppCompatDialog dialog = new AppCompatDialog(ChangePasswordActivity.this);
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
                    Volley.newRequestQueue(ChangePasswordActivity.this).add(stringRequest);



                }else{

                    pd.dismiss();
                    final AppCompatDialog dialog = new AppCompatDialog(ChangePasswordActivity.this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return true;
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
