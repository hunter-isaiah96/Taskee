package com.ihunter.taskee;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Constants {

    // ViewHolder Types
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_GROUP = 1;

    public static final int REVEAL_DURATION = 200;

    public enum ValidationError{TITLE_ERR, DATE_ERR, FUTURE_ERR, SAVE_ERR}

    private static final SimpleDateFormat fullDateTime = new SimpleDateFormat("MMM d, yyyy - h:mma", Locale.getDefault());
    private static final SimpleDateFormat shortDate = new SimpleDateFormat("EEE, MMM d - yyyy", Locale.getDefault());
    private static final SimpleDateFormat monthYear = new SimpleDateFormat("MMM - yyyy", Locale.ENGLISH);

    public static final int PICK_FILE_RESULT_CODE = 1;
    public static final int EXTERNAL_STORAGE_RESULT_CODE = 2;


    public static String getFullDateTime(long time){
        return fullDateTime.format(time);
    }

    public static String getShortDate(long time){
        return shortDate.format(time);
    }

    public static String getMonthDate(long time){
        return monthYear.format(time);
    }

    public static boolean isSameMonthOfYear(Calendar calendar, Calendar calendar2){
        if(calendar.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)){
            return true;
        }
        return false;
    }


}
