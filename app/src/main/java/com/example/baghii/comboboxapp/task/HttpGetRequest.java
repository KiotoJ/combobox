package com.example.baghii.comboboxapp.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baghii.comboboxapp.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

/**
 * Created by Baghii on 07/12/2017.
 */

public class HttpGetRequest extends AsyncTask<String,Void,String> {
    private static final String BASE_URL= "http://mymemory.translated.net/api/get?q=";
    private TextView text;
    Activity context;

    public HttpGetRequest(Activity context, TextView text) {
        this.context = context;
        this.text = text;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        /*TextView textView = (TextView) rowView.findViewById(R.id.translateTextView);
        if(textView==null){
            textView = (TextView) rowView.findViewById(R.id.OCRTextView);
        }*/
        text.setText(context.getResources().getString(R.string.wait));
    }

    @Override
    protected String doInBackground(String... strings) {
        String stringURL = strings[0];
        return getJSON(stringURL);
    }

    @Override
    protected void onPostExecute(String s) {
        if(s==null){
            Toast.makeText(context,"Verify your network",Toast.LENGTH_LONG).show();
        } else if (s.contains("SELECT")){
            Toast.makeText(context,s,Toast.LENGTH_LONG).show();
        }else {
            /*TextView textView = (TextView) rowView.findViewById(R.id.translateTextView);
            if(textView==null){
                textView = (TextView) rowView.findViewById(R.id.OCRTextView);
            }*/
            text.setText(s);
        }
    }

    public String getJSON (String text) {
        String translated = null;
        Spinner spinnerLeft = (Spinner) context.findViewById(R.id.spinnerLeft);
        Spinner spinnerRight = (Spinner) context.findViewById(R.id.spinnerRight);

        Locale srcLanguage = getLocale(spinnerLeft.getSelectedItem().toString());
        Locale dstLanguage = getLocale(spinnerRight.getSelectedItem().toString());
        HttpClient client = new DefaultHttpClient();
        try {
            String query = URLEncoder.encode(text, "UTF-8");
            String lang = URLEncoder.encode(srcLanguage.getLanguage()+"|"+dstLanguage.getLanguage(), "UTF-8");

            String url = BASE_URL+query+"&langpair="+lang;
            Log.wtf("request",url);
            HttpGet httpGet = new HttpGet(url);

            HttpResponse response = client.execute(httpGet);
            org.apache.http.StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode==200){
                JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
                translated = json.getJSONObject("responseData").getString("translatedText");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return translated;
    }

    public Locale getLocale(String language) {
        Locale spanish = new Locale("es", "ES");
        Locale arabic = new Locale("ar", "AR");
        Locale local = Locale.ENGLISH;
        if(language.equals("French"))
            local = Locale.FRENCH;
        else if(language.equals("Arabic"))
            local = arabic;
        else if(language.equals("Spanish"))
            local = spanish;
        else if(language.equals("English"))
            local = Locale.ENGLISH;
        return local;
    }
}