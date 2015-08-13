package com.hodanny.cocprogresstracker.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.hodanny.cocprogresstracker.models.Building;
import com.hodanny.cocprogresstracker.utils.Log;
import com.hodanny.cocprogresstracker.ResourceType;

import java.util.ArrayList;
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

    public TreeMap<String, TreeMap<Building, Integer>> selectAllUserProgress2()
    {
        //key - BuildingName
        //value - TreeMap
        //      key - building
        //      value - count
        TreeMap<String, TreeMap<Building, Integer>> buildings = new TreeMap<String, TreeMap<Building, Integer>>();
        String query = "SELECT B.Name, BD.Level, COUNT(*) as Count FROM UserProgress UP Inner Join BuildingDescriptions BD ON UP.FK_BuildingDescriptionId=BD._id  INNER JOIN Buildings B ON B._id=BD.FK_BuildingID GROUP BY B.Name, BD.Level ORDER BY B.Name asc";
        Cursor cursor = database.rawQuery(query, new String[]{});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Building building = new Building();
            String buildingName = cursor.getString(0);
            long buildingLevel = cursor.getLong(1);
            long count = cursor.getLong(2);

            building.setName(buildingName);
            building.setLevel(buildingLevel);

            if(buildings.containsKey(buildingName))
            {
                buildings.get(buildingName).put(building, (int)count);
            }
            else
            {
               TreeMap<Building,Integer> map = new TreeMap<>();
                map.put(building, (int)count);
                buildings.put(buildingName, map);
            }
            cursor.moveToNext();
        }
        return buildings;
    }


    public int getBuildingDescriptionId(Building building)
    {
        return 0;
    }

    /**
     * Updates building when user performs a downgrade or upgrade on a building
     * @param newBuilding new building description id
     * @param id UserProgress PK
     */
    public void ReplaceUserProgressBuilding(int newBuilding, int id)
    {
        String query = String.format("UPDATE UserProgress SET FK_BuildingDescriptionId=%d WHERE _id=%d", newBuilding, id);
        database.execSQL(query);
    }

    /**
     *  Retrieve possible downgrade and upgrades filtered by townhall and building
     * @param townhallLevel The active townhall level
     * @param building The building getting upgraded
     * @return
     */
    public List<Building> SelectPossibleBuildingUpgrade(int townhallLevel, Building building)
    {
        List<Building> buildings = new ArrayList<>();
        String query = String.format("SELECT B.Name, BD.Level FROM UserProgress UP Inner Join BuildingDescriptions BD ON UP.FK_BuildingDescriptionId=BD._id INNER JOIN Buildings B ON B._id=BD.FK_BuildingID WHERE B.Name = \"%s\" AND BD.Level \"%d\"  ORDER BY BD.Level asc", building.getName(), townhallLevel);
        Log.v(query);

        Cursor cursor = database.rawQuery(query, new String[]{});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Building bldng = new Building();
            bldng.setName(cursor.getString(0));
            bldng.setLevel(cursor.getLong(1));
            buildings.add(bldng);
            cursor.moveToNext();
        }
        cursor.close();
        return buildings;

    }

    /**
     * Populates UserProgress table based on TH Limits
     * @param townhallLevel
     */
    public void populateUserProgress(int townhallLevel)
    {
        String deleteQuery = "DELETE FROM UserProgress";
        database.execSQL(deleteQuery);
        String MyQuery =
                "SELECT BD._id, TH.TH" + townhallLevel + " FROM TownhallLimits as TH " +
                "INNER JOIN Buildings as B ON TH.FK_BuildingId=B._id " +
                "INNER JOIN BuildingDescriptions BD ON B._id=BD.FK_BuildingId " +
                "WHERE Level=0 ORDER BY B.Name asc";

        Cursor cursor = database.rawQuery(MyQuery, new String[]{});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long i = cursor.getLong(1);
            while (i > 0) {
                String sql = "INSERT INTO UserProgress ( FK_BuildingDescriptionId ) VALUES ( " + cursor.getLong(0) + ")";
                database.execSQL(sql);
                i--;
           }
           cursor.moveToNext();
        }

        cursor.close();
        // make sure to close the cursor
    }
}