package com.hodanny.cocprogresstracker;

/**
 * Created by dan on 7/30/2015.
 */

enum ResourceType
{
    GOLD,
    ELIXIR,
    DARK_ELIXIR,
    GEM
}


public class Resource {
    ResourceType resourceType;
    int cost;
}