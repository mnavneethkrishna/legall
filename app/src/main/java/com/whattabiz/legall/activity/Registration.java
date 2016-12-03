package com.whattabiz.legall.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whattabiz.legall.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;



/*
*Activity for registration of user details
 */


public class Registration extends AppCompatActivity {

    ArrayAdapter<String> proAdapter;
    ArrayAdapter<String> cityAdapter;
    CheckBox accept;
    Animation shake;
    ProgressDialog pd;

    String url;

    LinearLayout tandc;

    Integer status;
    String status1;

    ArrayList selectedItems = new ArrayList();

    List<String> city = new ArrayList<String>();

    CircleImageView propic;
    TextView title;
    Button register_btn;
    Spinner spnr_profession, spnr_city, spnr_lawyer_loc;
    ImageButton img_upload;

    LinearLayout lawyerPart;

    HashMap<String, String> enteredData = new HashMap<String, String>();

    Bitmap bitmap;

    EditText name, email, password, lawyerId, officeAddress, exp, phone, lawyer_id_a, lawyer_id_b;

    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Log.i("Legall", "Current activity: Registration");
        Log.i("Legall", "Layout set: activity_registration.xml");

        title = (TextView) findViewById(R.id.title);
        String fontPath = "fonts/PoiretOne.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        title.setTypeface(tf);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Registration");


        tandc = (LinearLayout) findViewById(R.id.tandc);
        name = (EditText) findViewById(R.id.etxt_name);
        email = (EditText) findViewById(R.id.etxt_email);
        password = (EditText) findViewById(R.id.etxt_password);
        //lawyerId = (EditText) findViewById(R.id.etxt_lawyer_id);
        officeAddress = (EditText) findViewById(R.id.etxt_office_address);
        exp = (EditText) findViewById(R.id.etxt_exp);
        lawyer_id_a = (EditText) findViewById(R.id.etxt_lawyer_a);
        lawyer_id_b = (EditText) findViewById(R.id.etxt_lawyer_b);

        accept = (CheckBox) findViewById(R.id.accept);
        phone = (EditText) findViewById(R.id.etxt_phone);

        shake = AnimationUtils.loadAnimation(this, R.anim.shakeanim);


        final SharedPreferences.Editor profile = getSharedPreferences("profile", MODE_PRIVATE).edit();
        profile.clear();
        profile.commit();


        spnr_city = (Spinner) findViewById(R.id.spnr_city);

        city.add("Choose your city");

        loadCityList();

        //initializing city spinner
        cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, city);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_city.setAdapter(cityAdapter);


        //Profile Picture
        propic = (CircleImageView) findViewById(R.id.profile_image);
        img_upload = (ImageButton) findViewById(R.id.btn_img_upload);
        img_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, PICK_IMAGE_REQUEST);
            }
        });


        tandc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Registration.this, TermsAndConditionViewer.class);

                if (spnr_profession.getSelectedItemPosition() == 1) {
                    i.putExtra("LINK", "http://whattabiz.com/Legall2/Android/TERMS+AND+CONDITIONS-LAWYER.doc");
                } else {
                    i.putExtra("LINK", "http://whattabiz.com/Legall2/Android/TERMS+AND+CONDITIONS.docx");
                }

                startActivity(i);
            }
        });


        //Registration Button
        register_btn = (Button) findViewById(R.id.register);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (name.getText().toString().length() < 5 || !checkname(name.getText().toString())) {

                    name.setError("Full name required");
                    name.requestFocus();
                    register_btn.startAnimation(shake);
                    return;

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {

                    email.setError("Invalid Email Address");
                    email.requestFocus();
                    register_btn.startAnimation(shake);
                    return;
                } else if (phone.getText().length() != 10) {
                    phone.setError("Invalid phone number", null);
                    phone.requestFocus();
                    phone.startAnimation(shake);
                    return;
                } else if (password.getText().length() < 6) {

                    password.setError("Minimum 6 characters", null);
                    password.requestFocus();
                    register_btn.startAnimation(shake);
                    return;

                } else if (spnr_city.getSelectedItemPosition() == 0) {
                    TextView errorText = (TextView) spnr_city.getSelectedView();
                    errorText.setError("Select a City");
                    errorText.requestFocus();
                    register_btn.startAnimation(shake);
                    return;
                } else if (spnr_profession.getSelectedItemPosition() == 1) {

                    if (lawyer_id_a.getText().toString().matches("")) {

                        lawyer_id_a.setError("Invalid Lawyer ID");
                        lawyer_id_b.requestFocus();
                        register_btn.startAnimation(shake);
                        return;

                    } else if (lawyer_id_b.getText().toString().matches("")) {

                        lawyer_id_b.setError("Invalid Lawyer ID");
                        lawyer_id_b.requestFocus();
                        register_btn.startAnimation(shake);
                        return;

                    } else if (officeAddress.getText().length() < 10) {

                        officeAddress.setError("Invalid Address");
                        officeAddress.requestFocus();
                        register_btn.startAnimation(shake);
                        return;

                    } else if (exp.getText().toString().matches("")) {
                        exp.setError("Invalid Experience Year");
                        exp.requestFocus();
                        register_btn.startAnimation(shake);
                        return;
                    } else if (!android.text.TextUtils.isDigitsOnly(exp.getText())) {

                        exp.setError("Only Numbers are Allowed");
                        exp.requestFocus();
                        register_btn.startAnimation(shake);
                        return;
                    } else if (exp.getText().toString().length() > 2 || Integer.valueOf(exp.getText().toString()) > 30) {
                        exp.setError("Invalid years of Experience");
                        exp.requestFocus();
                        register_btn.startAnimation(shake);
                        return;
                    }/*else if (Integer.valueOf(spec_count.getText().toString())==0){
                        Toast.makeText(getApplicationContext(),"Please Specify atleast one Specialisation",Toast.LENGTH_LONG).show();
                        btn_edit.startAnimation(shake);
                        return;
                    }*/

                } else if (!accept.isChecked()) {

                    Toast.makeText(getApplicationContext(), "Please accept the Terms and Conditions to continue", Toast.LENGTH_SHORT).show();
                    accept.startAnimation(shake);
                    return;

                }


                {


                    pd = new ProgressDialog(Registration.this);
                    pd.setMessage("Creating Account..");
                    pd.show();

                    //adding data to the hashmap cz it'll be easy to get a json data out of it


                    enteredData.put("Name", name.getText().toString().trim());
                    enteredData.put("EmailId", email.getText().toString().trim());
                    enteredData.put("Password", password.getText().toString());
                    enteredData.put("Mobile", "" + phone.getText().toString());
                    enteredData.put("City", spnr_city.getSelectedItem().toString());
                    enteredData.put("Profession", Integer.valueOf(spnr_profession.getSelectedItemPosition()).toString());
                    enteredData.put("LawyerId", spnr_lawyer_loc.getSelectedItem().toString() + "/" + lawyer_id_a.getText().toString().toUpperCase().trim() + "/" + lawyer_id_b.getText().toString());
                    enteredData.put("Exp", exp.getText().toString());
                    enteredData.put("Address1", officeAddress.getText().toString().trim());

                    register();
                }


            }
        });

        lawyerPart = (LinearLayout) findViewById(R.id.lawyer_part);

        //initializing lawyer id locations
        spnr_lawyer_loc = (Spinner) findViewById(R.id.spnr_lawyer_loc);
        final List<String> loc = new ArrayList<String>();


        loc.add("KAR");
        loc.add("MYS");

        proAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, loc);
        proAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_lawyer_loc.setAdapter(proAdapter);


        //initializing proffession spinner

        spnr_profession = (Spinner) findViewById(R.id.spnr_profession);
        final List<String> code = new ArrayList<String>();


        code.add("Normal User / Layman");
        code.add("Advocate");

        proAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, code);
        proAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_profession.setAdapter(proAdapter);

        lawyerPart = (LinearLayout) findViewById(R.id.lawyer_part);

        //checking user type
        spnr_profession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    lawyerPart.setVisibility(View.GONE);
                } else if (i == 1) {
                    lawyerPart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                SharedPreferences.Editor profile = getSharedPreferences("profile", MODE_PRIVATE).edit();
                profile.putString("propic", filePath.toString());
                profile.commit();
                //Setting the Bitmap to ImageView
                propic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void register() {


        final String url = "http://legall.co.in/web/registration1.php";


        final ProgressDialog pd = new ProgressDialog(Registration.this);
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

                            if (status == 0) {


                                pd.dismiss();


                                final AlertDialog.Builder build = new AlertDialog.Builder(Registration.this);
                                build.setMessage("Your email is already registered. Please use account recovery option in Login page or use a different email to register");
                                build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                build.show();


                            } else if (status == 1) {

                                pd.dismiss();


                                final AlertDialog.Builder build = new AlertDialog.Builder(Registration.this);
                                build.setMessage("Your Phone number is already registered. Please use account recovery option in Login page or use a different mobile number to register");
                                build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                build.show();

                            } else if (status == 2) {

                                pd.dismiss();


                                final AlertDialog.Builder build = new AlertDialog.Builder(Registration.this);
                                build.setMessage("Your Lawyer Id is already registered. Please use account recovery option in Login page or use a different mobile number to register");
                                build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                build.show();

                            } else if (status == 3 || status == 4) {


                                //registration successs

                                pd.dismiss();

                                final AppCompatDialog dialog = new AppCompatDialog(Registration.this);
                                dialog.setContentView(R.layout.dialog_registration_success);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                                Button back = (Button) dialog.findViewById(R.id.btn_back);
                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View view) {


                                        startActivity(new Intent(Registration.this, VerificationWelcome.class));

                                        return;
                                    }


                                });

                                dialog.show();
                                return;


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            pd.dismiss();
                            AlertDialog.Builder error = new AlertDialog.Builder(Registration.this);
                            error.setMessage("Server error ! we're working on to fix it. Please try again later.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .setCancelable(true)
                                    .show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Error handle error


                pd.dismiss();


                final AppCompatDialog dialog = new AppCompatDialog(Registration.this);
                dialog.setContentView(R.layout.dilog_request_error);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                Button back = (Button) dialog.findViewById(R.id.btn_back);
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();


                Log.i("Legall", "1");
                param.put("key", "Legall");
                param.put("profession", "" + spnr_profession.getSelectedItemPosition());
                param.put("name", name.getText().toString());
                param.put("email", email.getText().toString());
                param.put("phone", phone.getText().toString());
                param.put("password",password.getText().toString());
                param.put("city", spnr_city.getSelectedItem().toString());
                param.put("os", "1");


                if (spnr_profession.getSelectedItemPosition() == 1) {


                    param.put("lawyerId", spnr_lawyer_loc.getSelectedItem().toString() + "/" + lawyer_id_a.getText().toString().toUpperCase().trim() + "/" + lawyer_id_b.getText().toString());
                    param.put("exp", exp.getText().toString());
                    param.put("office", officeAddress.getText().toString());
                }


                Log.i("Legall", param.toString());


                return param;


            }

        };

        // Add the request to the queue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(Registration.this).add(stringRequest);


    }

    /*

    private class register extends AsyncTask<String,Void,String> {



        @Override
        protected String doInBackground(String... strings) {



            url = "http://legall.co.in/web/registration.php?key=Legall";

            url=url+"&name="+enteredData.get("Name").replaceAll(" ","%20");
            url=url+"&password="+enteredData.get("Password").replaceAll(" ","%20");
            url=url+"&email="+enteredData.get("EmailId").replaceAll(" ","%20");
            url=url+"&mobile="+enteredData.get("Mobile");
            url=url+"&profession="+enteredData.get("Profession");
            url = url + "&city=" + enteredData.get("City");

            if (enteredData.get("Profession").matches("1")) {

                url = url + "&exp=" + enteredData.get("Exp");
                url = url + "&lawyerId=" + enteredData.get("LawyerId");
                url = url + "&office="+ enteredData.get("Address1").replaceAll(" ","%20");

            }

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Success handle response

                            Log.i("Legall","Registration responce : "+response);

                            try{

                                JSONObject object= new JSONObject(response);
                                 status = object.getInt("status");

                            }catch(JSONException e){
                                e.printStackTrace();
                            }

                            if (status == 1){
                                pd.dismiss();

                                final AppCompatDialog dialog = new AppCompatDialog(Registration.this);
                                dialog.setContentView(R.layout.dialog_registration_success);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



                                Button back =(Button) dialog.findViewById(R.id.btn_back);
                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View view) {

                                        dialog.dismiss();
                                        startActivity(new Intent(Registration.this,VerificationWelcome.class));
                                        finish();
                                        return;
                                    }



                                });

                                dialog.show();
                                return;

                            }else if(status == 2){


                                //registration success, added to unverified lawyer
                                pd.dismiss();

                                final AppCompatDialog dialog = new AppCompatDialog(Registration.this);
                                dialog.setContentView(R.layout.dialog_registration_success);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



                                Button back =(Button) dialog.findViewById(R.id.btn_back);
                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View view) {

                                        dialog.dismiss();
                                        startActivity(new Intent(Registration.this,VerificationWelcome.class));
                                        finish();
                                        return;
                                    }



                                });

                                dialog.show();
                                return;



                            }else if(status == 0){


                                //registration form is being rejected by the server

                                pd.dismiss();


                                final AlertDialog.Builder build = new AlertDialog.Builder(Registration.this);
                                build.setMessage("User Already Exits");
                                build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                build.show();


                                return;


                            }else{

                                pd.dismiss();

                                final AppCompatDialog dialog = new AppCompatDialog(Registration.this);
                                dialog.setContentView(R.layout.dialog_invalid_form);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



                                Button back =(Button) dialog.findViewById(R.id.btn_back);
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

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Error handle error


                    pd.dismiss();

                    final AppCompatDialog dialog = new AppCompatDialog(Registration.this);
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
            Volley.newRequestQueue(Registration.this).add(stringRequest);

            return null;

        }

        @Override
        protected void onPostExecute(String result){

        }



    }

    */

    public boolean checkname(String id) {
        Pattern p = Pattern.compile("[a-zA-Z\\s]*");
        Matcher m = p.matcher(id);
        return m.matches();
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(Registration.this, VerificationWelcome.class));
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(Registration.this, VerificationWelcome.class));
                finish();
                break;



        }
        return true;
    }



    //to load city list
    public void loadCityList() {

        final ProgressDialog pd = new ProgressDialog(Registration.this);
        pd.setMessage("Please wait..");
        pd.setCancelable(false);
        pd.show();

        String url = "http://legall.co.in/web/city.php?id=1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Success handle response

                        Log.i("Legall", response);
                        pd.dismiss();

                        try {

                            JSONObject object = new JSONObject(response);
                            status1 = object.getString("message");

                            if (status1.matches("Success")) {


                                JSONArray cityArray = object.getJSONArray("city");

                                for (int i = 0; i < cityArray.length(); i++) {

                                    JSONObject obj = cityArray.getJSONObject(i);
                                    city.add(obj.getString("city_name"));

                                }

                            } else {


                                //registration form is being rejected by the server

                                pd.dismiss();

                                final AppCompatDialog dialog = new AppCompatDialog(Registration.this);
                                dialog.setContentView(R.layout.dialog_invalid_form);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                TextView message = (TextView) dialog.findViewById(R.id.txt_message);
                                message.setText("Couldn't fetch the data from the server. Please contact the customer care or try again after sometimes.");


                                Button back = (Button) dialog.findViewById(R.id.btn_back);
                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View view) {

                                        dialog.dismiss();
                                        startActivity(new Intent(Registration.this, VerificationWelcome.class));
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

                final AppCompatDialog dialog = new AppCompatDialog(Registration.this);
                dialog.setContentView(R.layout.dilog_request_error);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                Button back = (Button) dialog.findViewById(R.id.btn_back);
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
        Volley.newRequestQueue(Registration.this).add(stringRequest);

    }

}

