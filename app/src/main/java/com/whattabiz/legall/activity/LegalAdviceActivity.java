package com.whattabiz.legall.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LegalAdviceActivity extends AppCompatActivity {

    Context context;
    EditText email, subject, content;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_advice);

        context = getApplicationContext();

        email = (EditText) findViewById(R.id.email);
        subject = (EditText) findViewById(R.id.subject);
        content = (EditText) findViewById(R.id.editText);

        email.setText(User.email);


        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Legal Advice");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.tb_send:
                if (checkContent()) {


                    final ProgressDialog pd = new ProgressDialog(LegalAdviceActivity.this);
                    pd.setMessage("Sending Request...");
                    pd.setCancelable(false);
                    pd.show();


                    url = "http://www.legall.co.in/web/advice.php?key=Legall&from="+email.getText().toString()+"&subject="+subject.getText().toString().replaceAll(" ","%20")+"&desc="+content.getText().toString().replaceAll(" ","%20")+"&profession="+User.getInstance(LegalAdviceActivity.this).type+"&user_id="+User.getInstance(LegalAdviceActivity.this).Id;

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    pd.dismiss();
                                    Log.i("Legall","Login URL : "+url);
                                    Log.i("Legall","Login responce : "+response);

                                    //Success handle response
                                    try {
                                        JSONObject obj = new JSONObject(response);

                                        Integer status = obj.getInt("status");

                                        if (status == 1) {


                                            final AppCompatDialog dialog = new AppCompatDialog(LegalAdviceActivity.this);
                                            dialog.setContentView(R.layout.dialog_request_success);
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                            Log.i("Legall","1");
                                            TextView success = (TextView) dialog.findViewById(R.id.success);
                                            success.setText("Request Sent !");

                                            Log.i("Legall","2");
                                            TextView message = (TextView) dialog.findViewById(R.id.message);
                                            message.setText("Your request has been received. We'll get back to you shortly.");

                                            Log.i("Legall","3");

                                            Button back =(Button) dialog.findViewById(R.id.btn_dismiss);
                                            back.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(final View view) {

                                                    dialog.dismiss();
                                                    finish();
                                                    return;
                                                }



                                            });

                                            pd.dismiss();
                                            dialog.show();

                                        }else {
                                            pd.dismiss();
                                            Toast.makeText(LegalAdviceActivity.this,"Ooops ! that was a bad request. Check your request and try again.",Toast.LENGTH_LONG).show();
                                            return;
                                        }

                                    }
                                    catch(JSONException ex) {
                                        ex.printStackTrace();
                                    }


                                    pd.dismiss();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            pd.dismiss();

                            Log.e("Legall",error.toString());


                            final AppCompatDialog dialog = new AppCompatDialog(LegalAdviceActivity.this);
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

                            pd.dismiss();
                            dialog.show();
                            return;
                        }
                    });

                    // Add the request to the queue
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    Volley.newRequestQueue(this).add(stringRequest);


                }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.send, menu);

        return true;
    }

    public boolean checkContent(){

        if (email.getText().toString().matches("")||email.getText().toString().length()<5){
            Toast.makeText(context,"Invalid Email Address",Toast.LENGTH_LONG).show();
            return  false;
        }else if (subject.getText().toString().matches("")||subject.getText().toString().length()<10){
            Toast.makeText(context,"Subject Too Short",Toast.LENGTH_LONG).show();
            return  false;
        }else if(content.getText().toString().matches("")||content.getText().toString().length()<50){
            Toast.makeText(context,"Content Too Short..",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
