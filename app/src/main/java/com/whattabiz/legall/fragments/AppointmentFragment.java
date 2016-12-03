package com.whattabiz.legall.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whattabiz.legall.R;
import com.whattabiz.legall.adapters.AppointmentAdapter;
import com.whattabiz.legall.models.AppointmentModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment {

    private List<AppointmentModel> appointmentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AppointmentAdapter mAdapter;


    public AppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_appointment, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        mAdapter = new AppointmentAdapter(appointmentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(mAdapter);

       // new LoadList().execute();
        
        return rootView;
    }


    private void prepareAppointmentList() {

        appointmentList.clear();

        AppointmentModel content = new AppointmentModel("Musthaq Ahamad", "19/07/2016 - 12:30 pm","Office address and other stuffs goes here",R.drawable.propic);
        appointmentList.add(content);

        content = new AppointmentModel("Harikrishnan ", "18/02/2016 - 03:30 pm ", "Office address and other stuffs goes here", R.drawable.img_realestate);
        appointmentList.add(content);


        content = new AppointmentModel("Rumaan Kalandher ", "18/02/2016 - 04:30 pm ", "Office address and other stuffs goes here", R.drawable.img_startup);
        appointmentList.add(content);


        content = new AppointmentModel("Jhonathan Rodriguez ", "18/02/2016 - 05:30 pm ", "Office address and other stuffs goes here", R.drawable.img_litigation);
        appointmentList.add(content);

        content = new AppointmentModel("Nidarsh Nithyananda ", "18/02/2016 - 06:30 pm ", "Office address and other stuffs goes here", R.drawable.img_acts);
        appointmentList.add(content);









    }


    private class LoadList extends AsyncTask<String,Void,String> {



        @Override
        protected String doInBackground(String... strings) {

            prepareAppointmentList();

            return null;
        }

        @Override
        protected void onPostExecute(String result){
            mAdapter.notifyDataSetChanged();
        }
    }


}
