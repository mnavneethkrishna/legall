package com.whattabiz.legall.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whattabiz.legall.R;
import com.whattabiz.legall.User;
import com.whattabiz.legall.activity.ChangePasswordActivity;
import com.whattabiz.legall.activity.VerificationWelcome;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserSettingsFragment extends Fragment {

    CardView logout,about;


    public UserSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_settings, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        /*
            Logout button
         */

        view.findViewById(R.id.btn_change_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),ChangePasswordActivity.class));
            }
        });
        view.findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //clearing stored user data
                        User.getInstance(getContext()).clear();
                        SharedPreferences.Editor user = getContext().getSharedPreferences("user",Context.MODE_PRIVATE).edit();
                        user.clear();
                        user.commit();
                        user = getContext().getSharedPreferences("profile",Context.MODE_PRIVATE).edit();
                        user.clear();
                        user.commit();
                        startActivity(new Intent(getActivity(),VerificationWelcome.class));
                        getActivity().finish();
                    }
                }).setNegativeButton("NO",null).show();
            }
        });



        /*
            About Legall
         */
        view.findViewById(R.id.btn_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Legall v1.3");
                builder.setMessage("Copyright reserved Legall India Pvt. Ltd.");
                builder.setPositiveButton("OK",null).show();
            }
        });


        /*
            Clearing Cache note: only profile cache @sharedPref/app_data
         */
        view.findViewById(R.id.btn_cache).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Clear cache");
                builder.setMessage("This will clear all saved data of the application including the login details. Are you sure you want to clear the data ?");
                builder.setPositiveButton("CLEAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit().clear().commit();
                        getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE).edit().clear().commit();
                        User.getInstance(getContext()).clear();
                        startActivity(new Intent(getActivity(),VerificationWelcome.class));
                        getActivity().finish();

                    }
                }).setNegativeButton("CANCEL",null).show();
            }
        });
    }

}
