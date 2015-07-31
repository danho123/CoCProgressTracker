package com.hodanny.cocprogresstracker;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Created by dan on 7/31/2015.
 */
public class FileReader {

    public Context context;

    public FileReader(Context context) {
        this.context = context;
    }

    public List<Townhall> ParseTownhallLimits()
    {
        List<Townhall> townhalls = new ArrayList<>();
        try {
            BufferedReader br=new BufferedReader(new
                    InputStreamReader(context.getAssets().open("thlimits.csv")));
            CSVReader reader = new CSVReader(br);
            String [] nextLine;

            String[] header = reader.readNext();

            while ((nextLine = reader.readNext()) != null) {
                Townhall th = new Townhall();
                th.setLevel(Integer.parseInt(nextLine[0]));
                th.setHitpoints(Integer.parseInt(nextLine[1]));
                th.setCost(Integer.parseInt(nextLine[2]));
                for(int i = 3; i < nextLine.length; i++)
                {
                    th.Limits.put(header[i], Integer.parseInt(nextLine[i]));
                }
                townhalls.add(th);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return townhalls;
    }

    public String[] GetCsvHeader(String filename)
    {
        String[] header = null;
        try {
            BufferedReader br = new BufferedReader(new
                    InputStreamReader(context.getAssets().open(filename)));
            CSVReader reader = new CSVReader(br);
            header = reader.readNext();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return header;
    }
}
