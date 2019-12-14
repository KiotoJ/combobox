package com.example.baghii.comboboxapp.vue;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.baghii.comboboxapp.R;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

public class MainActivity extends AppCompatActivity {
    CircleMenu circleMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       circleMenu = (CircleMenu) findViewById(R.id.circle_menu);

        circleMenu.openMenu();
        circleMenu.setMainMenu(Color.parseColor("#2586FF"),R.drawable.ic_scan_doc,R.drawable.ic_translate_text)
                .addSubMenu(Color.parseColor("#2586FF"),R.drawable.ic_translate_text)
                .addSubMenu(Color.parseColor("#2586FF"),R.drawable.ic_scan_doc)
                .addSubMenu(Color.parseColor("#2586FF"),R.drawable.ic_translate_image)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        Intent i = new Intent(MainActivity.this, TabsActivity.class);
                        i.putExtra("index",index);
                        startActivity(i);
                    }
                });
    }
}
