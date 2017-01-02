package com.ihunter.taskee;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ihunter.taskee.activities.TaskEditorPresenter;
import com.ihunter.taskee.activities.TaskEditorView;
import com.ihunter.taskee.adapters.PriorityAdapter;
import com.ihunter.taskee.adapters.SubTaskAdapter;
import com.ihunter.taskee.dialogs.CustomTimeDialog;
import com.ihunter.taskee.ui.CustomEditText;
import com.mikepenz.iconics.view.IconicsImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

public class Test extends AppCompatActivity implements TaskEditorView, TextView.OnEditorActionListener {

    TaskEditorPresenter presenter;
    PriorityAdapter adapter;
    SubTaskAdapter subTaskAdapter;
    private static final int PICKFILE_RESULT_CODE = 1;

    @BindView(R.id.root_view)
    RelativeLayout rootView;

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

    @BindView(R.id.edit_task_description)
    CustomEditText editTaskDescription;

    @BindView(R.id.edit_task_sub_task_list)
    RecyclerView subTaskRecyclerView;

    @BindView(R.id.edit_task_date)
    AppCompatTextView editTaskDate;

    @BindView(R.id.edit_task_pick_date)
    IconicsImageView editTaskPickDate;

    @BindView(R.id.edit_task_priority_tag)
    AppCompatImageView editTaskPriority;

    @BindView(R.id.edit_task_add_sub_task)
    CustomEditText editTaskAddSubTask;

    @BindView(R.id.spinner)
    Spinner spinner;

    @OnClick(R.id.edit_task_pick_date)
    protected void onPickDateClick() {
        CustomTimeDialog timeDialog = new CustomTimeDialog(this, presenter.getPlan().getTimestamp());
        timeDialog.setTaskEditorPresenter(presenter);
        timeDialog.show();
    }

    @OnClick(R.id.edit_task_pick_image)
    protected void onPickImageClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICKFILE_RESULT_CODE);
    }

    @OnClick(R.id.edit_task_remove_image)
    protected void onRemoveImageClick(){
        presenter.setPlanImage("");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_new_task);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        presenter = new TaskEditorPresenter(this);
        adapter = new PriorityAdapter(this);
        spinner.setAdapter(adapter);
        subTaskAdapter = new SubTaskAdapter(getApplicationContext());
        subTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        subTaskRecyclerView.setAdapter(subTaskAdapter);
        editTaskAddSubTask.setOnEditorActionListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            presenter.editPlan(bundle.getLong("item_id"));
            presenter.setTitle("Edit Task");
        }else{
            presenter.setTitle("New Task");
        }
    }

    @OnTextChanged(R.id.edit_task_title)
    protected void onTitleChange(SpannableStringBuilder builder) {
        presenter.getPlan().setTitle(builder.toString());
    }

    @OnTextChanged(R.id.edit_task_description)
    protected void onDescriptionChange(SpannableStringBuilder builder) {
        presenter.getPlan().setDescription(builder.toString());
    }

    @OnItemSelected(R.id.spinner)
    public void onPriorityChange(Spinner spinner, int position) {
        presenter.getPlan().setPriority(position + 1);
        if (position == 0) {
            editTaskPriority.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.low_priority));
        } else if (position == 1) {
            editTaskPriority.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.medium_priority));
        } else if (position == 2) {
            editTaskPriority.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.high_priority));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_task_menu, menu);
        menu.findItem(R.id.save_task).getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.white), PorterDuff.Mode.SRC_IN);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.save_task:
                presenter.savePlan();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
        toolbarTitle.setText(string);
    }

    @Override
    public void onPlanForEditorMode() {
        editTaskTitle.setText(presenter.getPlan().getTitle());
        editTaskDescription.setText(presenter.getPlan().getDescription());
        editTaskDate.setText(presenter.getPlanDate());
        spinner.setSelection(presenter.getPlan().getPriority() - 1);
        editTaskPickDate.setColorRes(R.color.colorAccent);
        subTaskAdapter.setSubTaskList(presenter.getPlan().getSubTasks());
        if(!TextUtils.isEmpty(presenter.getPlan().getImage())){
            Glide.with(getApplicationContext()).load(Uri.parse(presenter.getPlan().getImage())).into(editTaskImage);
            editTaskImageHolder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPlanDateSet(String string) {
        editTaskDate.setText(string);
        editTaskPickDate.setColorRes(R.color.colorAccent);
    }

    @Override
    public void onImageSet(Uri uri) {
        if(!TextUtils.isEmpty(uri.toString())) {
            Glide.with(this).load(uri).into(editTaskImage);
            editTaskImageHolder.setVisibility(View.VISIBLE);
            return;
        }
        editTaskImageHolder.setVisibility(View.GONE);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (!TextUtils.isEmpty(textView.getText().toString())) {
                presenter.addSubTask(textView.getText().toString());
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
                    presenter.setPlanImage(data.getData().toString());
                }
                break;
        }
    }

}
