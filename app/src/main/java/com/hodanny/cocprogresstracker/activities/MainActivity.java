package com.hodanny.cocprogresstracker.activities;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hodanny.cocprogresstracker.activities.adapters.MainRecyclerAdapter;
import com.hodanny.cocprogresstracker.R;
import com.hodanny.cocprogresstracker.databases.BuildingDataSource;
import com.hodanny.cocprogresstracker.databases.DbHandler;
import com.hodanny.cocprogresstracker.fragments.BuildingEditFragment;
import com.hodanny.cocprogresstracker.models.Building;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;


public class MainActivity extends FragmentActivity implements MainRecyclerAdapter.ClickListener {

    public int mCurrentTownhall = 5;

    private BuildingDataSource mDatabase;
    private RecyclerView mRecyclerView;
    private TextView mTextViewTownhall;
    private MainRecyclerAdapter mMainRecyclerAdapter;

    private TreeMap<String, TreeMap<Building,Integer>> mUserProgress = new TreeMap<>();

    private void InitViews(Context context)
    {
        mRecyclerView = (RecyclerView)findViewById(R.id.main_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mMainRecyclerAdapter = new MainRecyclerAdapter(mUserProgress, this);
        mMainRecyclerAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mMainRecyclerAdapter);



        mTextViewTownhall = (TextView)findViewById(R.id.main_townhall_text);
        mTextViewTownhall.setText(String.format("Townhall %d", mCurrentTownhall));

    }

    private void InitDatabase()
    {
        DbHandler myDbHelper=new DbHandler(this);
        try {

            myDbHelper.initializeDataBase();
            mDatabase = new BuildingDataSource(this);
            mDatabase.open();
            mDatabase.populateUserProgress(mCurrentTownhall);
            mUserProgress = mDatabase.selectAllUserProgress2();
            mDatabase.close();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitDatabase();
        InitViews(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void itemClicked(View v, int pos) {
        Bundle bundle = new Bundle();

        ArrayList<Building> buildings = new ArrayList<>();

        TreeMap<Building,Integer> treeMap = mUserProgress.get(mUserProgress.keySet().toArray()[pos]); //get keyset of nested treemap

        bundle.putParcelableArrayList("buildings", buildings);
        DialogFragment newFragment = new BuildingEditFragment();
        newFragment.setArguments(bundle);
        newFragment.show(((FragmentActivity)v.getContext()).getSupportFragmentManager(), "dialog");
    }
}
