package com.whattabiz.legall.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.whattabiz.legall.R;
import com.whattabiz.legall.activity.TemplateViewActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class DraftDeedsFragment extends Fragment {

    Button view_template;


    public DraftDeedsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_draft_deeds, container, false);

        view_template = (Button) rootView.findViewById(R.id.btn_template);
        view_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(rootView.getContext(),TemplateViewActivity.class));
            }
        });


        return rootView;
    }

    public static DraftDeedsFragment newInstance() {
        DraftDeedsFragment fragment = new DraftDeedsFragment();
        return fragment;
    }

}
