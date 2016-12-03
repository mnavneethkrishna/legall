package com.whattabiz.legall.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whattabiz.legall.R;
import com.whattabiz.legall.models.TemplateModel;
import com.whattabiz.legall.activity.BuyNowActivity;

import java.util.List;

/**
 * Created by User on 8/3/2016.
 */

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.MyViewHolder> {

    private List<TemplateModel> templateList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,cost;
        CardView main_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            cost = (TextView) view.findViewById(R.id.txt_cost);
            main_view=(CardView) view.findViewById(R.id.view_main);

        }
    }


    public TemplateAdapter(List<TemplateModel> templateList) {
        this.templateList = templateList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_templates, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final TemplateModel template = templateList.get(position);
        holder.title.setText(template.getTitle());
        holder.cost.setText("₹ "+template.getCost());


        holder.main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(v.getContext(), BuyNowActivity.class);

                intent.putExtra("TITLE",template.getTitle());
                intent.putExtra("COST",template.getCost());
                intent.putExtra("ID",template.getId());
                intent.putExtra("LINK","Draft Deed");

                v.getContext().startActivity(intent);


            }
        });


    }


    @Override
    public int getItemCount() {
        return templateList.size();
    }
}

