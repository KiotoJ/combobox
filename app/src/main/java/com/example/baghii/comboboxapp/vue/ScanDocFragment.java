package com.example.baghii.comboboxapp.vue;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.baghii.comboboxapp.R;
import com.example.baghii.comboboxapp.controller.ImageListAdapter;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.File;
import java.io.IOException;

/**
 * Created by Baghii on 15/11/2017.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScanDocFragment extends Fragment {
    private RelativeLayout mScanLayout;
    private RelativeLayout mNoScanLayout;
    private RelativeLayout mImageViewLayout;
    private Button mScanButton;
    private ImageButton mAddScanButton;
    private GridView mGridView;
    private ImageListAdapter adapter;

    private static final int REQUEST_CODE = 99;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scan_doc, container, false);

        mNoScanLayout = (RelativeLayout) rootView.findViewById(R.id.layout_no_scan_doc);
        mScanLayout = (RelativeLayout) rootView.findViewById(R.id.layout_scan_doc);
        mImageViewLayout = (RelativeLayout) rootView.findViewById(R.id.layout_show_image);
        mScanButton = (Button) rootView.findViewById(R.id.action_scan);
        mGridView = (GridView) rootView.findViewById(R.id.grid_scan_img);
        mAddScanButton = (ImageButton) rootView.findViewById(R.id.action_add_scan);

        File[] imageClasses = populateDataImage();
        mImageViewLayout.setVisibility(View.INVISIBLE);

        if(imageClasses.length==0){
            mScanLayout.setVisibility(View.INVISIBLE);
        }else{
            mNoScanLayout.setVisibility(View.INVISIBLE);
            adapter = new ImageListAdapter(getContext(), imageClasses);
            mGridView.setAdapter(adapter);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    mScanLayout.setVisibility(View.INVISIBLE);
                    mImageViewLayout.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(getActivity(),ScanDocFullImageActivity.class);
                    intent.putExtra("image_path",adapter.getItem(position).getAbsolutePath());
                    startActivity(intent);
                }
            });
         }

        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });
        mAddScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle(R.string.scan_doc_page_title);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.hamburger);
    }

    private File[] populateDataImage(){
        ActivityCompat.requestPermissions(getActivity(),new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE},123);
        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File folder= new File(dir);
        folder.setReadable(true);
        File[] files = folder.listFiles();
        return files;
    }

    private void scan(){
        int preference= ScanConstants.OPEN_CAMERA;
        Intent intent = new Intent(getActivity(), ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_scan_doc, menu);
        super.onCreateOptionsMenu(menu, inflater);

        //Associate searchable config with SearchView
        final SearchManager searchManager = (SearchManager) getActivity().getSystemService (Context.SEARCH_SERVICE);
        SearchView mSearchView = (SearchView) menu.findItem(R.id.action_search_scanned_img).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        mSearchView.setIconifiedByDefault(true);
        mSearchView.setQueryHint(getResources().getString(R.string.search_scan_doc));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                //image saved in storage phone, "Pictures"
                MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),bitmap, "titl_imag",null);
                getActivity().getContentResolver().delete(uri, null, null);
                //affichage document
                mNoScanLayout.setVisibility(View.INVISIBLE);
                mScanLayout.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                //scannedImageView.setImageBitmap(bitmap);
                Toast.makeText(getContext(), "ImageClass saved on 'Pictures'", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
