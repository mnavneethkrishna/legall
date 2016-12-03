package com.whattabiz.legall.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.whattabiz.legall.R;
import com.whattabiz.legall.adapters.AppointmentAdapter;
import com.whattabiz.legall.models.AppointmentModel;

import java.util.ArrayList;
import java.util.List;

public class LAppointmentActivity extends AppCompatActivity {


    private List<AppointmentModel> appointmentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AppointmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lappointment);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Appointments");

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new AppointmentAdapter(appointmentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LAppointmentActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(mAdapter);

       // new LoadList().execute();


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



        mAdapter.notifyDataSetChanged();





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
