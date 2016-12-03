package com.whattabiz.legall.activity;

/*
*       Splash screen activity

 */

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.rerlanggas.lib.ExceptionHandler;
import com.whattabiz.legall.R;
import com.whattabiz.legall.User;

public class SplashActivity extends AppCompatActivity {

    ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ExceptionHandler.init(SplashActivity.this,ErrorActivity.class);


        Log.i("Legall", "Current activity: SplashActivity");
        Log.i("Legall", "Layout set: activity_splash.xml");


        Log.i("Legall", getSharedPreferences("user", MODE_PRIVATE).getString("userData", "nothing"));


        //Preparing for the Animation Start
        logo = (ImageView) findViewById(R.id.logo);
        logo.setVisibility(View.GONE);
        final Animation fadein = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in);
        Log.i("Legall", "Initialized fade in animation");


        //Handler to set delay before fade-in animation
        final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                logo.setVisibility(View.VISIBLE);
                logo.startAnimation(fadein);
                Log.i("Legall", "fade in animation success");
            }
        }, 500);
        //animation contents ends


        if (checkPermission()) {

            openActivity();
        }
        else {

            AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
            dialog.setTitle("Permissions Required!")
                    .setMessage("App requires certain permissions for the proper functioning of the features. Please grant permissions for Legall in your application manager.")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setPositiveButton("Give Permissions", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA}, 1);
                            checkPermission();

                        }
                    }).setCancelable(false).show();
        }


    }


    //to check user data from sharedPrefs
    private boolean userData() {

        return User.getInstance(SplashActivity.this).loadUserData(SplashActivity.this);

    }


    //method for checking app permissions
    public boolean checkPermission() {

        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(SplashActivity.this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {

            return false;
        } else
            return true;


    }


    //method for starting the next activity
    public void openActivity() {

        final android.os.Handler intentHandler = new android.os.Handler();
        intentHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (userData()) {

                    Log.i("Legall", "USER DATA STORE " + User.getInstance(SplashActivity.this).name);
                    switch (Integer.valueOf(User.type)) {

                        case 0:
                            //normal user
                            startActivity(new Intent(SplashActivity.this, UserHome.class));
                            finish();
                            break;
                        case 1:
                            //lawyer
                            startActivity(new Intent(SplashActivity.this, LawyerHome.class));
                            finish();
                            break;
                        case 2:
                            //unknown lawyer
                            startActivity(new Intent(SplashActivity.this, LawyerHome.class));
                            finish();
                            break;

                        default:
                            //no user data
                            startActivity(new Intent(SplashActivity.this, VerificationWelcome.class));
                            finish();
                            break;
                    }
                } else {
                    startActivity(new Intent(SplashActivity.this, VerificationWelcome.class));
                    //startActivity(new Intent(SplashActivity.this,Registration.class));
                    finish();
                }

                finish();
            }
        }, 1500);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults.length>0) {

                    openActivity();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SplashActivity.this);
                    dialog.setTitle("Permissions Required!")
                            .setMessage("App requires certain permissions for the proper functioning of the features. Please grant permissions for Legall in your application manager.")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .setPositiveButton("Give Permissions", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent settings = new Intent();
                                    settings.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    settings.setData(uri);
                                    startActivity(settings);
                                    finish();

                                }
                            }).setCancelable(false).show();
                }
        }
    }
}
