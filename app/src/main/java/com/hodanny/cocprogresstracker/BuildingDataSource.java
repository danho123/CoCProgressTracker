package com.hodanny.cocprogresstracker;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dan on 8/11/2015.
 */
public class BuildingDataSource {

    // Database fields
    private SQLiteDatabase database;
    private DbHandler dbHelper;

    public BuildingDataSource(Context context) {
        dbHelper = new DbHandler(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Building> getAllComments() {
        List<Building> comments = new ArrayList<Building>();

        Cursor cursor = database.query(dbHelper.DB_TABLE_BUILDINGS, dbHelper.DB_TABLE_BUILDINGS_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Building comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private Building cursorToComment(Cursor cursor) {
        Building comment = new Building();

        comment.setName(cursor.getString(1));
        comment.setLevel(cursor.getInt(2));
        comment.setHitpoints(cursor.getInt(3));
        comment.setCost(cursor.getInt(4));

        String resourceType = cursor.getString(5);

        comment.setResourceType(ResourceType.GOLD);

        int timeInSeconds = cursor.getInt(6);
        //comment.setBuildTime());
        comment.setTownhallRequirement(cursor.getInt(7));


        return comment;
    }
}