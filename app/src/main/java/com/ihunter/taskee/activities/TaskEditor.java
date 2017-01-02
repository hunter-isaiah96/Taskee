package com.ihunter.taskee.activities;

import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.ihunter.taskee.R;
import com.ihunter.taskee.adapters.SubTaskAdapter;
import com.ihunter.taskee.ui.AddSubTaskView;
import com.ihunter.taskee.ui.CustomEditText;
import com.ihunter.taskee.ui.CustomRadioButton;
import com.ihunter.taskee.ui.CustomTextView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.ihunter.taskee.R.id.create_task_medium_priority;

/**
 * Created by Master Bison on 12/5/2016.
 */

public class TaskEditor extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, TaskEditorView {

    SubTaskAdapter subTaskAdapter;
    Menu menu;
    TaskEditorPresenter presenter;

    @BindView(R.id.create_task_root_layout)
    LinearLayout rootView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    CustomTextView toolbarTitle;

    @BindView(R.id.create_task_date_day)
    CustomTextView taskDateDay;

    @BindView(R.id.create_task_date_top)
    CustomTextView taskDateTop;

    @BindView(R.id.create_task_date_bottom)
    CustomTextView taskDateBottom;

    @BindView(R.id.create_task_date_year)
    CustomTextView taskDateYear;

    @BindView(R.id.create_task_time)
    CustomTextView taskTime;

    @BindView(R.id.create_task_time_top)
    CustomTextView taskTimeTop;

    @BindView(R.id.create_task_time_bottom)
    CustomTextView taskTimeBottom;

    @BindView(R.id.create_task_title)
    CustomEditText taskTitle;

    @BindView(R.id.create_task_description)
    CustomEditText taskDescription;

    @BindView(R.id.create_task_low_priority)
    CustomRadioButton lowPriorityButton;

    @BindView(create_task_medium_priority)
    CustomRadioButton mediumPriorityButton;

    @BindView(R.id.create_task_high_priority)
    CustomRadioButton highPriorityButton;

    @BindView(R.id.create_task_priority_group)
    RadioGroup priorityGroup;

    @BindView(R.id.create_task_sub_task_list)
    RecyclerView subTaskRecyclerView;

    @BindView(R.id.add_sub_task_view)
    AddSubTaskView addSubTaskView;

    @OnCheckedChanged(R.id.create_task_low_priority)
    public void selectLowPriority(CustomRadioButton radioButton){
        radioButton.toggleState();
        if(radioButton.isChecked()) {
            toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.low_priority));
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.low_priority));
            }
            presenter.getPlan().setPriority(1);
        }
    }

    @OnCheckedChanged(R.id.create_task_medium_priority)
    public void selectMediumPriority(CustomRadioButton radioButton){
        radioButton.toggleState();
        if(radioButton.isChecked()) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_priority));
            }
            toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.medium_priority));
            presenter.getPlan().setPriority(2);
        }
    }

    @OnCheckedChanged(R.id.create_task_high_priority)
    public void selectHighPriority(CustomRadioButton radioButton){
        radioButton.toggleState();
        if(radioButton.isChecked()) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.high_priority));
            }
            toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.high_priority));
            presenter.getPlan().setPriority(3);
        }
    }

    @OnClick({R.id.create_task_select_date, R.id.create_task_select_time})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.create_task_select_date:
                Calendar date = Calendar.getInstance();
                date.setTimeInMillis(presenter.getPlan().getTimestamp());
                DatePickerDialog.newInstance(TaskEditor.this,
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DAY_OF_MONTH))
                        .show(getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.create_task_select_time:
                Calendar time = Calendar.getInstance();
                time.setTimeInMillis(presenter.getPlan().getTimestamp());
                TimePickerDialog.newInstance(TaskEditor.this,
                        time.get(Calendar.HOUR_OF_DAY),
                        time.get(Calendar.MINUTE), false)
                        .show(getFragmentManager(), "TimePicker");
                break;
        }
    }

    @OnTextChanged(R.id.create_task_title)
    public void setPlanTitle(Editable string) {
        presenter.getPlan().setTitle(string.toString());
    }

    @OnTextChanged(R.id.create_task_description)
    public void setPlanDescription(Editable string) {
        presenter.getPlan().setDescription(string.toString());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        ButterKnife.bind(this);
        presenter = new TaskEditorPresenter(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter.setTitle("New Task");
        subTaskAdapter = new SubTaskAdapter(getApplicationContext());
        subTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        subTaskRecyclerView.setAdapter(subTaskAdapter);
        addSubTaskView.setPresenter(presenter);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            presenter.editPlan(bundle.getLong("item_id"));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_task_menu, menu);
        menu.findItem(R.id.save_task).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.white), PorterDuff.Mode.SRC_IN);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_task) {
            presenter.savePlan();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (!presenter.getPlan().isDateSet()) {
            taskDateDay.setVisibility(View.VISIBLE);
            taskDateYear.setVisibility(View.VISIBLE);
            taskDateTop.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            taskDateBottom.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        }
//        String[] dates = presenter.getPlanDate();
//        taskDateDay.setText(dates[0]);
//        taskDateTop.setText(dates[1]);
//        taskDateBottom.setText(dates[2]);
//        taskDateYear.setText(dates[3]);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
//        if (!presenter.getPlan().isTimeSet()) {
//            taskTime.setVisibility(View.VISIBLE);
//            taskTimeTop.setVisibility(View.GONE);
//            taskTimeBottom.setVisibility(View.GONE);
//        }
//        presenter.setPlanTime(hourOfDay, minute);
//        taskTime.setText(presenter.getPlanTime());
    }

    @Override
    public void onPlanValidationError(String string) {
        Snackbar.make(rootView, string, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onPlanSaveSuccessful() {
        finish();
    }

    @Override
    public void onAddSubTask(String string) {
        subTaskAdapter.addTask(string);
        presenter.getPlan().setSubTasks(subTaskAdapter.getSubTasks());
    }

    @Override
    public void onTitleSet(String string) {
        toolbar.setTitle(string);
    }

    @Override
    public void onPlanForEditorMode() {
        taskTime.setVisibility(View.VISIBLE);
        taskTimeTop.setVisibility(View.GONE);
        taskTimeBottom.setVisibility(View.GONE);
        taskDateDay.setVisibility(View.VISIBLE);
        taskDateYear.setVisibility(View.VISIBLE);
        taskDateTop.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        taskDateBottom.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        taskTitle.setText(presenter.getPlan().getTitle());
        taskDescription.setText(presenter.getPlan().getDescription());
        ((CustomRadioButton)priorityGroup.getChildAt(0)).setChecked(true);
//        taskTime.setText(presenter.getPlanTime());
//        String[] dates = presenter.getPlanDate();
//        taskDateDay.setText(dates[0]);
//        taskDateTop.setText(dates[1]);
//        taskDateBottom.setText(dates[2]);
//        taskDateYear.setText(dates[3]);
        subTaskAdapter.setSubTaskList(presenter.getPlan().getSubTasks());
    }

    @Override
    public void onPlanDateSet(String string) {

    }

    @Override
    public void onImageSet(Uri uri) {

    }
}
