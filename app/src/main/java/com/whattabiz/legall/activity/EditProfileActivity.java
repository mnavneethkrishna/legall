package com.whattabiz.legall.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whattabiz.legall.R;
import com.whattabiz.legall.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EditProfileActivity extends AppCompatActivity {

    Bundle extra;
    LinearLayout lawyerPart;

    EditText name, email, phone, officeAddress, exp, lawyerId, oldPass, newPass, confirmPass;
    Button updateProfile, changePassword, editSpec;
    TextView spec_count;
    Spinner spnr_city;

    List<String> city = new ArrayList<String>();


    int userType;
    ArrayList selectedItems = new ArrayList();
    ArrayAdapter<String> cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        //initially hiding setting lawyer part fields
        lawyerPart = (LinearLayout) findViewById(R.id.lawyer_part);
        lawyerPart.setVisibility(View.GONE);

        //checking for type of user
        extra = getIntent().getExtras();
        userType = extra.getInt("type");
        if (userType == 1) {

            //get profile details from the server and update the edit texts
            //enabling lawyer part
            lawyerPart.setVisibility(View.VISIBLE);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");


        name = (EditText) findViewById(R.id.etxt_name);
        email = (EditText) findViewById(R.id.etxt_email);
        phone = (EditText) findViewById(R.id.etxt_phone);
        officeAddress = (EditText) findViewById(R.id.etxt_office_address);
        exp = (EditText) findViewById(R.id.etxt_exp);
        lawyerId = (EditText) findViewById(R.id.etxt_lawyer_id);


        spnr_city = (Spinner) findViewById(R.id.spnr_city);


        //retrieve user data to fields
        if (userType == 0) {


        }
        updateProfile = (Button) findViewById(R.id.save_profile);
        //changePassword = (Button) findViewById(R.id.change_pass);


        spnr_city = (Spinner) findViewById(R.id.spnr_city);

        setUpdateProfile();


        city.add("Choose your city");

        //checking for internet connection
        if (IsConnected()) {
            loadCityList();
        } else {

            final AppCompatDialog dialog = new AppCompatDialog(EditProfileActivity.this);
            dialog.setContentView(R.layout.dialog_no_internet);
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


        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //form validation
                if (name.getText().toString().matches("") || name.getText().toString().length() < 5) {
                    name.setError("Name too short");
                    name.requestFocus();
                    return;
                } else if (email.getText().toString().matches("")) {
                    email.setError("Email Required");
                    email.requestFocus();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {

                    email.setError("Invalid Email Address");
                    email.requestFocus();
                    return;
                } else if (phone.getText().toString().length() < 10) {
                    phone.setError("Invalid Phone Number");
                    phone.requestFocus();
                    return;
                } else if (spnr_city.getSelectedItemPosition() == 0) {
                    ((TextView) spnr_city.getSelectedView()).setError("Please select a city");
                    return;
                } else if (userType == 1) {

                    //only for lawyer part
                    if (officeAddress.getText().length() < 10) {
                        officeAddress.setError("Invalid Address");
                        officeAddress.requestFocus();
                        return;
                    } else if (exp.getText().toString().matches("")) {
                        exp.setError("Invalid Experience Year");
                        exp.requestFocus();
                        return;
                    } else if (!android.text.TextUtils.isDigitsOnly(exp.getText())) {
                        exp.setError("Only Numbers are Allowed");
                        exp.requestFocus();
                        return;
                    } else if (exp.getText().toString().length() > 2 || Integer.valueOf(exp.getText().toString()) > 30) {
                        exp.setError("Invalid years of Experience");
                        exp.requestFocus();

                        return;
                    }

                }


                if (User.type.matches("0")) {

                    editProfile(name.getText().toString(), email.getText().toString(), phone.getText().toString(), spnr_city.getSelectedItem().toString(), null, null, null);
                } else {

                    editProfile(name.getText().toString(), email.getText().toString(), phone.getText().toString(), spnr_city.getSelectedItem().toString(), lawyerId.getText().toString().trim(), officeAddress.getText().toString().trim(), exp.getText().toString());

                }


            }
        });


        //Toast.makeText(this, type,Toast.LENGTH_LONG).show();


        /**
         * update the contents from backend to this array list
         * for city spinner
         */

        cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, city);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_city.setAdapter(cityAdapter);


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

    //to load city list
    public void loadCityList() {

        final ProgressDialog pd = new ProgressDialog(EditProfileActivity.this);
        pd.setMessage("Please wait..");
        pd.setCancelable(false);
        pd.show();


        String url = "http://legall.co.in/web/city.php?id=1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Success handle response

                        pd.dismiss();

                        Log.i("Legall", response);

                        try {

                            JSONObject object = new JSONObject(response);
                            String status = object.getString("message");

                            if (status.matches("Success")) {


                                JSONArray cityArray = object.getJSONArray("city");

                                for (int i = 0; i < cityArray.length(); i++) {

                                    JSONObject obj = cityArray.getJSONObject(i);
                                    city.add(obj.getString("city_name"));

                                }

                                if (city.contains(User.city))
                                    spnr_city.setSelection(city.indexOf(User.city));

                            } else {


                                //registration form is being rejected by the server


                                final AppCompatDialog dialog = new AppCompatDialog(EditProfileActivity.this);
                                dialog.setContentView(R.layout.dialog_invalid_form);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                TextView message = (TextView) dialog.findViewById(R.id.txt_message);
                                message.setText("Couldn't fetch the data from the server. Please contact the customer care or try again after sometimes.");


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

                final AppCompatDialog dialog = new AppCompatDialog(EditProfileActivity.this);
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
        });

        // Add the request to the queue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(EditProfileActivity.this).add(stringRequest);

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

    public void setUpdateProfile() {

        name.setText(User.name);
        email.setText(User.email);
        phone.setText(User.phone);


        Bundle b = getIntent().getExtras();
        userType = b.getInt("type");
        if (userType == 1) {

            officeAddress.setText(User.officeAddress);
            lawyerId.setText(User.lawyerId);
            exp.setText(User.exp);

        }
    }


    public void editProfile(final String name, final String email, final String phone, final String city, final String lawyerid, final String office, final String exp) {

        final String url = "http://legall.co.in/web/editProfile.php";


        final ProgressDialog pd = new ProgressDialog(EditProfileActivity.this);
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

                            if (status == 1) {

                                final AlertDialog.Builder dialog = new AlertDialog.Builder(EditProfileActivity.this);
                                dialog.setMessage("Your Profile details has been updated.. please re-login to your account to continue..");
                                dialog.setTitle("Success");
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //clearing user data and preparing for login

                                        User.clear();

                                        SharedPreferences.Editor edit = getSharedPreferences("user",MODE_PRIVATE).edit();
                                        edit.clear().commit();

                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                    }
                                });

                                dialog.show();
                            } else {


                                //registration form is being rejected by the server

                                pd.dismiss();

                                final AppCompatDialog dialog = new AppCompatDialog(EditProfileActivity.this);
                                dialog.setContentView(R.layout.dialog_invalid_form);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                TextView message = (TextView) dialog.findViewById(R.id.txt_message);
                                message.setText("Please check the details provided and try again.");


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

                final AppCompatDialog dialog = new AppCompatDialog(EditProfileActivity.this);
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


                param.put("profession", User.type);
                param.put("user_id", User.Id);
                param.put("name", name.trim());
                param.put("email", email.trim());
                param.put("phone", phone.trim());
                param.put("city", city.trim());


                if (!User.type.matches("0")) {

                    param.put("lawyerId", User.lawyerId);
                    param.put("exp", exp);
                    param.put("office", office.trim());
                }

                return param;
            }

        };

        // Add the request to the queue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(EditProfileActivity.this).add(stringRequest);


    }
}
