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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
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
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ihunter.taskee.Constants;
import com.ihunter.taskee.R;
import com.ihunter.taskee.adapters.SubTaskAdapter;
import com.ihunter.taskee.data.Task;
import com.ihunter.taskee.dialogs.CustomTimeDialog;
import com.ihunter.taskee.interfaces.TaskEditorView;
import com.ihunter.taskee.presenters.TaskEditorPresenter;
import com.ihunter.taskee.services.AlarmService;
import com.ihunter.taskee.ui.CustomEditText;
import com.mikepenz.iconics.view.IconicsImageView;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.ihunter.taskee.Constants.EXTERNAL_STORAGE_RESULT_CODE;
import static com.ihunter.taskee.Constants.PICK_FILE_RESULT_CODE;

public class TaskEditorActivity extends AppCompatActivity implements TaskEditorView, TextView.OnEditorActionListener {

    private TaskEditorPresenter presenter;
    private SubTaskAdapter subTaskAdapter;
    private Task task;

    @BindView(R.id.root_view)
    View rootView;

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
    CustomEditText editTaskTitle;

    @BindView(R.id.edit_task_note)
    CustomEditText editTaskNote;

    @BindView(R.id.edit_task_sub_task_list)
    RecyclerView subTaskRecyclerView;

    @BindView(R.id.edit_task_date)
    AppCompatTextView editTaskDate;

    @BindView(R.id.edit_task_pick_date)
    IconicsImageView editTaskPickDate;

    @BindView(R.id.edit_task_pick_image)
    IconicsImageView editTaskPickImage;

    @BindView(R.id.edit_task_add_sub_task)
    CustomEditText editTaskAddSubTask;

    @BindView(R.id.edit_task_pick_color)
    IconicsImageView pickColor;

    @OnClick(R.id.edit_task_pick_date)
    protected void onPickDateClick() {
        CustomTimeDialog timeDialog = new CustomTimeDialog(this, task.getTimestamp());
        timeDialog.setTaskEditorPresenter(this);
        timeDialog.show();
    }

    @OnClick(R.id.edit_task_pick_image)
    protected void onPickImageClick() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_RESULT_CODE);
        }else{
            openImagePicker();
        }
    }

    @OnClick(R.id.edit_task_pick_color)
    protected void onPickColorClick(){
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

    @OnClick(R.id.edit_task_remove_image)
    protected void onRemoveImageClick(){
        editTaskImageHolder.setVisibility(View.GONE);
        editTaskImage.setImageBitmap(null);
        editTaskPickImage.setColorRes(R.color.textColorSecondary);
        task.setImage("");
    }

    @OnTextChanged(R.id.edit_task_title)
    protected void onTitleChange(SpannableStringBuilder builder) {
        task.setTitle(builder.toString());
    }

    @OnTextChanged(R.id.edit_task_note)
    protected void onDescriptionChange(SpannableStringBuilder builder) {
        task.setNote(builder.toString());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        ButterKnife.bind(this);
        setUpToolbar();
        presenter = new TaskEditorPresenter(this);
        subTaskAdapter = new SubTaskAdapter();
        subTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        subTaskRecyclerView.setAdapter(subTaskAdapter);
        editTaskAddSubTask.setOnEditorActionListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            presenter.editTask(bundle.getInt("item_id"));
            toolbarTitle.setText(getString(R.string.line_edit_task));
        }else{
            int[] androidColors = getResources().getIntArray(R.array.task_colors);
            int selectedColor = androidColors[new Random().nextInt(androidColors.length)];
            toolbarTitle.setText(getString(R.string.line_new_task));
            task = new Task();
            taskView.setBackgroundColor(selectedColor);
            task.setColor(Integer.toHexString(selectedColor));
            pickColor.setColor(selectedColor);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case EXTERNAL_STORAGE_RESULT_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openImagePicker();
                }
                break;
        }
    }

    public void openImagePicker(){
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT < 19){
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        startActivityForResult(intent, PICK_FILE_RESULT_CODE);
    }

    public void setUpToolbar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void setImage(){
        if(!TextUtils.isEmpty(task.getImage())) {
            Glide.with(getApplicationContext()).load(task.getImage()).error(R.drawable.zzz_eraser).into(editTaskImage);
            editTaskImageHolder.setVisibility(View.VISIBLE);
            editTaskPickImage.setColorRes(R.color.colorAccent);
            return;
        }
        editTaskImageHolder.setVisibility(View.GONE);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_task_menu, menu);

        final MenuItem addAlarm = menu.findItem(R.id.add_alarm);
//        menu.findItem(R.id.save_task).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.white), PorterDuff.Mode.SRC_IN);

        final AppCompatCheckBox cb = (AppCompatCheckBox)addAlarm.getActionView().findViewById(R.id.alarm_checkbox);
        cb.setSupportButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryLight));
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    task.setHasReminder(true);
                    cb.setSupportButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorAccent));
                    return;
                }
                task.setHasReminder(false);
                cb.setSupportButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryLight));
            }
        });
        cb.setChecked(task.hasReminder());
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (!TextUtils.isEmpty(textView.getText().toString())) {
                subTaskAdapter.addTask(textView.getText().toString());
                task.setSubTasks(subTaskAdapter.getSubTasks());
                textView.setText("");
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_FILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    task.setImage(data.getData().toString());
                    setImage();
                }
                break;
        }
    }

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
//        Snackbar.make(findViewById(R.id.root_view), "Hey there!", Snackbar.LENGTH_LONG).show();
    }

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
        setImage();
    }

    @Override
    public void onTaskDateSet(long time) {
        task.setTimestamp(time);
        task.setDateSet(true);
        editTaskDate.setText(Constants.getFullDateTime(task.getTimestamp()));
        editTaskPickDate.setColorRes(R.color.colorAccent);
    }

//    @Override
//    public void onBackPressed() {
//        if(TextUtils.isEmpty(task.getTitle()) || !task.isDateSet() || task.getPriority() == 0){
//            finish();
//            return;
//        }
//        presenter.savePlan(plan);
//    }

}
