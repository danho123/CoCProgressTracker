package com.hodanny.cocprogresstracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by dan on 7/30/2015.
 */
public class DbHandler extends SQLiteOpenHelper {

    FileReader fileReader;
    SQLiteDatabase db;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "coc.db";

    private static final String TABLE_DEFENSE = "DefenseBuildings";

    private static final String COLUMN_DEFENSE_ID = "_id";
    private static final String COLUMN_DEFENSE_NAME = "Name";
    private static final String COLUMN_DEFENSE_LEVEL = "Level";

    private static final String TABLE_THLIMITS = "Townhall";

    private static final String COLUMN_THLIMITS_ID = "_id";
    private static final String COLUMN_THLIMITS_LEVEL = "Level";
    private static final String COLUMN_THLIMITS_CANNON = "Cannon";
    private static final String COLUMN_THLIMITS_MORTAR = "Mortar";

    private static final String TABLE_USERPROGRESS = "UserProgress";

    private static final String COLUMN_USERPROGRESS_ID = "_id";
    private static final String COLUMN_USERPROGRESS_BUILDING = "Building";
    private static final String COLUMN_USERPROGRESS_LEVEL = "Level";

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        fileReader = new FileReader(context);
        db = this.getWritableDatabase();
    }

    public void createUserProgress(Townhall th)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERPROGRESS);
        String CREATE_USERPROGRESS_TABLE = "CREATE TABLE " + TABLE_USERPROGRESS + "(" +
                COLUMN_USERPROGRESS_ID + " INTEGER PRIMARY KEY," +
                COLUMN_USERPROGRESS_BUILDING + " TEXT," +
                COLUMN_USERPROGRESS_LEVEL + " INTEGER)";

        ArrayList<Building> buildings = new ArrayList<>();

        db.execSQL(CREATE_USERPROGRESS_TABLE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DEFENSE_TABLE =
                "CREATE TABLE " + TABLE_DEFENSE + "(" +
                        COLUMN_DEFENSE_ID + " INTEGER PRIMARY KEY," +
                        COLUMN_DEFENSE_NAME + " TEXT," +
                        COLUMN_DEFENSE_LEVEL + " INTEGER)";

        String CREATE_THLIMITS_TABLE =
                "CREATE TABLE " + TABLE_THLIMITS + "(" +
                        COLUMN_THLIMITS_ID + " INTEGER PRIMARY KEY," + constructCreateTableQuery("thlimits.csv");

        db.execSQL(CREATE_DEFENSE_TABLE);
        db.execSQL(CREATE_THLIMITS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEFENSE);
        onCreate(db);
    }

    private String constructCreateTableQuery(String filename)
    {
        String [] townhallHeader = fileReader.GetCsvHeader(filename);
        StringBuilder townhallColumns = new StringBuilder();
        for(String header : townhallHeader)
        {
            if(townhallColumns.length() != 0)
            {
                townhallColumns.append(" INTEGER, ");
            }
            townhallColumns.append(header);
        }
        townhallColumns.append(" INTEGER)");
        return townhallColumns.toString();
    }


}
