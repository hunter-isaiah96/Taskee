package com.ihunter.taskee;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Master Bison on 1/3/2017.
 */

public class Constants {

    public enum ValidationError{TITLE_ERR, DATE_ERR, FUTURE_ERR, SAVE_ERR}
    private static final SimpleDateFormat fullDateTime = new SimpleDateFormat("MMM d, yyyy - h:mma", Locale.getDefault());
    private static final SimpleDateFormat shortDate = new SimpleDateFormat("EEE, MMM d - yyyy", Locale.getDefault());
    public static final int PICKFILE_RESULT_CODE = 1;
    public static final int EXTERNAL_STORAGE_RESULT_CODE = 1;

    public static String getFullDateTime(long time){
        return fullDateTime.format(time);
    }

    public static String getShortDate(long time){
        return shortDate.format(time);
    }

}
