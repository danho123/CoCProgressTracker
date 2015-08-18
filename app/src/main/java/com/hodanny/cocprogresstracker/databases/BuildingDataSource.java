package com.hodanny.cocprogresstracker.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.hodanny.cocprogresstracker.models.Building;
import com.hodanny.cocprogresstracker.utils.Log;
import com.hodanny.cocprogresstracker.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by dan on 8/11/2015.
 */
public class BuildingDataSource {

    // Database fields
    private static SQLiteDatabase database;
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

    public HashMap<String, ArrayList<Building>> selectAllUserProgress2()
    {
        HashMap<String, ArrayList<Building>> buildings = new HashMap<>();
        String query = "SELECT UP._id, EL.EntityId, E.Name, EL.Level, E.Type FROM UserProgress UP Inner Join EntityLevels EL ON UP.EntityLevelId=EL._id  INNER JOIN Entities E ON E._id=EL.EntityId";
        Cursor cursor = database.rawQuery(query, new String[]{});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Building building = new Building();

            String buildingName = cursor.getString(2);
            long buildingLevel = cursor.getLong(3);
            String buildingType = cursor.getString(4);

            building.setId(cursor.getInt(0));
            building.setEntityId(cursor.getInt(1));
            building.setName(buildingName);
            building.setLevel(buildingLevel);

            if(buildings.containsKey(buildingType))
            {
                buildings.get(buildingType).add(building);
            }
            else
            {
               ArrayList<Building> newList = new ArrayList<>();
               newList.add(building);
               buildings.put(buildingType, newList);
            }
            cursor.moveToNext();
        }
        return buildings;
    }

    /**
     * Populates UserProgress table based on TH Limits
     * @param townhallLevel
     */
    public void populateUserProgress(int townhallLevel, boolean overwrite)
    {
        Cursor cur = database.rawQuery("SELECT COUNT(*) FROM UserProgress", null);
        if (cur != null) {
            cur.moveToFirst();                       // Always one row returned.
            if (cur.getInt (0) != 0 && !overwrite) {               // if there are records already then don't need to populate

                return;
            }
        }
        String deleteQuery = "DELETE FROM UserProgress";
        database.execSQL(deleteQuery);
        String MyQuery =
                "SELECT EL._id, TH.TH" + townhallLevel + " FROM TownhallLimits as TH INNER JOIN Entities as E ON TH.EntityId=E._id INNER JOIN EntityLevels EL ON E._id=EL.EntityId WHERE EL.Level is 0";

        Cursor cursor = database.rawQuery(MyQuery, new String[]{});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long i = cursor.getLong(1);
            while (i > 0) {
                String sql = "INSERT INTO UserProgress ( EntityLevelId ) VALUES ( " + cursor.getLong(0) + ")";
                database.execSQL(sql);
                i--;
           }
           cursor.moveToNext();
        }
        cursor.close();
    }

    public void updateUserProgressEntity(Building building)
    {
        String selectQuery = "SELECT EntityLevels._id FROM EntityLevels WHERE EntityLevels.EntityId=? AND EntityLevels.Level=?";
        Cursor cursor = database.rawQuery(selectQuery, new String[]{String.valueOf(building.getEntityId()), String.valueOf(building.getLevel())});
        cursor.moveToFirst();
        String updateQuery = String.format("UPDATE UserProgress SET entityLevelId=%d WHERE _id=%d", cursor.getInt(0), building.getId());
        database.execSQL(updateQuery);
    }

    public HashMap<String,Integer> getEntityMaxLevelMap(int townhallLevel)
    {
        HashMap<String,Integer> map = new HashMap<>();
        String query = "SELECT Entities.Name, MAX(Level) FROM EntityLevels INNER JOIN Entities ON Entities._id=EntityLevels.EntityId WHERE EntityLevels.Requirement <= ? GROUP BY EntityId Order BY EntityId asc";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(townhallLevel)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            map.put(cursor.getString(0), cursor.getInt(1));
            cursor.moveToNext();
        }
        cursor.close();
        return map;
    }

}