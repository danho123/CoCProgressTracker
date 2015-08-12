package com.hodanny.cocprogresstracker.utils;

/**
 * Created by dan on 8/4/2015.
 */
public class Log {
    private static final String TAG = "cocprogresstracker";

    public static void e(String msg) {
        android.util.Log.e(TAG, msg);
    }

    public static void e(String msg, Throwable e) {
        android.util.Log.e(TAG, msg, e);
    }

    public static void v(String msg)
    {
        android.util.Log.v(TAG,msg);
    }

}
