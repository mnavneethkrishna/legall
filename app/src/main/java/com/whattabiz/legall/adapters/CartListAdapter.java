package com.whattabiz.legall.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.whattabiz.legall.models.CartItemModel;
import com.whattabiz.legall.R;
import com.whattabiz.legall.activity.CartActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 8/3/2016.
 */

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {

    private List<CartItemModel> cartItemList;
    private List<CartItemModel> savedList = new ArrayList<>();
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,cost,type;
        CardView main_view;
        Button remove;
       

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            type = (TextView) view.findViewById(R.id.txt_type);
            cost = (TextView) view.findViewById(R.id.txt_cost);
            remove = (Button) view.findViewById(R.id.btn_remove); 
            
            main_view=(CardView) view.findViewById(R.id.view_main);
            
        }
    }


    public CartListAdapter(List<CartItemModel> cartItemList, Context context) {
        this.cartItemList = cartItemList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cart_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CartItemModel item = cartItemList.get(position);
        holder.title.setText(item.getTitle());
        Integer price = Double.valueOf(item.getCost()).intValue();
        holder.cost.setText("₹ "+price);
        holder.type.setText(item.getCat());
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setMessage("DO you want to Remove this Item from Cart ?")
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        CartActivity.getInstance().cartItemList.remove(position);

                        int total=0;

                        if (!cartItemList.isEmpty())

                            for (CartItemModel c : cartItemList) {

                                total += Double.valueOf(c.getCost()).intValue();

                            }

                        ((CartActivity)mContext).updateCart();
                        CartActivity.mAdapter.notifyDataSetChanged();


                    }

                }).show();
            }
        });

            }





    @Override
    public int getItemCount() {
        return cartItemList.size();
    }
}

