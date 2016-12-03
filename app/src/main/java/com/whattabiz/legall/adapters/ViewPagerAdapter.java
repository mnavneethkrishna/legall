package com.whattabiz.legall.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.whattabiz.legall.R;
import com.whattabiz.legall.User;
import com.whattabiz.legall.activity.ArticleViewer;
import com.whattabiz.legall.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 10/29/2016.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private  List<SlideModel> mResources = new ArrayList<>();
    private  ArrayList<Bitmap> slideImages = new ArrayList<>();

    public ViewPagerAdapter(Context mContext, List<SlideModel> mResources) {
        this.mContext = mContext;
            this.mResources = mResources;
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((CardView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.row_slide_item, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.img);
        TextView title = (TextView) itemView.findViewById(R.id.txt_title);

        CardView viewMain = (CardView) itemView.findViewById(R.id.view_main);

        viewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ArticleViewer.class);
                intent.putExtra("WINDOW_TITLE", mResources.get(position).getTitle());
                intent.putExtra("LIST_ID", 1);
                intent.putExtra("LINK", mResources.get(position).getPdf());
                intent.putExtra("IMG",mResources.get(position).getLink());
                intent.putExtra("CONTENT",mResources.get(position).getCaption());
                view.getContext().startActivity(intent);
            }
        });

        title.setText(mResources.get(position).getTitle());
        title.setTypeface(User.getInstance(mContext).getFont(mContext));


        Log.i("Legall",""+position);

        Picasso.with(mContext).load(Uri.parse(mResources.get(position).getLink().replaceAll(" ","%20"))).into(imageView);

/*

    if (User.getInstance(mContext).slideImages.isEmpty()) {
    Picasso.with(mContext).load(Uri.parse(mResources.get(position).getLink().replaceAll(" ","%20"))).into(new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            //User.getInstance(mContext).slideImages.add(bitmap);
            imageView.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    });

        //User.getInstance(mContext).slideImages.add(position,BitmapFactory.decodeResource(mContext.getResources(),R.drawable.img_loading));
}else if (User.getInstance(mContext).slideImages.size() == 4){
    imageView.setImageBitmap(User.getInstance(mContext).slideImages.get(position));
}

        */
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((CardView) object);
    }
}
