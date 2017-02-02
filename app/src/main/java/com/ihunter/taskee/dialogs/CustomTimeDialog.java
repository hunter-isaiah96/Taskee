package com.ihunter.taskee.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Window;

import com.ihunter.taskee.interfaces.views.TaskEditorView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by Master Bison on 1/2/2017.
 */

public class CustomTimeDialog extends Dialog implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener {

    private Activity activity;
    private long date;
    private int color;

    private TaskEditorView taskEditorView;
    private Calendar calendar;

    public CustomTimeDialog(Activity activity, long date, String color) {
        super(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.activity = activity;
        this.date = date;
        this.color = Color.parseColor("#" + color);

        calendar = Calendar.getInstance();
        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public CustomTimeDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected CustomTimeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public void setTaskEditorPresenter(TaskEditorView taskEditorView){
        this.taskEditorView = taskEditorView;
    }

    public void init(){
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(date);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setOnCancelListener(this);
        dpd.setAccentColor(color);
        dpd.show(activity.getFragmentManager(),"Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(date);
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setOnCancelListener(this);
        tpd.setAccentColor(color);
        tpd.show(activity.getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        taskEditorView.onTaskDateSet(calendar.getTimeInMillis());
        dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        dismiss();
    }
}
