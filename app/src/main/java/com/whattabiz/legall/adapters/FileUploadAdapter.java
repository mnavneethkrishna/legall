package com.whattabiz.legall.adapters;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whattabiz.legall.R;
import com.whattabiz.legall.activity.DocumentScrutinyActivity;
import com.whattabiz.legall.models.FileModel;

import java.util.List;

/**
 * Created by User on 8/3/2016.
 */

public class FileUploadAdapter extends RecyclerView.Adapter<FileUploadAdapter.MyViewHolder> {

    private List<FileModel> fileList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView filename;
        TextView delete;


        public MyViewHolder(View view) {
            super(view);
            filename = (TextView) view.findViewById(R.id.filename);
            delete = (TextView) view.findViewById(R.id.delete);


        }
    }


    public FileUploadAdapter(List<FileModel> fileList) {
        this.fileList = fileList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_file_upload_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final FileModel item = fileList.get(position);
        holder.filename.setText(item.getFilename());


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(final View view) {
                DocumentScrutinyActivity.getInstance().filesList.remove(position);
                DocumentScrutinyActivity.getInstance().updateFilesSelected();
            }

        });


    }


    @Override
    public int getItemCount() {
        return fileList.size();
    }
}

