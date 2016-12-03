package com.whattabiz.legall.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whattabiz.legall.activity.NotificationViewActivity;
import com.whattabiz.legall.R;
import com.whattabiz.legall.models.NotificationModel;

import java.util.List;

/**
 * Notification list adapter
 *
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<NotificationModel> notificationList;

    String WINDOW_TITLE = "";


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,caption,date;
        ImageView icon;
        FrameLayout imageBg;
        LinearLayout main_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            caption = (TextView) view.findViewById(R.id.txt_caption);
            icon = (ImageView) view.findViewById(R.id.icon);
            imageBg = (FrameLayout) view.findViewById(R.id.img_bg);
            date = (TextView) view.findViewById(R.id.txt_date);
            main_view=(LinearLayout) view.findViewById(R.id.view_main);
        }
    }


    public NotificationAdapter(List<NotificationModel> notificationList) {
        this.notificationList = notificationList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final NotificationModel cView = notificationList.get(position);
        holder.title.setText(cView.getTitle());
        holder.caption.setText(cView.getCaption());

        //status code 1, for waiting
        if (cView.getStatus()==1)
            holder.imageBg.setBackgroundResource(R.drawable.circle_blue);
        //status code 2, for success
        else if (cView.getStatus()==2)
            holder.imageBg.setBackgroundResource(R.drawable.circle_green);
        //status code 0, for failed
        else if(cView.getStatus()==0)
            holder.imageBg.setBackgroundResource(R.drawable.circle_red);
        //default blue
        else
            holder.imageBg.setBackgroundResource(R.drawable.circle_blue);




        //checking type
        if(cView.getType().matches("appointment")) {
            holder.icon.setImageResource(R.mipmap.nav_ic_appoinment);
            WINDOW_TITLE = "Appointment";
        }
        else if(cView.getType().matches("document")) {
            holder.icon.setImageResource(R.drawable.ic_casefile);
            WINDOW_TITLE = "Document Verification";
        }

        else {
            holder.icon.setImageResource(R.drawable.ic_id);
            WINDOW_TITLE = "Notification";
        }

        //Setting on click listener for each row

        holder.main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(v.getContext(), NotificationViewActivity.class);
                intent.putExtra("WINDOW_TITLE",WINDOW_TITLE);
                intent.putExtra("MESSAGE",cView.getMessage());
                v.getContext().startActivity(intent);


            }
        });
    }


    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}

