package com.whattabiz.legall.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.whattabiz.legall.R;
import com.whattabiz.legall.User;
import com.whattabiz.legall.activity.ArticleViewer;
import com.whattabiz.legall.models.ArticleListModel;

import java.util.List;

/**
 * Created by User on 8/3/2016.
 */

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.MyViewHolder> {

    private List<ArticleListModel> articleList;


    Context c;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,caption,date;
        ImageView image;
        CardView main_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            caption = (TextView) view.findViewById(R.id.txt_caption);
            image = (ImageView) view.findViewById(R.id.image);
            main_view=(CardView) view.findViewById(R.id.view_main);
            date = (TextView) view.findViewById(R.id.txt_date);
            c=view.getContext();
        }
    }


    public ArticleListAdapter(List<ArticleListModel> articleList) {
        this.articleList = articleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_articles_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ArticleListModel cView = articleList.get(position);
        holder.title.setText(cView.getTitle());
        holder.title.setTypeface(User.getInstance(c).getFont(c));
        holder.caption.setText(cView.getCaption());
        holder.date.setText( cView.getDate());
        Picasso.with(c).load(Uri.parse(cView.getImg())).into(holder.image);
       /* holder.caption.setText(cView.getType());
        holder.rowThumb.setImageResource(cView.getImgid());*/



        //Setting on click listener for each row

        holder.main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(v.getContext(), ArticleViewer.class);
                intent.putExtra("WINDOW_TITLE",cView.getTitle());
                intent.putExtra("CONTENT",cView.getCaption());
                intent.putExtra("IMG",cView.getImg());
                intent.putExtra("LIST_ID",1);
                intent.putExtra("LINK",cView.getPdf());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) c,v.findViewById(R.id.image),"img");
                v.getContext().startActivity(intent,options.toBundle());


            }
        });
    }


    @Override
    public int getItemCount() {
        return articleList.size();
    }
}

