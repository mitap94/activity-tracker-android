package com.petrovic.m.dimitrije.activitytracker.utils;

public class Utils {

    private static final String APP_NAME = "ActivityTracker";

    public static String getLogTag(Class classObj) {
        return APP_NAME + "." + classObj.getSimpleName();
    }
}
