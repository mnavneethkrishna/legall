package com.whattabiz.legall.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.whattabiz.legall.adapters.ActsDocAdapter;
import com.whattabiz.legall.models.ActsDocModel;
import com.whattabiz.legall.R;

import java.util.ArrayList;
import java.util.List;

public class ActsPdfListActivity extends AppCompatActivity {


    private List<ActsDocModel> actsPdfList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ActsDocAdapter mAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acts_pdf_list);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Documents" );



        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new ActsDocAdapter(actsPdfList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ActsPdfListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareContentList();
    }




    /**
     * pdf model requires 3 arguments
     * 1.title
     * 2.caption
     * 3.link to the pdf
     */
    public void prepareContentList(){

        ActsDocModel doc = new ActsDocModel("Law Document 1","lorem ipsum odor, la ts ta boo gafd yesdor thy istedx hei opyt hdhc hdks ","");
        actsPdfList.add(doc);

        doc = new ActsDocModel("Law Document 2","lorem ipsum odor, la ts ta boo gafd yesdor thy istedx hei opyt hdhc hdks ","");
        actsPdfList.add(doc);

        doc = new ActsDocModel("Law Document 3","lorem ipsum odor, la ts ta boo gafd yesdor thy istedx hei opyt hdhc hdks ","");
        actsPdfList.add(doc);

        doc = new ActsDocModel("Law Document 4","lorem ipsum odor, la ts ta boo gafd yesdor thy istedx hei opyt hdhc hdks ","");
        actsPdfList.add(doc);

        doc = new ActsDocModel("Law Document 5","lorem ipsum odor, la ts ta boo gafd yesdor thy istedx hei opyt hdhc hdks ","");
        actsPdfList.add(doc);

        doc = new ActsDocModel("Law Document 6","lorem ipsum odor, la ts ta boo gafd yesdor thy istedx hei opyt hdhc hdks ","");
        actsPdfList.add(doc);

        doc = new ActsDocModel("Law Document 7","lorem ipsum odor, la ts ta boo gafd yesdor thy istedx hei opyt hdhc hdks ","");
        actsPdfList.add(doc);

        doc = new ActsDocModel("Law Document 8","lorem ipsum odor, la ts ta boo gafd yesdor thy istedx hei opyt hdhc hdks ","");
        actsPdfList.add(doc);

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
