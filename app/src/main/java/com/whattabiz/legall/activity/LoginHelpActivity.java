package com.whattabiz.legall.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whattabiz.legall.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginHelpActivity extends AppCompatActivity {


    EditText email;
    Button pass, mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_help);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Help center");

        pass = (Button) findViewById(R.id.forgot_pass);
        mail = (Button) findViewById(R.id.resend_email);
        email = (EditText) findViewById(R.id.email);


        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().matches("")) {
                    email.setError("Enter an email");
                    email.requestFocus();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {

                    email.setError("Invalid Email Address");
                    email.requestFocus();
                    return;
                }

                forgotPassword(email.getText().toString().trim());

            }
        });


        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (email.getText().toString().matches("")) {
                    email.setError("Enter an email");
                    email.requestFocus();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {

                    email.setError("Invalid Email Address");
                    email.requestFocus();
                    return;
                }

                resendEmail(email.getText().toString().trim());
            }
        });
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    public void forgotPassword(final String email) {

        final String url = "http://legall.co.in/web/forgetPassword.php";


        final ProgressDialog pd = new ProgressDialog(LoginHelpActivity.this);
        pd.setMessage("Please wait..");
        pd.setCancelable(false);
        pd.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Success handle response

                        pd.dismiss();

                        Log.i("Legall", response);

                        try {

                            JSONObject object = new JSONObject(response);
                            int status = object.getInt("status");

                            if (status == 2) {

                                final AlertDialog.Builder dialog = new AlertDialog.Builder(LoginHelpActivity.this);
                                dialog.setMessage("We've sent a recovery link to " + email + ", please follow the instructions in the mail to recover your account.");
                                dialog.setTitle("Success");
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                        finish();

                                    }
                                });

                                dialog.show();
                            } else if(status == 0){


                                //registration form is being rejected by the server

                                pd.dismiss();

                                final AppCompatDialog dialog = new AppCompatDialog(LoginHelpActivity.this);
                                dialog.setContentView(R.layout.dialog_invalid_form);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                TextView message = (TextView) dialog.findViewById(R.id.txt_message);
                                message.setText("Your email is not registered. Please create a new account to continue to Legall.");
                                Button back = (Button) dialog.findViewById(R.id.btn_back);
                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View view) {

                                        dialog.dismiss();
                                        finish();
                                        return;
                                    }


                                });

                                dialog.show();
                                return;

                            }else {

                                pd.dismiss();

                                final AppCompatDialog dialog = new AppCompatDialog(LoginHelpActivity.this);
                                dialog.setContentView(R.layout.dilog_request_error);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                                Button back = (Button) dialog.findViewById(R.id.btn_back);
                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View view) {

                                        dialog.dismiss();
                                        finish();
                                        return;
                                    }


                                });

                                dialog.show();
                                return;


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Error handle error


                pd.dismiss();

                final AppCompatDialog dialog = new AppCompatDialog(LoginHelpActivity.this);
                dialog.setContentView(R.layout.dilog_request_error);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                Button back = (Button) dialog.findViewById(R.id.btn_back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        dialog.dismiss();
                        finish();
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


                param.put("email", email.trim());
                param.put("key", "Legall");


                return param;
            }

        };

        // Add the request to the queue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(LoginHelpActivity.this).add(stringRequest);


    }



    public void resendEmail(final String email) {

        final String url = "http://legall.co.in/web/resendLink.php";


        final ProgressDialog pd = new ProgressDialog(LoginHelpActivity.this);
        pd.setMessage("Please wait..");
        pd.setCancelable(false);
        pd.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Success handle response

                        pd.dismiss();

                        Log.i("Legall", response);

                        try {

                             JSONObject object = new JSONObject(response);
                            int status = object.getInt("status");

                            if (status == 2) {

                                //email sent

                                final AlertDialog.Builder dialog = new AlertDialog.Builder(LoginHelpActivity.this);
                                dialog.setMessage("We've sent a verification link to " + email + ", please follow the instructions in the mail to verify your email.");
                                dialog.setTitle("Success");
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                        finish();

                                    }
                                });

                                dialog.show();
                            } else if (status == 3){


                                //Email already registered

                                pd.dismiss();

                                final AppCompatDialog dialog = new AppCompatDialog(LoginHelpActivity.this);
                                dialog.setContentView(R.layout.dialog_registration_success);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                TextView title = (TextView) dialog.findViewById(R.id.title);
                                title.setText("verified email");

                                TextView message = (TextView) dialog.findViewById(R.id.caption);
                                message.setText("your email id is already verified. please login to your account to continue.");


                                Button back = (Button) dialog.findViewById(R.id.btn_back);
                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View view) {

                                        dialog.dismiss();
                                        finish();
                                        return;
                                    }


                                });

                                dialog.show();
                                return;

                            }else if(status == 0){



                                final AlertDialog.Builder dialog = new AlertDialog.Builder(LoginHelpActivity.this);
                                dialog.setMessage("Your Email id is not yet registered. Please register a new account to continue.");
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                        finish();

                                    }
                                });

                            }else {

                                final AppCompatDialog dialog = new AppCompatDialog(LoginHelpActivity.this);
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

                        } catch (JSONException e) {

                            Log.e("Legall",e.toString());
                            e.printStackTrace();

                            final AppCompatDialog dialog = new AppCompatDialog(LoginHelpActivity.this);
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


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Error handle error


                pd.dismiss();

                final AppCompatDialog dialog = new AppCompatDialog(LoginHelpActivity.this);
                dialog.setContentView(R.layout.dilog_request_error);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                Button back = (Button) dialog.findViewById(R.id.btn_back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        dialog.dismiss();
                        finish();
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


                param.put("email", email.trim());
                param.put("key", "Legall");


                return param;
            }

        };

        // Add the request to the queue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(LoginHelpActivity.this).add(stringRequest);


    }
}
