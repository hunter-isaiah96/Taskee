package com.ihunter.taskee.utils;

import android.content.res.Resources;

/**
 * Created by Master Bison on 1/1/2017.
 */

public class ScreenUtils {

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

}
