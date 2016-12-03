package com.whattabiz.legall.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whattabiz.legall.R;
import com.whattabiz.legall.activity.CaseDetailsActivity;
import com.whattabiz.legall.models.ChildCaseDetailsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rumaan on 11-11-2016.
 */

class ChildCaseDetailRecyclerAdapter extends RecyclerView.Adapter<ChildCaseDetailRecyclerAdapter.ViewHolder> {
    private Context mContext;

    private List<ChildCaseDetailsModel> list = new ArrayList<>();

    ChildCaseDetailRecyclerAdapter(Context context, List<ChildCaseDetailsModel> childList) {
        this.mContext = context;
        this.list = childList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_child_case_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.time.setText(list.get(position).getTime());
        holder.caseTitle.setText(list.get(position).getCaseTitle());
        holder.caseDetails.setText(list.get(position).getCaseDetails());
        holder.userName.setText(list.get(position).getAssosUserName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, CaseDetailsActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView time;
        private TextView caseTitle;
        private TextView caseDetails;
        private TextView userName;
        private LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.case_time);
            caseTitle = (TextView) itemView.findViewById(R.id.case_title);
            caseDetails = (TextView) itemView.findViewById(R.id.case_details);
            userName = (TextView) itemView.findViewById(R.id.client_name);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
        }
    }


}
