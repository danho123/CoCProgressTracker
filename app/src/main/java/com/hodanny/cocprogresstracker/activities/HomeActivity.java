package com.hodanny.cocprogresstracker.activities;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.hodanny.cocprogresstracker.R;
import com.hodanny.cocprogresstracker.databases.BuildingDataSource;
import com.hodanny.cocprogresstracker.databases.DbHandler;
import com.hodanny.cocprogresstracker.fragments.ViewPagerFragment;
import com.hodanny.cocprogresstracker.models.Building;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends FragmentActivity {


    private BuildingDataSource mDatabase;
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    private int mCurrentTownhall = 5;
    private HashMap<String, ArrayList<Building>> mMap;
    public static HashMap<String, Integer> mMaxMap;


    private void InitDatabase()
    {
        DbHandler myDbHelper=new DbHandler(this);
        try {

            myDbHelper.initializeDataBase();
            mDatabase = new BuildingDataSource(this);
            mDatabase.open();
            mDatabase.populateUserProgress(mCurrentTownhall, true);
            mMap = mDatabase.selectAllUserProgress2();
            mMaxMap = mDatabase.getEntityMaxLevelMap(mCurrentTownhall);
            mDatabase.close();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        InitDatabase();
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

//            case R.id.action_contact:
//                QuickContactFragment dialog = new QuickContactFragment();
//                dialog.show(getSupportFragmentManager(), "QuickContactFragment");
//                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mMap.keySet().toArray()[position].toString();
        }

        @Override
        public int getCount() {
            return mMap.keySet().toArray().length;
        }

        @Override
        public Fragment getItem(int position)
        {
            return ViewPagerFragment.newInstance(mMap.get(mMap.keySet().toArray()[position].toString()));
        }


    }
}