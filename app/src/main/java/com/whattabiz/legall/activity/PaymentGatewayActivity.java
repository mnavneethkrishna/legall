package com.whattabiz.legall.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.whattabiz.legall.Lawyers;
import com.whattabiz.legall.R;
import com.whattabiz.legall.User;

import org.apache.commons.codec.Encoder;
import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.lang.Object;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PaymentGatewayActivity extends AppCompatActivity {

    WebView wv;
    Context context = PaymentGatewayActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Payment");

        wv = (WebView) findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setDisplayZoomControls(false );
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);


        //original url
        String url = "https://secure.payu.in/_payment";


        //test url
        //String url = "https://test.payu.in/_payment";

        wv.postUrl(url, EncodingUtils.getBytes(getPostString(),"BASE64"));

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Please wait..");
        pd.setCancelable(false);


        wv.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view,String url,Bitmap favicon){

                pd.show();
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();

                if (url.contains("success")||url.contains("Success"))
                    showSuccess();
                super.onPageFinished(view, url);
            }

            @SuppressWarnings("unused")
            public void onReceivedSslError(WebView view) {
                pd.dismiss();
                Log.e("Error", "Exception caught!");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


        });







    }

    private String getPostString()
    {
        //original key and salt
        String key = "3c9P2Ahy";
        String salt  = "1CcZ8RHR6a";


        //test key and salt
//        String key = "JBZaLc";
//        String salt = "GQs7yium";


        String time = new SimpleDateFormat("hhmmss").format(Calendar.getInstance().getTime());
        String txnid = "TXN"+ User.Id + time;
        Log.i("Legall","transaction id : "+txnid);
        String amount = getIntent().getExtras().getString("TOTAL");
        String firstname = User.name;
        String email = User.email;
        String phone = User.phone;
        String surl = "http://legall.co.in/payumoney/success.php";
        String furl = "http://legall.co.in/payumoney/failure.php";
        String productInfo = getIntent().getExtras().getString("PRODUCT_INFO");


        StringBuilder post = new StringBuilder();
        post.append("key=");
        post.append(key);
        post.append("&");
        post.append("txnid=");
        post.append(txnid);
        post.append("&");
        post.append("amount=");
        post.append(amount);
        post.append("&");
        post.append("productinfo=");
        post.append(productInfo);
        post.append("&");
        post.append("firstname=");
        post.append(firstname);
        post.append("&");
        post.append("email=");
        post.append(email);
        post.append("&");
        post.append("phone=");
        post.append(phone);
        post.append("&");
        post.append("surl=");
        post.append(surl);
        post.append("&");
        post.append("furl=");
        post.append(furl);
        post.append("&");

        StringBuilder checkSumStr = new StringBuilder();

        MessageDigest digest;
        String hash;
        try {
            digest = MessageDigest.getInstance("SHA-512");

            checkSumStr.append(key);
            checkSumStr.append("|");
            checkSumStr.append(txnid);
            checkSumStr.append("|");
            checkSumStr.append(amount);
            checkSumStr.append("|");
            checkSumStr.append(productInfo);
            checkSumStr.append("|");
            checkSumStr.append(firstname);
            checkSumStr.append("|");
            checkSumStr.append(email);
            checkSumStr.append("|||||||||||");
            checkSumStr.append(salt);

            digest.update(checkSumStr.toString().getBytes());

            hash = bytesToHexString(digest.digest());
            post.append("hash=");
            post.append(hash);
            post.append("&");
            Log.i("Legall", "SHA result is " + hash);
        } catch (NoSuchAlgorithmException e1) {

            e1.printStackTrace();
        }

        post.append("service_provider=");
        post.append("payu_paisa");
        return post.toString();
    }


    private static String bytesToHexString(byte[] bytes) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                showPrompt();
                break;



        }
        return true;
    }

    public void showPrompt(){

        final AlertDialog.Builder dialog = new AlertDialog.Builder(PaymentGatewayActivity.this);
        dialog.setCancelable(false)
                .setMessage("Do you want to cancel the transaction ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setNegativeButton("NO",null).show();
    }


    public void showSuccess(){

        CartActivity.getInstance().clearCart();
        CartActivity.getInstance().updateCart();

        final AppCompatDialog dialog = new AppCompatDialog(context);
        dialog.setContentView(R.layout.dialog_registration_success);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText("Transaction Success");
        TextView caption = (TextView) dialog.findViewById(R.id.caption);
        caption.setText("We've recieved your payment. we'll contact you soon with the details through email or phone. Thank you!");


        Button back = (Button) dialog.findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                finish();
                return;
            }


        });

        dialog.show();
    }
}
