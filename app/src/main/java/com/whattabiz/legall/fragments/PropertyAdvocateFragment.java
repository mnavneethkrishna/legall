package com.whattabiz.legall.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whattabiz.legall.R;
import com.whattabiz.legall.activity.LawyerListActivity;
import com.whattabiz.legall.activity.DocumentScrutinyActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class PropertyAdvocateFragment extends Fragment {





    public PropertyAdvocateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_property_advocate, container, false);

        rootView.findViewById(R.id.btn_get_advocate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(rootView.getContext(),LawyerListActivity.class);
                i.putExtra("TYPE","Real Estate");
                startActivity(i);
            }
        });

        rootView.findViewById(R.id.btn_document).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), DocumentScrutinyActivity.class);
                intent.putExtra("WHICH_ACTIVITY", 0);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public static PropertyAdvocateFragment newInstance() {
        PropertyAdvocateFragment fragment = new PropertyAdvocateFragment();
        return fragment;
    }

}
