package com.whattabiz.legall.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.whattabiz.legall.R;

import java.util.Random;

public class EPdfViewer extends AppCompatActivity {


    WebView wv;
    Button get;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_service);

        final Bundle bundle=getIntent().getExtras();


        final String EXTRA_WINDOW_TITLE =bundle.getString("WINDOW_TITLE","ERROR");
        final String EXTRA_LINK = bundle.getString("LINK",null);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(EXTRA_WINDOW_TITLE);

        wv = (WebView) findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setDisplayZoomControls(false );

        //replace with file string
        String pdf=bundle.getString("PDF");

        //base url for google docs
        wv.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdf);

        //wv.setVisibility(View.INVISIBLE);



        final ProgressDialog pd = new ProgressDialog(EPdfViewer.this);
        pd.setMessage("Loading PDF...");
        pd.setCancelable(false);
        pd.show();

        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,String url){
                //view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view,String url){
                if( pd.isShowing()){
                    pd.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view,int error_code,String description,String failingurl){
                if ( pd.isShowing()){
                    pd.dismiss();
                    wv.setVisibility(View.INVISIBLE);
                }
                new AlertDialog.Builder(EPdfViewer.this)
                        .setMessage("Some error occurred, Couldn't load the pdf ! Check your internet connection and try again.")
                        .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false).show();
            }

        });

        get = (Button) findViewById(R.id.btn_get);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Random r = new Random();
                Integer num = r.nextInt(500);

                Intent i =new Intent(EPdfViewer.this,BuyNowActivity.class);
                i.putExtra("LINK",EXTRA_LINK);
                i.putExtra("TITLE",EXTRA_WINDOW_TITLE);
                i.putExtra("ID", num.toString());
                i.putExtra("COST",bundle.getString("PRICE"));
                startActivity(i);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;



        }
        return true;
    }



}
