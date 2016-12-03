package com.whattabiz.legall.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.whattabiz.legall.R;

public class TermsAndConditionViewer extends AppCompatActivity {

    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition_viewer);


        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Terms and Conditions");

        wv = (WebView) findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setDisplayZoomControls(false );

        Bundle b = getIntent().getExtras();

        String pdf=b.getString("LINK");

        Log.i("Legall",pdf);
        wv.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdf);


        final ProgressDialog pd = new ProgressDialog(TermsAndConditionViewer.this);
        pd.setMessage("Loading PDF...");
        pd.setCancelable(false);
        pd.show();

        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,String url){

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
                new AlertDialog.Builder(TermsAndConditionViewer.this)
                        .setMessage("Some error occurred, Couldn't load the Terms And Conditions ! Check your internet connection and try again.")
                        .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false).show();
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
