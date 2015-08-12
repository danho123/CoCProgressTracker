package com.hodanny.cocprogresstracker;

import android.database.SQLException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public int currentTownhall = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHandler myDbHelper=new DbHandler(this);


        try {

            myDbHelper.initializeDataBase();
            BuildingDataSource dbSource = new BuildingDataSource(this);
            dbSource.open();

            dbSource.populateUserProgress(currentTownhall);
            dbSource.close();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;


        }
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
}
