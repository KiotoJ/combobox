package com.example.baghii.comboboxapp.vue;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.baghii.comboboxapp.R;
import com.example.baghii.comboboxapp.controller.TranslateTextListAdapter;

import java.util.ArrayList;


/**
 * Created by Baghii on 15/11/2017.
 */

public class TranslateTextFragment extends Fragment {

    private Spinner spinnerLeft;
    private Spinner spinnerRight;
    private ArrayAdapter<CharSequence> adapter;
    public ListView mListView;
    public TranslateTextListAdapter translateTextListAdapter;
    public ArrayList<String> mTextList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_translate_text, container, false);

        spinnerLeft = (Spinner) rootView.findViewById(R.id.spinnerLeft);
        spinnerRight = (Spinner) rootView.findViewById(R.id.spinnerRight);
        mListView = (ListView) rootView.findViewById(R.id.list_trans_text);

        adapter = ArrayAdapter.createFromResource(getActivity(),R.array.language,R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLeft.setAdapter(adapter);
        spinnerRight.setAdapter(adapter);
        //Automatically select two spinner items
        spinnerLeft.setSelection(1);
        spinnerRight.setSelection(2);

        //Custom array adapter
        mTextList = new ArrayList<>();
        translateTextListAdapter = new TranslateTextListAdapter(getActivity(),mTextList);
        mListView.setAdapter(translateTextListAdapter);

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
        inflater.inflate(R.menu.menu_translate_text, menu);
        super.onCreateOptionsMenu(menu, inflater);

        //Associate searchable config with SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService (Context.SEARCH_SERVICE);
        SearchView mSearchView = (SearchView) menu.findItem(R.id.action_translate_text).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setQueryHint(getResources().getString(R.string.search_translate_text));

        //On searchview listener
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                translateTextListAdapter.add(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}