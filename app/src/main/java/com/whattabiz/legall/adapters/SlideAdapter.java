package com.whattabiz.legall.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whattabiz.legall.R;
import com.whattabiz.legall.User;
import com.whattabiz.legall.activity.PdfViewer;
import com.whattabiz.legall.models.SlideModel;

import java.util.List;

/**
 * Created by User on 8/3/2016.
 */

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.MyViewHolder> {

    private List<SlideModel> slideList;

    Context c;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        ImageView img;
        CardView mainView;


        public MyViewHolder(View view) {
            super(view);
            c = view.getContext();
            title = (TextView) view.findViewById(R.id.txt_title);
            img = (ImageView) view.findViewById(R.id.img);
            mainView = (CardView) view.findViewById(R.id.view_main);

        }
    }


    public SlideAdapter(List<SlideModel> slideList) {
        this.slideList = slideList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_slide_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SlideModel bulletText = slideList.get(position);
        holder.title.setText(bulletText.getTitle().toString());
        //holder.img.setImageResource(bulletText.getImgId());


        Log.i("Legall", "inside bindview");
        Log.i("Legall", "inside if");
        Log.i("Legall", "" + User.getInstance(c).slideImages.size());
        holder.img.setImageBitmap(User.getInstance(c).slideImages.get(position));


        //Setting on click listener for each row
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(view.getContext(), PdfViewer.class);
                intent.putExtra("WINDOW_TITLE", bulletText.getTitle());
                intent.putExtra("LIST_ID", 1);
                intent.putExtra("LINK", bulletText.getPdf());
                view.getContext().startActivity(intent);

            }
        });


    }


    @Override
    public int getItemCount() {
        return slideList.size();
    }
}

