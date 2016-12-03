package com.whattabiz.legall.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.whattabiz.legall.R;

public class ArticleViewer extends AppCompatActivity {

    Button viewpdf;
    ImageView img;
    LinearLayout objects;

    TextView title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_viewer);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Articles");

        objects = (LinearLayout) findViewById(R.id.objects);

        Animation anim = AnimationUtils.loadAnimation(ArticleViewer.this,R.anim.slide_up);
        objects.startAnimation(anim);


        Bundle b = getIntent().getExtras();

        final String PDF = b.getString("LINK");
        String TITLE = b.getString("WINDOW_TITLE");
        String CONTENT = b.getString("CONTENT");
        String IMG = b.getString("IMG");

        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.caption);

        title.setText(TITLE);
        content.setText(CONTENT);
        img = (ImageView) findViewById(R.id.img);
        Picasso.with(ArticleViewer.this).load(IMG.replaceAll(" ","%20")).into(img);







        viewpdf = (Button) findViewById(R.id.btn_viewpdf);
        viewpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ArticleViewer.this,PdfViewer.class).putExtra("LINK",PDF));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                supportFinishAfterTransition();
                break;


        }
        return true;
    }

    @Override
    public void finish(){
        Animation anim = AnimationUtils.loadAnimation(ArticleViewer.this,R.anim.slide_down);
        objects.startAnimation(anim);
        final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 200);
        super.finish();



    }


}
