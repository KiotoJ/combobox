package com.example.baghii.comboboxapp.controller;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.baghii.comboboxapp.R;
import com.example.baghii.comboboxapp.task.HttpGetRequest;

import java.util.ArrayList;

/**
 * Created by Baghii on 08/12/2017.
 */

public class TranslateTextListAdapter extends ArrayAdapter<String> {
    private Activity mContext;
    private ArrayList<String> mySearchTextList;

    public TranslateTextListAdapter(Activity context, ArrayList<String> mySearchTextList) {
        super(context, R.layout.list_translate_text, mySearchTextList);
        this.mContext = context;
        this.mySearchTextList = mySearchTextList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TranslateTextHolder holder;
        String textToTranslate = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_translate_text, parent,false);
            holder = new TranslateTextHolder();

            // Lookup view for data population
            holder.text = (TextView) convertView.findViewById(R.id.translateTextView);
            holder.btn_voice = (ImageButton)convertView.findViewById(R.id.btn_voice);
            holder.btn_translate = (ImageButton)convertView.findViewById(R.id.btn_traslate);

            //Translate Text
            new HttpGetRequest((Activity) mContext,holder.text).execute(textToTranslate);

            //-------------------Listener------------------------------------------
            final CharSequence txt = holder.text.getText();
            final TranslateTextHolder finalHolder = holder;
            holder.btn_translate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new HttpGetRequest((Activity)mContext, finalHolder.text).execute(String.valueOf(txt));
                }
            });
            holder.btn_voice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            Log.wtf("issue", textToTranslate+"---"+String.valueOf(txt));
        } else{
            holder=(TranslateTextHolder) convertView.getTag();
        }
        return convertView;
    }

    private static class TranslateTextHolder{
        private TextView text;
        private ImageButton btn_voice;
        private ImageButton btn_translate;
    }
}