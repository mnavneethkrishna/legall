package com.whattabiz.legall.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.whattabiz.legall.R;
import com.whattabiz.legall.User;
import com.whattabiz.legall.activity.EditProfileActivity;
import com.whattabiz.legall.activity.LawyerHome;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLawyerProfile extends Fragment {




    private int PICK_IMAGE_REQUEST = 1;
    CircleImageView propic;


    Bitmap bitmap = null;

    TextView name, email, phone, loc, office, lawyerid,exp;
    Button edit;


    public FragmentLawyerProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_lawyer_profile, container, false);


        propic = (CircleImageView) rootView.findViewById(R.id.profile_image);

        name = (TextView) rootView.findViewById(R.id.txt_name);
        email = (TextView) rootView.findViewById(R.id.txt_email);
        phone = (TextView) rootView.findViewById(R.id.txt_phone);
        loc = (TextView) rootView.findViewById(R.id.txt_location);
        office = (TextView) rootView.findViewById(R.id.txt_office);
        lawyerid = (TextView) rootView.findViewById(R.id.txt_lawyerid);
        edit = (Button) rootView.findViewById(R.id.btn_edit);
        exp = (TextView) rootView.findViewById(R.id.txt_exp);



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfileActivity.class).putExtra("type", 1));
            }
        });


        Bitmap bitmap = ((LawyerHome) getActivity()).bitmap;

        if (bitmap != null) {
            propic.setImageBitmap(((LawyerHome) getActivity()).bitmap);
        }


        rootView.findViewById(R.id.profileView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, PICK_IMAGE_REQUEST);
            }
        });

        new LoadProfile().execute();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);

                SharedPreferences.Editor profile = getActivity().getSharedPreferences("profile", MODE_PRIVATE).edit();
                profile.putString("propic", filePath.toString());
                profile.commit();


                //Setting the Bitmap to ImageView

                new FragmentLawyerProfile.LoadDP().execute();

                //setting navigation drawer dp
                ((LawyerHome) getActivity()).setDp();

            } catch (IOException e) {
                e.printStackTrace();
                bitmap = null;
            }
        }


    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        if (image == null) {
            return null;
        }

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public void setDp() {

        SharedPreferences profile = getActivity().getSharedPreferences("profile", getContext().MODE_PRIVATE);
        String profileUri = profile.getString("propic", "");

        Uri imageUri = null;

        if (profileUri != "") {

            try {

                imageUri = Uri.parse(profileUri);


            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                new FragmentLawyerProfile.LoadDP().execute();

            } catch (IOException e) {
                e.printStackTrace();
                bitmap = null;
            }
        }
    }


    private class LoadDP extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            bitmap = getResizedBitmap(bitmap, 300);


            return null;
        }

        @Override
        protected void onPostExecute(String result) {


            if (bitmap != null) {

                propic.setImageBitmap(bitmap);
            }

        }
    }


    private class LoadProfile extends AsyncTask<String, Void, String> {


        String NAME, EMAIL, LOC, PHONE, OFFICE, LAWYERID, EXP;

        @Override
        protected String doInBackground(String... strings) {


            User userdData = User.getInstance(getContext());

            NAME = userdData.name;
            EMAIL = userdData.email;
            LOC = userdData.city;
            PHONE = userdData.phone;
            OFFICE = userdData.officeAddress;
            LAWYERID = "ID : "+userdData.lawyerId;
            EXP = userdData.exp;


            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            name.setText(NAME);
            email.setText(EMAIL);
            phone.setText(PHONE);
            loc.setText(LOC);
            office.setText(OFFICE);
            if (OFFICE.matches("")) office.setVisibility(View.GONE);
            lawyerid.setText(LAWYERID);
            exp.setText("Experience : "+EXP+" years");


        }
    }
}
