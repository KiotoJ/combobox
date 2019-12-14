package com.example.baghii.comboboxapp.vue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.baghii.comboboxapp.R;

public class ScanDocFullImageActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_doc_full_image);

        String img_path = getIntent().getStringExtra("image_path");
        imageView = (ImageView) findViewById(R.id.img_full);
        Bitmap bitmap = BitmapFactory.decodeFile(img_path);
        imageView.setImageBitmap(bitmap);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
