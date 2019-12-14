package com.example.baghii.comboboxapp.vue;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.baghii.comboboxapp.R;
import com.example.baghii.comboboxapp.controller.TranslateImageListAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Baghii on 15/11/2017.
 */

public class TranslateImageFragment extends Fragment {
    Uri uri;
    private Spinner spinnerLeft;
    private Spinner spinnerRight;
    private ListView mListView;

    private ImageButton btn_translate;
    private ImageButton btn_view_image;
    private ImageButton btn_add_img;
    private ImageButton btn_select_image;

    ArrayAdapter<CharSequence> mAdapter;

    TranslateImageListAdapter translateImageListAdapter;
    ArrayList<Bitmap> bitmaps;

    static final int PICK_IMAGE_REQUEST=1;
    static  final int CAMERA_PICK_REQUEST=123;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_translate_image, container, false);

        spinnerLeft = (Spinner) rootView.findViewById(R.id.spinnerLeft);
        spinnerRight = (Spinner) rootView.findViewById(R.id.spinnerRight);
        mListView = (ListView) rootView.findViewById(R.id.list_trans_img);
        btn_translate=(ImageButton) rootView.findViewById(R.id.btn_translate);
        btn_view_image = (ImageButton) rootView.findViewById(R.id.btn_view_image);
        btn_add_img=(ImageButton) rootView.findViewById(R.id.btn_add_image);
        btn_select_image=(ImageButton) rootView.findViewById(R.id.btn_select_image);

        mAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.language,R.layout.spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLeft.setAdapter(mAdapter);
        spinnerRight.setAdapter(mAdapter);

        //Automatically select two spinner items
        spinnerLeft.setSelection(1);
        spinnerRight.setSelection(2);

        //Initialize Listview
        bitmaps = new ArrayList<>();
        translateImageListAdapter = new TranslateImageListAdapter(getActivity(),bitmaps);
        mListView.setAdapter(translateImageListAdapter);

        btn_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_view_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PICK_REQUEST);
            }
        });
        btn_select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImageView selectedImg= (ImageView) view.findViewById(R.id.imageView);
                showFileChooser();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        appCompatActivity.getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_translate_img, menu);
        super.onCreateOptionsMenu(menu, inflater);

        //Associate searchable config with SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService (Context.SEARCH_SERVICE);
        SearchView mSearchView = (SearchView) menu.findItem(R.id.action_translate_img).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setQueryHint(getResources().getString(R.string.search_translate_img));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = getBitmap(requestCode, resultCode, data);
        //Update adapter
        translateImageListAdapter.add(bitmap);
    }

    private void showFileChooser(){
        boolean isKitKat = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_img)),PICK_IMAGE_REQUEST);
        } else {
            isKitKat = false;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select file"), PICK_IMAGE_REQUEST);
        }
    }

    private File[] populateDataImage(){
        ActivityCompat.requestPermissions(getActivity(),new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE},123);
        String picturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        String cameraPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        String rootPath = Environment.getRootDirectory().getAbsolutePath();

        File pictureFolder= new File(picturePath);
        File cameraFolder= new File(cameraPath);
        File rootFolder= new File(rootPath);

        File[] folder = {pictureFolder,cameraFolder,rootFolder};
        return folder;
    }

    public Bitmap getBitmap(int requestCode, int resultCode, Intent data){
        Bitmap bitmap = null;
      //  Toast.makeText(getActivity(), "data : "+data+" & gtdat: "+ data.getData(), Toast.LENGTH_LONG).show();
        if((requestCode == PICK_IMAGE_REQUEST || requestCode == CAMERA_PICK_REQUEST)
                && resultCode == RESULT_OK){
            this.uri = data.getData();
            //Choose Image
            if (requestCode == CAMERA_PICK_REQUEST) {//open camera
                bitmap = (Bitmap) data.getExtras().get("data");
                // Toast.makeText(getActivity(), "code Image : "+resultCode+" =="+ RESULT_OK, Toast.LENGTH_LONG).show();
            } else if (requestCode == PICK_IMAGE_REQUEST)//open gallery
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), this.uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return  bitmap;
    }


}