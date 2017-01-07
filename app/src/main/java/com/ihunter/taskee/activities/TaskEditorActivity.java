package com.ihunter.taskee.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ihunter.taskee.Constants;
import com.ihunter.taskee.R;
import com.ihunter.taskee.adapters.PriorityAdapter;
import com.ihunter.taskee.adapters.SubTaskAdapter;
import com.ihunter.taskee.data.Task;
import com.ihunter.taskee.dialogs.CustomTimeDialog;
import com.ihunter.taskee.interfaces.TaskEditorInterface;
import com.ihunter.taskee.presenters.TaskEditorPresenter;
import com.ihunter.taskee.ui.CustomEditText;
import com.mikepenz.iconics.view.IconicsImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

import static com.ihunter.taskee.Constants.EXTERNAL_STORAGE_RESULT_CODE;
import static com.ihunter.taskee.Constants.PICKFILE_RESULT_CODE;

public class TaskEditorActivity extends AppCompatActivity implements TaskEditorInterface, TextView.OnEditorActionListener {

    TaskEditorPresenter presenter;
    PriorityAdapter adapter;
    SubTaskAdapter subTaskAdapter;
    Task plan;

    @BindView(R.id.root_view)
    ScrollView rootView;

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

    @BindView(R.id.edit_task_priority_tag)
    AppCompatImageView editTaskPriority;

    @BindView(R.id.edit_task_add_sub_task)
    CustomEditText editTaskAddSubTask;

    @BindView(R.id.spinner)
    Spinner spinner;

    @OnClick(R.id.edit_task_pick_date)
    protected void onPickDateClick() {
        CustomTimeDialog timeDialog = new CustomTimeDialog(this, plan.getTimestamp());
        timeDialog.setTaskEditorPresenter(presenter);
        timeDialog.show();
    }

    @OnClick(R.id.edit_task_pick_image)
    protected void onPickImageClick() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_RESULT_CODE);
            return;
        }else{
            openImagePicker();
        }
    }

    @OnClick(R.id.edit_task_remove_image)
    protected void onRemoveImageClick(){
        editTaskImageHolder.setVisibility(View.GONE);
        editTaskImage.setImageBitmap(null);
        editTaskPickImage.setColorRes(R.color.textColorSecondary);
        plan.setImage("");
    }

    @OnTextChanged(R.id.edit_task_title)
    protected void onTitleChange(SpannableStringBuilder builder) {
        plan.setTitle(builder.toString());
    }

    @OnTextChanged(R.id.edit_task_note)
    protected void onDescriptionChange(SpannableStringBuilder builder) {
        plan.setNote(builder.toString());
    }

    @OnItemSelected(R.id.spinner)
    public void onPriorityChange(Spinner spinner, int position) {
        plan.setPriority(position + 1);
        if (position == 0) {
            editTaskPriority.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.low_priority));
        } else if (position == 1) {
            editTaskPriority.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.medium_priority));
        } else if (position == 2) {
            editTaskPriority.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.high_priority));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        ButterKnife.bind(this);
        setUpToolbar();
        presenter = new TaskEditorPresenter(this);
        adapter = new PriorityAdapter(this);
        spinner.setAdapter(adapter);
        subTaskAdapter = new SubTaskAdapter(getApplicationContext(), false, true);
        subTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        subTaskRecyclerView.setAdapter(subTaskAdapter);
        editTaskAddSubTask.setOnEditorActionListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            presenter.editPlan(bundle.getLong("item_id"));
            toolbarTitle.setText(getString(R.string.line_edit_task));
        }else{
            toolbarTitle.setText(getString(R.string.line_new_task));
            plan = new Task();
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
        Intent intent;
        if (Build.VERSION.SDK_INT < 19){
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
        }
    }

    public void setUpToolbar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void setImage(){
        if(!TextUtils.isEmpty(plan.getImage())) {
            Glide.with(getApplicationContext()).load(plan.getImage()).error(R.drawable.zzz_image_area_close).into(editTaskImage);
            editTaskImageHolder.setVisibility(View.VISIBLE);
            editTaskPickImage.setColorRes(R.color.colorAccent);
            return;
        }
        editTaskImageHolder.setVisibility(View.GONE);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_task_menu, menu);
        menu.findItem(R.id.save_task).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.white), PorterDuff.Mode.SRC_IN);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_task:
                presenter.savePlan(plan);
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
                plan.setSubTasks(subTaskAdapter.getSubTasks());
                textView.setText("");
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    plan.setImage(data.getData().toString());
                    setImage();
                }
                break;
        }
    }

    @Override
    public void onPlanValidationError(Constants.ValidationError validationError) {
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

    @Override
    public void onPlanSaveSuccessful() {
        finish();
    }

    @Override
    public void onPlanForEditorMode(Task plan) {
        this.plan = plan;
        plan.setDateSet(true);
        editTaskTitle.setText(plan.getTitle());
        editTaskNote.setText(plan.getNote());
        editTaskDate.setText(Constants.getFullDateTime(plan.getTimestamp()));
        spinner.setSelection(plan.getPriority() - 1);
        editTaskPickDate.setColorRes(R.color.colorAccent);
        subTaskAdapter.setSubTaskList(plan.getSubTasks());
        setImage();
    }

    @Override
    public void onPlanDateSet(long time) {
        plan.setTimestamp(time);
        plan.setDateSet(true);
        editTaskDate.setText(Constants.getFullDateTime(plan.getTimestamp()));
        editTaskPickDate.setColorRes(R.color.colorAccent);
    }

//    @Override
//    public void onBackPressed() {
//        if(TextUtils.isEmpty(plan.getTitle()) || !plan.isDateSet() || plan.getPriority() == 0){
//            finish();
//            return;
//        }
//        presenter.savePlan(plan);
//    }

}
