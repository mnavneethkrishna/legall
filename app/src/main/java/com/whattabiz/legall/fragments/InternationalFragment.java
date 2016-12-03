package com.whattabiz.legall.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.whattabiz.legall.R;
import com.whattabiz.legall.activity.LegalAdviceActivity;


public class InternationalFragment extends Fragment {

    Button get;


    public InternationalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_international, container, false);

        rootView.findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(rootView.getContext(),LegalAdviceActivity.class));
            }
        });

        return rootView;
    }

    public static InternationalFragment newInstance() {
        InternationalFragment fragment = new InternationalFragment();
        return fragment;
    }


}
