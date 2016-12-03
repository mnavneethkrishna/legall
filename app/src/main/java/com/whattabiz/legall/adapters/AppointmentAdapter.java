package com.whattabiz.legall.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whattabiz.legall.models.AppointmentModel;
import com.whattabiz.legall.activity.AppointmentViewActivity;
import com.whattabiz.legall.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 8/3/2016.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> {

    private List<AppointmentModel> appoinmentList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,time,address;
        CircleImageView image;
        CardView main_view;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txt_name);
            time = (TextView) view.findViewById(R.id.txt_dtime);
            address = (TextView) view.findViewById(R.id.txt_address);
            image = (CircleImageView) view.findViewById(R.id.propic);
            main_view=(CardView) view.findViewById(R.id.view_main);
        }
    }


    public AppointmentAdapter(List<AppointmentModel> appoinmentList) {
        this.appoinmentList = appoinmentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_appointment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AppointmentModel cView = appoinmentList.get(position);
        holder.name.setText(cView.getName());
        holder.time.setText(cView.getTime());
        holder.address.setText(cView.getAddress());
        holder.image.setImageResource(cView.getImgid());
       /* holder.time.setText(cView.getType());
        holder.rowThumb.setImageResource(cView.getImgid());*/


        //Setting on click listener for each row

        holder.main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(v.getContext(), AppointmentViewActivity.class);
                intent.putExtra("WINDOW_TITLE",cView.getName());
                intent.putExtra("LIST_ID",1);
                intent.putExtra("LINK",cView.getImgid());
                v.getContext().startActivity(intent);


            }
        });
    }


    @Override
    public int getItemCount() {
        return appoinmentList.size();
    }
}

