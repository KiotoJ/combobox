package com.example.baghii.comboboxapp.controller;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baghii.comboboxapp.R;
import com.example.baghii.comboboxapp.task.HttpGetRequest;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Baghii on 09/12/2017.
 */

public class TranslateImageListAdapter extends ArrayAdapter<Bitmap> {
    private Activity mContext;
    private ArrayList<Bitmap> mBitmaps;
    String datapath = "";
    private TessBaseAPI mTess;

    public TranslateImageListAdapter(@NonNull Activity context, ArrayList<Bitmap> bitmaps) {
        super(context, R.layout.list_translate_image,bitmaps);
        this.mContext =context;
        this.mBitmaps = bitmaps;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TranslateImageHolder holder;
        Bitmap image = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.list_translate_image, parent, false);
            holder = new TranslateImageHolder();

            //  Lookup view for data population
            holder.imageSource = (ImageView) convertView.findViewById(R.id.imageSource);
            holder.textView = (TextView) convertView.findViewById(R.id.OCRTextView);

            //Set Image View
            holder.imageSource.setImageBitmap(image);

            //init Tesseract API
            //make sure training data has been copied
            datapath = mContext.getFilesDir()+ "/tesseract/";
            checkFile(new File(datapath + "tessdata/"));
            String language = "eng";
            mTess = new TessBaseAPI();
            mTess.init(datapath, language);

            //Process on translating image
            String OCRresult = processImage(image);

            //Translate text result
            new HttpGetRequest((Activity)mContext,holder.textView).execute(OCRresult);

        } else{
            holder=(TranslateImageHolder) convertView.getTag();
        }
        return convertView;
    }

    private void checkFile(File dir) {
        if (!dir.exists()&& dir.mkdirs()){
            copyFiles();
        }
        //The directory exists, but there is no data file in it
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }
    private void copyFiles() {
        try {
            //location we want the file to be at
            String filepath = datapath + "/tessdata/eng.traineddata";
            AssetManager assetManager = mContext.getAssets();
            //open byte streams for reading/writing
            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String processImage(Bitmap img){
        mTess.setImage(img);
        return mTess.getUTF8Text();
    }
    private static class TranslateImageHolder{
        private ImageView imageSource;
        private TextView textView;
    }

}
