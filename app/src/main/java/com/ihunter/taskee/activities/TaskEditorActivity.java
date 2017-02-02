package com.ihunter.taskee.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ihunter.taskee.Constants;
import com.ihunter.taskee.R;
import com.ihunter.taskee.TaskeeApplication;
import com.ihunter.taskee.adapters.SubTaskAdapter;
import com.ihunter.taskee.data.Task;
import com.ihunter.taskee.dialogs.CustomTimeDialog;
import com.ihunter.taskee.interfaces.views.TaskEditorView;
import com.ihunter.taskee.presenters.TaskEditorPresenter;
import com.ihunter.taskee.services.AlarmService;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.realm.Realm;

import static com.ihunter.taskee.Constants.EXTERNAL_STORAGE_RESULT_CODE;
import static com.ihunter.taskee.Constants.PICK_FILE_RESULT_CODE;

public class TaskEditorActivity extends AppCompatActivity implements TaskEditorView, TextView.OnEditorActionListener {

    private IconicsDrawable checked;
    private IconicsDrawable unchecked;
    private TaskEditorPresenter presenter;
    private SubTaskAdapter subTaskAdapter;
    private Task task;
    private Realm realm;

    @BindView(R.id.root_view)
    ScrollView rootView;

    @BindView(R.id.task_view)
    View taskView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbarTitle;

    @BindView(R.id.edit_task_image_holder)
    FrameLayout editTaskImageHolder;

    @BindView(R.id.edit_task_image)
    AppCompatImageView editTaskImage;

    @BindView(R.id.edit_task_title)
    EditText editTaskTitle;

    @BindView(R.id.edit_task_note)
    EditText editTaskNote;

    @BindView(R.id.edit_task_sub_task_list)
    RecyclerView subTaskRecyclerView;

    @BindView(R.id.edit_task_date)
    AppCompatTextView editTaskDate;

    @BindView(R.id.edit_task_pick_date)
    IconicsImageView editTaskPickDate;

    @BindView(R.id.edit_task_pick_image)
    IconicsImageView editTaskPickImage;

    @BindView(R.id.edit_task_add_sub_task)
    EditText editTaskAddSubTask;

    @BindView(R.id.edit_task_pick_color)
    IconicsImageView pickColor;

    // Open up the date & time picker dialog
    @OnClick(R.id.edit_task_pick_date)
    void onPickDateClick() {
        CustomTimeDialog timeDialog = new CustomTimeDialog(this, task.getTimestamp(), "");
        timeDialog.setTaskEditorPresenter(this);
        timeDialog.show();
    }

    // Open the image picker
    @OnClick(R.id.edit_task_pick_image)
    void onPickImageClick() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_RESULT_CODE);
        }else{
            openImagePicker();
        }
    }

    // Open the task color picker
    @OnClick(R.id.edit_task_pick_color)
    void onPickColorClick(){
        new SpectrumDialog.Builder(getApplicationContext())
                .setColors(R.array.task_colors)
                .setSelectedColor(Color.parseColor("#" + task.getColor()))
                .setDismissOnColorSelected(false)
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        if(positiveResult) {
                            taskView.setBackgroundColor(color);
                            task.setColor(Integer.toHexString(color));
                            pickColor.setColor(color);
                        }
                    }
                }).build().show(getSupportFragmentManager(), "color_picker");
    }

    // Remove the task image
    @OnClick(R.id.edit_task_remove_image)
    protected void onRemoveImageClick(){
        editTaskImageHolder.setVisibility(View.GONE);
        editTaskImage.setImageBitmap(null);
        editTaskPickImage.setColorRes(R.color.textColorSecondary);
        task.setImage("");
    }

    // Set the task title when the title text changes
    @OnTextChanged(R.id.edit_task_title)
    void onTitleChange(SpannableStringBuilder builder) {
        task.setTitle(builder.toString());
    }

    // Set the task note when the note text changes
    @OnTextChanged(R.id.edit_task_note)
    void onDescriptionChange(SpannableStringBuilder builder) {
        task.setNote(builder.toString());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        ButterKnife.bind(this);
        setupGlobalVariables();
        setUpToolbar();
        setupSubTasks();
        presenter = new TaskEditorPresenter(this, realm);
        editTaskAddSubTask.setOnEditorActionListener(this);

        /**
         * Check if intent data was sent to the activity.
         * If there was intent data, tell the presenter to find
         * a Task with the passed id(intent_id), and set the global
         * Task(task) as the task in onTaskForEditorMode.
         * Otherwise, the global Task(task) will be a new Task.
        */
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            presenter.editTask(bundle.getInt("item_id"));
            setToolbarTitle(getString(R.string.line_edit_task));
        }else{
            task = new Task();
            int[] androidColors = getResources().getIntArray(R.array.task_colors);
            int selectedColor = androidColors[new Random().nextInt(androidColors.length)];
            setToolbarTitle(getString(R.string.line_new_task));
            taskView.setBackgroundColor(selectedColor);
            task.setColor(Integer.toHexString(selectedColor));
            pickColor.setColor(selectedColor);
        }
    }

    //Initialize global variables
    public void setupGlobalVariables(){
        realm = TaskeeApplication.get(this).getRealm();
        checked = new IconicsDrawable(this).icon(MaterialDesignIconic.Icon.gmi_alarm_check).actionBar().colorRes(R.color.colorAccent);
        unchecked = new IconicsDrawable(this).icon(MaterialDesignIconic.Icon.gmi_alarm).actionBar().colorRes(R.color.textColorSecondary);
    }

    // Set activity toolbar
    public void setUpToolbar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    // Setting up SubTasks RecyclerView & Adapter
    public void setupSubTasks(){
        subTaskAdapter = new SubTaskAdapter(this);
        subTaskAdapter.setTaskEditorView(this);
        subTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        subTaskRecyclerView.setAdapter(subTaskAdapter);
    }

    public void setToolbarTitle(String string){
        toolbarTitle.setText(string);
    }

    /**
     * Start the image picker intent.
     * Android versions below KitKat(19) return a different URI than above or equal to KitKat.
     * Therefore we have to set the action and category accordingly to
     * prevent image loaders from throwing a Permission Denial Exception while loading the URI.
     */
    public void openImagePicker(){
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        startActivityForResult(intent, PICK_FILE_RESULT_CODE);
    }

    // Display or Remove the image accordingly
    public void setImage(){
        if(!TextUtils.isEmpty(task.getImage())) {
            Glide.with(getApplicationContext()).load(task.getImage()).into(editTaskImage);
            editTaskImageHolder.setVisibility(View.VISIBLE);
            editTaskPickImage.setColorRes(R.color.colorAccent);
            return;
        }
        editTaskImageHolder.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            // Checking if we have storage access permission
            case EXTERNAL_STORAGE_RESULT_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openImagePicker();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Called when the user has selected an image from the picker intent
            case PICK_FILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    task.setImage(data.getData().toString());
                    setImage();
                }
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        // Execute if user presses enter on keyboard
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (!TextUtils.isEmpty(textView.getText().toString())) {
                // Add a SubTask
                subTaskAdapter.addTask(textView.getText().toString());
                task.setSubTasks(subTaskAdapter.getSubTasks());
                textView.setText("");
                return true;
            }
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_task_menu, menu);
        MenuItem addAlarm = menu.findItem(R.id.add_alarm);
        MenuItem saveTask = menu.findItem(R.id.save_task);
        saveTask.setIcon(new IconicsDrawable(this).icon(MaterialDesignIconic.Icon.gmi_check).actionBar().color(Color.BLACK));
        addAlarm.setChecked(task.hasReminder());
        addAlarm.setIcon(addAlarm.isChecked() ? checked : unchecked);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_task:
                presenter.saveTask(task);
                break;
            case android.R.id.home:
                finish();
                break;
            case R.id.add_alarm:
                item.setChecked(!item.isChecked());
                task.setHasReminder(item.isChecked());
                item.setIcon(item.isChecked() ? checked : unchecked);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Called when the presenter is checking fields of the Task and doesn't pass the validation
    @Override
    public void onTaskValidationError(Constants.ValidationError validationError) {
        String error = "";
        switch(validationError){
            case TITLE_ERR:
                error = getString(R.string.edit_task_title_error);
                break;
            case DATE_ERR:
                error = getString(R.string.edit_task_date_error);
                break;
            case FUTURE_ERR:
                error = getString(R.string.edit_task_future_error);
                break;
            case SAVE_ERR:
                error = getString(R.string.edit_task_save_error);
                break;
        }
        Snackbar.make(rootView, error, Snackbar.LENGTH_SHORT).show();
    }

    // Called when the task passes the validation and saves
    @Override
    public void onTaskSaveSuccessful(int id) {
        AlarmService alarmService = new AlarmService(getApplicationContext());
        if(task.hasReminder()) {
            alarmService.setAlarm(task.getTimestamp(), id);
        }else{
            alarmService.removeAlarm(id);
        }
        finish();
    }

    // Called if the current activity receives an intent extra of item_id
    @Override
    public void onTaskForEditorMode(Task task) {
        this.task = task;
        task.setDateSet(true);
        editTaskTitle.setText(task.getTitle());
        editTaskNote.setText(task.getNote());
        editTaskDate.setText(Constants.getFullDateTime(task.getTimestamp()));
        editTaskPickDate.setColorRes(R.color.colorAccent);
        subTaskAdapter.setSubTaskList(task.getSubTasks());
        taskView.setBackgroundColor(Color.parseColor("#" + task.getColor()));
        pickColor.setColor(Color.parseColor("#" + task.getColor()));
        setImage();
    }

    // Called when the user selects a date and time from the Dialog.
    @Override
    public void onTaskDateSet(long time) {
        task.setTimestamp(time);
        task.setDateSet(true);
        editTaskDate.setText(Constants.getFullDateTime(task.getTimestamp()));
        editTaskPickDate.setColorRes(R.color.colorAccent);
    }

    //
    @Override
    public void onSubTaskAdded() {
        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
               rootView.fullScroll(View.FOCUS_DOWN);
            }
        }, 100);
    }

    /*
    @Override
    public void onBackPressed() {
        if(TextUtils.isEmpty(task.getTitle()) || !task.isDateSet() || task.getPriority() == 0){
            finish();
            return;
        }
        presenter.savePlan(plan);
    }
    */

}
