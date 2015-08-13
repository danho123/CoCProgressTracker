package com.hodanny.cocprogresstracker.models;

import android.graphics.Bitmap;

import com.hodanny.cocprogresstracker.ResourceType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Comparator;

/**
 * Created by dan on 7/30/2015.
 */



public class Building implements Comparable<Building> {
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

    public int getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(int buildTime) {
        this.buildTime = buildTime;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
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
    private int buildTime;
    private long level;
    private int townhallRequirement;
    private String name;

    private Bitmap image;

    public Building(){};

    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof Building))
        {
            return false;
        }
        if(o == this)
        {
            return true;
        }

        Building building = (Building)o;
        return new EqualsBuilder().
                append(name, building.name).
                append(level, building.level).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17,31).
                append(name).append(level).toHashCode();
    }

    @Override
    public int compareTo(Building another) {
        return (int)(level - another.level);
    }
}
