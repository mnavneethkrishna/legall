package com.whattabiz.legall.activity;

/*Mobile number verification Activity
*Same activity for entering mobile number
* verification of mobile number
* showing up success message
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whattabiz.legall.R;

public class MobileVerification extends AppCompatActivity {

    //phone_text for the top text which says phone verification
    //text_phone_number to show success message 
    TextView phone_text,text_phone_number;
    EditText number;
    Button verify,continue_btn;
    ImageView done_img;


    //separate views for number verification and success message, views are swapped once the work is done by verify
    LinearLayout verifyView,successView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);
        Log.i("Legall","Current activity: MobileVerification");
        Log.i("Legall","Layout set: activity_mobile_verification.xml");


        //setting Typeface for the phone verification text
        phone_text=(TextView) findViewById(R.id.phone_verification_text);
        String fontPath="fonts/PoiretOne.ttf";
        Typeface tf=Typeface.createFromAsset(getAssets(),fontPath);
        phone_text.setTypeface(tf);




        text_phone_number=(TextView) findViewById(R.id.text_phone_number);
        number=(EditText) findViewById(R.id.phone_number);
        verify=(Button) findViewById(R.id.verify_btn);
        continue_btn=(Button) findViewById(R.id.continue_btn);
        done_img=(ImageView) findViewById(R.id.done_img);
        verifyView=(LinearLayout) findViewById(R.id.verify_view);
        successView=(LinearLayout) findViewById(R.id.success_view);
        Log.i("Legall","All views Declared and bound");


        successView.setVisibility(View.GONE);
        continue_btn.setVisibility(View.GONE);



       /*
        number.requestFocus();
        InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(number,InputMethodManager.SHOW_IMPLICIT);
        */


        //verify button
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (number.getText().toString().length()!=10){
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shakeanim);
                    number.startAnimation(shake);
                    number.setError("invalid number");
                    number.requestFocus();
                }else {

                    SharedPreferences.Editor profile = getSharedPreferences("user",MODE_PRIVATE).edit();
                    profile.putString("phone",number.getText().toString());
                    profile.commit();


                    text_phone_number.setText("+91 " + number.getText().toString());
                    Log.i("Legall", "verify Button action : Phone number changed to " + number.getText().toString());


                    //hiding the soft keyboard once the button is pressed
                    View v = getCurrentFocus();
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        Log.i("Legall", "verify Button Action: Soft keyboard hidden");

                    }

                    //swapping the views
                    verifyView.setVisibility(View.GONE);
                    successView.setVisibility(View.VISIBLE);
                    continue_btn.setVisibility(View.VISIBLE);
                    Log.i("Legall", "verify Button action : views swapped");

                }
            }
        });




        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MobileVerification.this,Registration.class));

                finish();
            }
        });






    }
}
