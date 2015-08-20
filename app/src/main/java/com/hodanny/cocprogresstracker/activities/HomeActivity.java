package com.hodanny.cocprogresstracker.activities;

import android.net.Uri;
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
import com.hodanny.cocprogresstracker.fragments.SummaryFragment;
import com.hodanny.cocprogresstracker.fragments.TownhallFragment;
import com.hodanny.cocprogresstracker.fragments.ViewPagerFragment;
import com.hodanny.cocprogresstracker.models.Building;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends FragmentActivity implements SummaryFragment.OnFragmentInteractionListener, TownhallFragment.OnFragmentInteractionListener {


    public static BuildingDataSource mDatabase;
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    private int mCurrentTownhall = 10;

    //viewpager data, divides the entities by categories.
    private HashMap<String, ArrayList<Building>> mMap;

    //keeps track of the max level for each entity depending on townhall level
    public static HashMap<String, Integer> mMaxMap;

    private void InitDatabase()
    {
        DbHandler myDbHelper=new DbHandler(this);
        try {

            myDbHelper.initializeDataBase();
            mDatabase = new BuildingDataSource(this);
            mDatabase.open();
            //populate userprogress table if it doesn't exist
            mDatabase.populateUserProgress(mCurrentTownhall, true);



            mMap = mDatabase.selectAllUserProgress();
            //
            mMaxMap = mDatabase.getEntityMaxLevelMap(mCurrentTownhall);

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
        adapter = new MyPagerAdapter(getSupportFragmentManager(), mMap.keySet().toArray());

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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<String> pages = new ArrayList<>();
        public MyPagerAdapter(FragmentManager fm, Object[] arr) {
            super(fm);
            for(int i = 0; i < arr.length;i++)
            {
                pages.add(arr[i].toString());
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pages.get(position);
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public Fragment getItem(int position)
        {
            if(pages.get(position).equals("Summary"))
            {
                return SummaryFragment.newInstance("","");
            }
            else
            {
                return ViewPagerFragment.newInstance(mMap.get(pages.get(position)));
            }

        }


    }
}