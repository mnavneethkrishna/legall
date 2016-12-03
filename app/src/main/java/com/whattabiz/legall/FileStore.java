package com.whattabiz.legall;

import com.whattabiz.legall.models.FileModel;

import java.util.ArrayList;

/**
 * Created by User on 10/16/2016.
 */

public class FileStore {

    public static ArrayList<FileModel> data = new ArrayList<>();

    public void FileStore(){

    }

    public void remove(int position){
        data.remove(position);
    }

    public void add(FileModel file){
        data.add(file);
    }


    public FileModel getItem(int position){
        if (data!=null)
            return data.get(position);
        else return null;
    }

    public int size(){
        return data.size();
    }

    public ArrayList<FileModel> getData(){
        return data;
    }
}
