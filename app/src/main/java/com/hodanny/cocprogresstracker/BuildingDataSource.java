package com.hodanny.cocprogresstracker;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

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

    public List<Building> getAllBuildings() {
        List<Building> buildings = new ArrayList<Building>();

        Cursor cursor = database.query(dbHelper.DB_TABLE_BUILDINGDESCRIPTIONS, dbHelper.DB_TABLE_BUILDINGDESCRIPTIONS_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Building building = cursorToBuilding(cursor);
            buildings.add(building);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return buildings;
    }

    private Building cursorToBuilding(Cursor cursor) {
        Building building = new Building();

        building.setName(cursor.getString(1));
        building.setLevel(cursor.getInt(2));
        building.setHitpoints(cursor.getInt(3));
        building.setCost(cursor.getInt(4));

        String resourceType = cursor.getString(5);

        building.setResourceType(ResourceType.GOLD);

        int timeInSeconds = cursor.getInt(6);
        building.setBuildTime(timeInSeconds);
        building.setTownhallRequirement(cursor.getInt(7));


        return building;
    }

    public void populateUserProgress(int townhallLevel)
    {
        String MyQuery =
                "SELECT BD._id, TH.TH" + townhallLevel + " FROM TownhallLimits as TH " +
                "INNER JOIN Buildings as B ON TH.FK_BuildingId=B._id " +
                "INNER JOIN BuildingDescription BD ON B._id=BD.FK_BuildingId " +
                "WHERE Level=1 ORDER BY B.Name asc";

        Cursor cursor = database.rawQuery(MyQuery, new String[]{});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String sql = "INSERT INTO UserProgress ( FK_BuildingDescriptionId, Count ) VALUES ( " + cursor.getLong(0) + "," + cursor.getLong(1) + ")";
            database.execSQL(sql);
            cursor.moveToNext();
        }

        cursor.close();

        database.close();
        // make sure to close the cursor
    }
}