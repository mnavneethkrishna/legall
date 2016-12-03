package com.whattabiz.legall.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whattabiz.legall.R;
import com.whattabiz.legall.activity.startupCompanyActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyDescriptionFragment extends Fragment {


    public CompanyDescriptionFragment() {
        // Required empty public constructor
    }

    public static CompanyDescriptionFragment newInstance() {
        CompanyDescriptionFragment fragment = new CompanyDescriptionFragment();
        return fragment;
    }


    TextView title, description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_company_description, container, false);


        title = (TextView) rootView.findViewById(R.id.txt_title);
       title.setText(startupCompanyActivity.title);

        description = (TextView) rootView.findViewById(R.id.txt_description);
        description.setText(startupCompanyActivity.description);





        return rootView;
    }

}
