package com.whattabiz.legall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.whattabiz.legall.adapters.NotificationAdapter;
import com.whattabiz.legall.models.NotificationModel;
import com.whattabiz.legall.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityNotification extends AppCompatActivity {

    private List<NotificationModel> notificationList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotificationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Notifications");



        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new NotificationAdapter(notificationList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ActivityNotification.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareNotificationList();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;



        }
        return true;
    }
}
