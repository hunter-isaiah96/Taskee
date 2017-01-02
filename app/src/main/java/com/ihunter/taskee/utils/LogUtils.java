package com.ihunter.taskee.utils;

import android.util.Log;

import java.text.SimpleDateFormat;

/**
 * Created by Master Bison on 12/9/2016.
 */

public class LogUtils {

    static String TAG = "QPlanningUI";

    public static void logI(String log){
        Log.i(TAG, log);
    }

    public static void logD(String log){
        Log.d(TAG, log);
    }

    public static void logE(String log){
        Log.e(TAG, log);
    }

    public static void logSystem(Object object){
        System.out.println(object);
    }

    public static void logFullDate(long date){
        System.out.println(new SimpleDateFormat("MMMM, dd, yyyy h:mm a").format(date));
    }

}
