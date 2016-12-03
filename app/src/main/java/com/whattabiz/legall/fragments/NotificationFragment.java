package com.whattabiz.legall.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whattabiz.legall.adapters.NotificationAdapter;
import com.whattabiz.legall.R;
import com.whattabiz.legall.models.NotificationModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    private List<NotificationModel> notificationList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotificationAdapter mAdapter;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        mAdapter = new NotificationAdapter(notificationList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareNotificationList();

        return rootView;
    }

    public  void prepareNotificationList(){

        notificationList.clear();

        /**
         * data structure is in the form
         * title, caption, message,t ype(appointment,document,other), date(and time), status(0-failed,1-processing,2-success)
         *
         */

        NotificationModel content = new NotificationModel("Appointment Success" ,"Appointment granted, success message here within two lines..","some really crazy long text goes here","appointment","19/07/2016 - 12:30 pm",2);
        notificationList.add(content);

        content = new NotificationModel("Document Verification Processing","Document verification in process, success message here within two lines..","some really crazy long text goes here","document","19/07/2016 - 12:30 pm",1);
        notificationList.add(content);

        content = new NotificationModel("Appointment failed" ,"Appointment failed message, with some caption","some really crazy long text goes here","appointment","19/07/2016 - 12:30 pm",0);
        notificationList.add(content);

        content = new NotificationModel("Other notification" ,"Appointment failed message, with some caption","some really crazy long text goes here","other","19/07/2016 - 12:30 pm",1);
        notificationList.add(content);

        mAdapter.notifyDataSetChanged();

    }

}
