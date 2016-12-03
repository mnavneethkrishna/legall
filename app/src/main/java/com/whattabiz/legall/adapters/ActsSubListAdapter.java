package com.whattabiz.legall.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whattabiz.legall.models.ActsListModel;
import com.whattabiz.legall.activity.PdfViewer;
import com.whattabiz.legall.R;

import java.util.List;

/**
 * Delete this adapter and connected items if the backend contains only mai category and files
 * change the main category adapter to view the pdf directly at mainview Onclick
 */

public class ActsSubListAdapter extends RecyclerView.Adapter<ActsSubListAdapter.MyViewHolder> {

    private List<ActsListModel> ActsList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,caption;
        LinearLayout main_view;
        String type, link;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            caption = (TextView) view.findViewById(R.id.txt_caption);
            main_view=(LinearLayout) view.findViewById(R.id.view_main);
        }
    }


    public ActsSubListAdapter(List<ActsListModel> ActsList) {
        this.ActsList = ActsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_acts_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ActsListModel bulletText = ActsList.get(position);
        holder.title.setText(bulletText.getTitle().replaceAll(".pdf",""));
        holder.caption.setText(bulletText.getCaption());
       /* holder.caption.setText(bulletText.getType());
        holder.rowThumb.setImageResource(bulletText.getLink());*/


        //Setting on click listener for each row

        holder.main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(v.getContext(), PdfViewer.class);
                intent.putExtra("WINDOW_TITLE",bulletText.getTitle());
                intent.putExtra("LIST_ID",1);
                intent.putExtra("LINK",bulletText.getLink());
                v.getContext().startActivity(intent);


            }
        });


    }




    @Override
    public int getItemCount() {
        return ActsList.size();
    }
}

