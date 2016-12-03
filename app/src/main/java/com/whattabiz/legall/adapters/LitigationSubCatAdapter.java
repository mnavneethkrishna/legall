package com.whattabiz.legall.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whattabiz.legall.R;
import com.whattabiz.legall.activity.LawyerListActivity;
import com.whattabiz.legall.models.LitigationCategoryModel;

import java.util.List;

/**
 * Created by User on 8/3/2016.
 */

public class LitigationSubCatAdapter extends RecyclerView.Adapter<LitigationSubCatAdapter.MyViewHolder> {

    private List<LitigationCategoryModel> categoryModelList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, caption;


        LinearLayout main_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            caption = (TextView) view.findViewById(R.id.txt_caption);

            main_view=(LinearLayout) view.findViewById(R.id.view_main);
        }
    }


    public LitigationSubCatAdapter(List<LitigationCategoryModel> litigationCategoryModelList) {
        this.categoryModelList = litigationCategoryModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_litigation_category, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final LitigationCategoryModel categoryModel = categoryModelList.get(position);
        holder.title.setText(categoryModel.getTitle());
        holder.caption.setText(categoryModel.getCaption());



        //Setting on click listener for each row

        holder.main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //Intent intent=new Intent(v.getContext(),detailsPage.class);
               // v.getContext().startActivity(intent);
                Intent i = new Intent(v.getContext(),LawyerListActivity.class);
                i.putExtra("WINDOW_TITLE",categoryModel.getTitle());
                i.putExtra("CAT_ID",categoryModel.getCatId());
                i.putExtra("SUBCAT_ID",categoryModel.getUrl());
                v.getContext().startActivity(i);

            }
        });



    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }
}
