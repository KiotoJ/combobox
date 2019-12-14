package com.example.baghii.comboboxapp.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baghii.comboboxapp.R;
import com.example.baghii.comboboxapp.task.ImageSourceRequest;

import java.io.File;


/**
 * Created by JON on 16/11/2017.
 */

public class ImageListAdapter extends ArrayAdapter<File> {
    private Context mContext;
    File[] myImageClassList;

    public ImageListAdapter(Context context, File[] myImageClassList) {
        super(context, R.layout.list_image,myImageClassList);
        this.mContext =context;
        this.myImageClassList = myImageClassList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Myholder holder;
        File img = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_image, parent, false);

            // Lookup view for data population
            holder = new Myholder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.title = (TextView) convertView.findViewById(R.id.textView);

            holder.title.setText(img.getName());
            String imgPath = img.getAbsolutePath();
            if (img.isDirectory()){
                File[] listFiles = img.listFiles();
                for(int i=0; i<listFiles.length; i++){
                    if(listFiles[i].getAbsolutePath().endsWith(".jpg")||listFiles[i].getAbsolutePath().endsWith(".png")){
                        imgPath=listFiles[i].getAbsolutePath();
                        break;
                    }
                }
            }
            new ImageSourceRequest(holder.imageView).execute(imgPath);
        }else{
            holder=(Myholder) convertView.getTag();
        }
        return  convertView;
    }

    private static class Myholder{
        private ImageView imageView;
        private TextView title;
    }
}
