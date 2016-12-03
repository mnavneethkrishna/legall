package com.whattabiz.legall;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;

import com.whattabiz.legall.models.CartItemModel;
import com.whattabiz.legall.models.FaqModel;
import com.whattabiz.legall.models.SlideModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Haxzie on 10/20/2016.
 * <p>
 * User Class to store user data for the entire application
 */
public class   User {


    private static final Integer version = 40;
    public static String type;
    public static String Id;
    public static String name;
    public static String phone;
    public static String email;
    public static String city;


    public static String lawyerId;
    public static String exp;
    public static String officeAddress;

    public static ArrayList<Bitmap> slideImages = new ArrayList<>();
    public static ArrayList<String> slideTitle = new ArrayList<>();
    public static ArrayList<FaqModel> faqList = new ArrayList<>();

    public static ArrayList<SlideModel> staticSlide = new ArrayList<>();
    public static Integer status;
    private static Typeface font;

    private static User ourInstance = new User();

    public static Context context;

    public static ArrayList<CartItemModel> cartItem = new ArrayList<>();


    public static User getInstance(Context context) {
        User.context = context;
        return ourInstance;
    }

    public User() {
    }


    //for loading data into the static variables
    public static boolean loadUserData(Context context) {

        font = Typeface.createFromAsset(context.getAssets(),"fonts/segoeui.ttf");
        SharedPreferences user = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        String status = user.getString("status", null);
        String update = user.getString("update",null);

        if (update == null){

            return false;
        }



        if (status != null) {

            if (user.getString("userData", null) != null) {
                try {

                    JSONObject data = new JSONObject(user.getString("userData", null));


                    type = data.getString("profession");
                    //Toast.makeText(context,type,Toast.LENGTH_LONG).show();
                    Id = data.getString("user_id");

                    name = data.getString("name");

                    email = data.getString("email");

                    phone = data.getString("mobile");

                    city = data.getString("city");

                    if (!type.matches("0")) {

                        lawyerId = data.getString("lawyerId");

                        exp = data.getString("exp");

                        officeAddress = data.getString("address");
                    }else {

                        lawyerId = null;

                        exp = null;

                        officeAddress = null;

                    }


                    return true;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Legall", "USER DATA ERROR : @sharedPreferences-user, json data  ");
                    return false;
                }
            } else {
                return false;
            }
        } else
            return false;
    }

    public static String getName() {
        if (name != null) {
            return name;
        } else {
            loadUserData(context);
            return name;
        }

    }

    public static void clear() {

        type = null;
        Id = null;
        name = null;
        phone = null;
        email = null;
        city = null;
        lawyerId = null;
        exp = null;
        officeAddress = null;

        slideImages.clear();
        slideTitle.clear();
        staticSlide.clear();
        status = null;
        cartItem.clear();


    }


    public static Typeface getFont(Context context){
        if (font == null){
            font = Typeface.createFromAsset(context.getAssets(),"fonts/segoeui.ttf");

        }

        return font;
    }

    public static int versionCheck(){
        return version;
    }
}
