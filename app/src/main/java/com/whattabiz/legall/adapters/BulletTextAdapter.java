package com.whattabiz.legall.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whattabiz.legall.models.BulletText;
import com.whattabiz.legall.R;
import com.whattabiz.legall.activity.startupCompanyActivity;

import java.util.List;

/**
 * Created by User on 8/3/2016.
 */

public class BulletTextAdapter extends RecyclerView.Adapter<BulletTextAdapter.MyViewHolder> {

    private List<BulletText> bulletTextList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        CardView main_view;
        String type, link;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);

            main_view=(CardView) view.findViewById(R.id.view_main);
        }
    }


    public BulletTextAdapter(List<BulletText> bulletTextList) {
        this.bulletTextList = bulletTextList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_bullet_title, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BulletText bulletText = bulletTextList.get(position);
        holder.title.setText(bulletText.getTitle());
       /* holder.caption.setText(bulletText.getType());
        holder.rowThumb.setImageResource(bulletText.getLink());*/


        //Setting on click listener for each row



        holder.main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Intent intent = new Intent(v.getContext(), startupCompanyActivity.class);
                intent.putExtra("WINDOW_TITLE",bulletText.getTitle());
                intent.putExtra("LIST_ID",bulletText.getLink());
                intent.putExtra("DESC",bulletText.getType());
                //intent.putExtra("LINK",bulletText.getType());
                v.getContext().startActivity(intent);


            }
        });
    }


    @Override
    public int getItemCount() {
        return bulletTextList.size();
    }
}

