package com.hodanny.cocprogresstracker.utils;

/**
 * Created by danho on 8/19/2015.
 */
public class TimeConverter {
    public static String formatSeconds(int seconds)
    {
        StringBuilder sb = new StringBuilder();
        int days;
        int hours;
        int minutes;

        days = seconds / 86400;
        hours = (seconds%86400) / 3600;
        minutes = (seconds%86400%3600) / 60;
        seconds = seconds % 60;

        if(days != 0)
        {
            sb.append(days + "d");
        }
        if(hours != 0)
        {
            if(sb.length() !=0)
            {
                sb.append(" ");
            }
            sb.append(hours + "h");
        }
        if(minutes != 0)
        {
            if(sb.length() !=0)
            {
                sb.append(" ");
            }
            sb.append(minutes + "m");
        }
        if(seconds != 0)
        {
            if(sb.length() !=0)
            {
                sb.append(" ");
            }
            sb.append(seconds + "s");

        }
        return sb.toString();
    }
}
