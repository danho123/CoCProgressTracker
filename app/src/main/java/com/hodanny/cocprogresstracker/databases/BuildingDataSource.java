package com.hodanny.cocprogresstracker.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.hodanny.cocprogresstracker.models.Building;
import com.hodanny.cocprogresstracker.models.Walls;
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

    public HashMap<String, ArrayList<Building>> selectAllUserProgress()
    {
        HashMap<String, ArrayList<Building>> buildings = new HashMap<>();
        String query = "SELECT UP._id, EL.EntityId, E.Name, EL.Level, E.Type, EL.Cost, EL.ResourceType, EL.TimeInSeconds, EL.Image, EL2.Cost as NextUpgradeCost, EL2.TimeInSeconds as NextUpgradeTime FROM UserProgress UP Inner Join EntityLevels EL ON UP.EntityLevelId=EL._id  INNER JOIN Entities E ON E._id=EL.EntityId LEFT JOIN EntityLevels EL2 ON EL.entityid=EL2.entityid AND EL.Level+1=EL2.Level";
        Cursor cursor = database.rawQuery(query, new String[]{});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Building building = new Building();

            String buildingName = cursor.getString(2);
            long buildingLevel = cursor.getLong(3);
            String buildingType = cursor.getString(4);

            if(buildingType.equalsIgnoreCase("townhall"))
            {
                Log.e("DEBUG");
            }

            int buildingCost = cursor.getInt(5);
            String buildingResourceType = cursor.getString(6);
            int buildingTimeInSeconds = cursor.getInt(7);
            String buildingImage = cursor.getString(8);

            int nextUpgradeCost = cursor.getInt(9);
            int nextUpgradeTime = cursor.getInt(10);

            ResourceType resourceType = null;
            if(buildingResourceType.equalsIgnoreCase("gold"))
            {
                resourceType = ResourceType.GOLD;
            }
            else if(buildingResourceType.equalsIgnoreCase("elixir"))
            {
                resourceType = ResourceType.ELIXIR;
            }
            else if(buildingResourceType.equalsIgnoreCase("dark elixir"))
            {
                resourceType = ResourceType.DARK_ELIXIR;
            }
            else if(buildingResourceType.equalsIgnoreCase("gold;elixir"))
            {
                resourceType = ResourceType.GOLD_OR_ELIXIR;
            }
            else
            {
                resourceType = ResourceType.GOLD;
            }

            building.setId(cursor.getInt(0));
            building.setEntityId(cursor.getInt(1));
            building.setName(buildingName);
            building.setLevel(buildingLevel);
            building.setCost(buildingCost);
            building.setResourceType(resourceType);
            building.setBuildTime(buildingTimeInSeconds);
            building.setImage(buildingImage);

            building.setUpgradeCost(nextUpgradeCost);
            building.setUpgradeTime(nextUpgradeTime);

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
        if (cur != null && !overwrite) {
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

    /**
     * returns the cost and build time of the new entity
     * @param building
     */
    public int[] updateUserProgressEntity(Building building)
    {
        String selectQuery = "SELECT EntityLevels._id, EntityLevels.Cost, EntityLevels.TimeInSeconds FROM EntityLevels WHERE EntityLevels.EntityId=? AND EntityLevels.Level=?";
        Cursor cursor = database.rawQuery(selectQuery, new String[]{String.valueOf(building.getEntityId()), String.valueOf(building.getLevel())});
        cursor.moveToFirst();

        String updateQuery = String.format("UPDATE UserProgress SET entityLevelId=%d WHERE _id=%d", cursor.getInt(0), building.getId());
        database.execSQL(updateQuery);

        selectQuery = "SELECT EntityLevels._id, EntityLevels.Cost, EntityLevels.TimeInSeconds FROM EntityLevels WHERE EntityLevels.EntityId=? AND EntityLevels.Level=?";
        cursor = database.rawQuery(selectQuery, new String[]{String.valueOf(building.getEntityId()), String.valueOf(building.getLevel()+1)});
        cursor.moveToFirst();

        int[] ret;

        if(!cursor.isAfterLast())
        {
             ret = new int[] {cursor.getInt(1), cursor.getInt(2)};
        }
        else
        {
            ret = null; //it's maxed. no more upgrades.
        }
        cursor.close();
        return ret;

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

    public List<Walls> getWalls()
    {
        ArrayList<Walls> walls = new ArrayList<>();
        String query = "SELECT E.Name, EL.Level, EL.Cost, EL.ResourceType, CASE WHEN UP._id IS NULL THEN 0 ELSE COUNT(*) END AS Count FROM EntityLevels EL  LEFT JOIN UserProgress UP ON UP.EntityLevelId=EL._id INNER JOIN Entities E ON E._id=EL.entityId WHERE E.Name=\"Wall\" GROUP BY Level";
        Cursor cursor = database.rawQuery(query, new String[]{});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Walls wall = new Walls();
            wall.setName(cursor.getString(0));
            wall.setLevel(cursor.getInt(1));
            wall.setCost(cursor.getInt(2));
            wall.setResourceType(ResourceType.GOLD);
            wall.setCount(cursor.getInt(4));
            cursor.moveToNext();
        }
        cursor.close();
        return walls;
    }

    public void updateTownhallLevel(int i )
    {

    }

}