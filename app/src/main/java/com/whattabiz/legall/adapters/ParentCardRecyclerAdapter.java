package com.whattabiz.legall.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whattabiz.legall.Lawyers;
import com.whattabiz.legall.R;
import com.whattabiz.legall.activity.CaseDetailsActivity;

/**
 * Created by Rumaan on 11-11-2016.
 */

public class ParentCardRecyclerAdapter extends RecyclerView.Adapter<ParentCardRecyclerAdapter.ViewHolder> {
    private Context mContext;

    public ParentCardRecyclerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_parent_case_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // set the details here
        holder.caseTitle.setText(Lawyers.getInstance().caseModelArrayList.get(position).getCaseTitle());
        holder.caseDetails.setText(Lawyers.getInstance().caseModelArrayList.get(position).getCaseDetails());
        holder.caseTime.setText(Lawyers.getInstance().caseModelArrayList.get(position).getTime());
        holder.caseClientName.setText(Lawyers.getInstance().caseModelArrayList.get(position).getClientNAme());
        holder.caseClientPhone.setText(Lawyers.getInstance().caseModelArrayList.get(position).getClientPhone());
        holder.day.setText(Lawyers.getInstance().caseModelArrayList.get(position).getDay());

        holder.caseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CaseDetailsActivity.class);
                // add details into intent
                intent.putExtra("TITLE", Lawyers.getInstance().caseModelArrayList.get(position).getCaseTitle());
                intent.putExtra("DESC", Lawyers.getInstance().caseModelArrayList.get(position).getCaseDetails());
                intent.putExtra("PREV_DATE", Lawyers.getInstance().caseModelArrayList.get(position).getPrevHearing());
                intent.putExtra("NEXT_DATE", Lawyers.getInstance().caseModelArrayList.get(position).getNextHearing());
                intent.putExtra("NAME", Lawyers.getInstance().caseModelArrayList.get(position).getClientNAme());
                intent.putExtra("PHONE", Lawyers.getInstance().caseModelArrayList.get(position).getClientPhone());
                intent.putExtra("CASE_STATUS", Lawyers.getInstance().caseModelArrayList.get(position).getCaseStatus());
                intent.putExtra("pos", position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Lawyers.getInstance().caseModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView day;
        TextView caseTime;
        TextView caseTitle;
        TextView caseDetails;
        TextView caseClientName;
        TextView caseClientPhone;
        CardView caseCard;

        ViewHolder(View itemView) {
            super(itemView);
            day = (TextView) itemView.findViewById(R.id.case_day);
            caseClientName = (TextView) itemView.findViewById(R.id.client_name);
            caseClientPhone = (TextView) itemView.findViewById(R.id.client_phone);
            caseDetails = (TextView) itemView.findViewById(R.id.case_details);
            caseTime = (TextView) itemView.findViewById(R.id.case_time);
            caseTitle = (TextView) itemView.findViewById(R.id.case_title);
            caseCard = (CardView) itemView.findViewById(R.id.case_card);
        }
    }
}
