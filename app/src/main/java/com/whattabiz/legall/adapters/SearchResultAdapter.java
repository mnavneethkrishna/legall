package com.whattabiz.legall.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whattabiz.legall.Lawyers;
import com.whattabiz.legall.R;
import com.whattabiz.legall.activity.ActsSubCatActivity;
import com.whattabiz.legall.activity.PdfViewer;
import com.whattabiz.legall.activity.SearchResultActivity;
import com.whattabiz.legall.activity.startupCompanyActivity;
import com.whattabiz.legall.models.SearchResultModel;

import java.util.List;

/**
 * Created by User on 8/3/2016.
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder> {

    private List<SearchResultModel> resultList;


    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        LinearLayout main_view;
        String type, link;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);

            main_view=(LinearLayout) view.findViewById(R.id.view_main);
        }
    }


    public SearchResultAdapter(List<SearchResultModel> resultList, Context context) {
        this.resultList = resultList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_search_result, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SearchResultModel result = resultList.get(position);

//        String QUERY = ((SearchResultActivity)context).QUERY;
//        String resultText = result.getTitle().replaceAll(QUERY,"<font color='#1e88e5'>"+QUERY+"</font>");
        holder.title.setText(result.getTitle());
       /* holder.caption.setText(result.getType());
        holder.rowThumb.setImageResource(result.getLink());*/


        //Setting on click listener for each row
        





        holder.main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (result.getType()==0) {


                    Intent intent = new Intent(v.getContext(), ActsSubCatActivity.class);
                    intent.putExtra("WINDOW_TITLE", result.getTitle());
                    intent.putExtra("LIST_ID", result.getId());
                    intent.putExtra("LINK_ID", result.getLink());
                    v.getContext().startActivity(intent);

                }else {
                    Intent intent = new Intent(v.getContext(), PdfViewer.class);
                    intent.putExtra("WINDOW_TITLE",result.getTitle());
                    intent.putExtra("LIST_ID",1);
                    intent.putExtra("LINK",result.getLink());
                    v.getContext().startActivity(intent);

                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return resultList.size();
    }
}

