package com.whattabiz.legall.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.whattabiz.legall.models.ContentItem;
import com.whattabiz.legall.adapters.ContentItemAdapter;
import com.whattabiz.legall.R;

import java.util.ArrayList;
import java.util.List;

public class ContentListActivity extends AppCompatActivity {

    private List<ContentItem> contentItemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContentItemAdapter mAdapter;
    TextView number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);

        //Initializing the toolbar
        Bundle b=getIntent().getExtras();
        String WINDOW_TITLE=b.getString("WINDOW_TITLE","");
        Integer LIST_ID=b.getInt("LIST_ID",0);
        Integer LINK = Integer.valueOf(b.getString("LINK",""));


        //for emergency purposes :p
        if (WINDOW_TITLE.matches("")){
            new AlertDialog.Builder(getApplicationContext())
                    .setMessage("Uh oh ! some error occurred :( ")
                    .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            finish();

                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setCancelable(true).show();
        }





        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(WINDOW_TITLE);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new ContentItemAdapter(contentItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);




    }


    /**
     * List generator for Registration
     */



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
