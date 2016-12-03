package com.whattabiz.legall.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whattabiz.legall.models.ActsDocModel;
import com.whattabiz.legall.activity.PdfViewer;
import com.whattabiz.legall.R;

import java.util.List;

/**
 * Created by User on 8/3/2016.
 */

public class ActsDocAdapter extends RecyclerView.Adapter<ActsDocAdapter.MyViewHolder> {

    private List<ActsDocModel> actsDocList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,caption;
        CardView main_view;
       

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            caption = (TextView) view.findViewById(R.id.txt_caption);
            main_view=(CardView) view.findViewById(R.id.view_main);
            
        }
    }


    public ActsDocAdapter(List<ActsDocModel> actsDocList) {
        this.actsDocList = actsDocList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_acts_pdf_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ActsDocModel document = actsDocList.get(position);
        holder.title.setText(document.getTitle());
        holder.caption.setText(document.getCaption());
       /* holder.caption.setText(document.getType());
        holder.rowThumb.setImageResource(document.getLink());*/


        //Setting on click listener for each row

        holder.main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(v.getContext(), PdfViewer.class);
                intent.putExtra("WINDOW_TITLE",document.getTitle());
                intent.putExtra("LIST_ID",1);
                intent.putExtra("LINK",document.getLink());
                v.getContext().startActivity(intent);


            }
        });

       
    }




    @Override
    public int getItemCount() {
        return actsDocList.size();
    }
}

