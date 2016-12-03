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
import com.whattabiz.legall.models.FaqModel;
import com.whattabiz.legall.models.FaqModel;

import java.util.List;

/**
 * Created by User on 8/3/2016.
 */

public class FaqListAdapter extends RecyclerView.Adapter<FaqListAdapter.MyViewHolder> {

    private List<FaqModel> faqList;


    Context c;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView question,answer;
        

        public MyViewHolder(View view) {
            super(view);
            question = (TextView) view.findViewById(R.id.question);
            answer = (TextView) view.findViewById(R.id.answer);
           
            c=view.getContext();
        }
    }


    public FaqListAdapter(List<FaqModel> faqList) {
        this.faqList = faqList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_faq_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final FaqModel cView = faqList.get(position);
        holder.question.setText(cView.getQuestion());
        holder.answer.setTypeface(User.getInstance(c).getFont(c));
        holder.answer.setText(cView.getAnswer());






    }


    @Override
    public int getItemCount() {
        return faqList.size();
    }
}

