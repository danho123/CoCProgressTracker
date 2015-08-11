package com.hodanny.cocprogresstracker;

import android.graphics.Bitmap;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * Created by dan on 7/30/2015.
 */



public class Building {
    public int getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Time getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
//
//        long timeUnit = Long.parseLong((buildTime.substring(0, buildTime.length() - 1)));
//        if(buildTime.endsWith("s"))
//        {
//            timeUnit = TimeUnit.SECONDS.toMillis(timeUnit);
//        }
//        else if(buildTime.endsWith("h"))
//        {
//            timeUnit = TimeUnit.HOURS.toMillis(timeUnit);
//        }
//        else if(buildTime.endsWith("d"))
//        {
//            timeUnit = TimeUnit.DAYS.toMillis(timeUnit);
//        }

        this.buildTime = new Time(1);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTownhallRequirement() {
        return townhallRequirement;
    }

    public void setTownhallRequirement(int townhallRequirement) {
        this.townhallRequirement = townhallRequirement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    private int hitpoints;
    private int cost;

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    private ResourceType resourceType;
    private Time buildTime;
    private int level;
    private int townhallRequirement;
    private String name;

    private Bitmap image;

    public Building(){};

}
