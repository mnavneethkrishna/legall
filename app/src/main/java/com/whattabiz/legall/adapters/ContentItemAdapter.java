package com.whattabiz.legall.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whattabiz.legall.models.ContentItem;
import com.whattabiz.legall.R;
import com.whattabiz.legall.activity.BuyNowActivity;

import java.util.List;

/**
 * Created by User on 8/3/2016.
 */

public class ContentItemAdapter extends RecyclerView.Adapter<ContentItemAdapter.MyViewHolder> {

    private List<ContentItem> contentItemList;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, caption;

        ImageView rowThumb;
        LinearLayout main_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            caption = (TextView) view.findViewById(R.id.txt_caption);
            rowThumb = (ImageView) view.findViewById(R.id.img_row_thumb);
            main_view=(LinearLayout) view.findViewById(R.id.view_main);
        }
    }


    public ContentItemAdapter(List<ContentItem> contentItemList) {
        this.contentItemList = contentItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_contentlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ContentItem contentItem = contentItemList.get(position);
        holder.title.setText(contentItem.getTitle());
        holder.caption.setText("₹ "+contentItem.getPrice());


        //Setting on click listener for each row

        holder.main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //Intent intent=new Intent(v.getContext(),detailsPage.class);
               // v.getContext().startActivity(intent);
                Intent i = new Intent(v.getContext(),BuyNowActivity.class);
                i.putExtra("WINDOW_TITLE",contentItem.getTitle());
                i.putExtra("TITLE",contentItem.getTitle());
                i.putExtra("LINK",contentItem.getCat());
                i.putExtra("ID",contentItem.getLink());
                i.putExtra("COST",contentItem.getPrice());
                v.getContext().startActivity(i);

            }
        });



    }

    @Override
    public int getItemCount() {
        return contentItemList.size();
    }
}
