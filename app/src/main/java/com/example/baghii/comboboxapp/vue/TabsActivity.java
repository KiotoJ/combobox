package com.example.baghii.comboboxapp.vue;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.baghii.comboboxapp.R;
import com.example.baghii.comboboxapp.controller.SectionsPagerAdapter;

public class TabsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);
        Fragment[] fragments = {
                Fragment.instantiate(this, TranslateTextFragment.class.getName()),
                Fragment.instantiate(this, TranslateImageFragment.class.getName()),
                Fragment.instantiate(this, ScanDocFragment.class.getName())
        };
        setSupportActionBar(mToolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),fragments);
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(0);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_translate_text);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_translate_image);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_scan_doc);

       int mItemIndex = getIntent().getIntExtra("index",0);
       switch (mItemIndex){
           case 1:
               mItemIndex=2;
               break;
           case 2:
               mItemIndex=1;
               break;
       }
        //Set default selected tab item
        mViewPager.setCurrentItem(mItemIndex);
        invalidateOptionsMenu(mItemIndex);

        //-----------------------------LISTENER--------------------------------------
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void invalidateOptionsMenu(int position) {
        for(int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            Fragment fragment = mSectionsPagerAdapter.getItem(i);
            fragment.setHasOptionsMenu(i == position);
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
