package com.ihunter.taskee.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihunter.taskee.R;
import com.ihunter.taskee.activities.TaskEditorPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Master Bison on 12/23/2016.
 */

public class AddSubTaskView extends LinearLayout implements View.OnFocusChangeListener, TextView.OnEditorActionListener{

    @BindView(R.id.add_sub_task)
    CustomEditText addSubTask;

    @BindView(R.id.add_sub_task_container)
    ViewGroup container;

    @BindView(R.id.add_sub_task_clear)
    ImageButton clearSubTask;

    TaskEditorPresenter createTaskPresenter;

    public AddSubTaskView(Context context) {
        super(context);
        if (!isInEditMode()) {
            initUI(context);
        }
    }

    public AddSubTaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            initUI(context);
        }
    }

    public void initUI(Context context){
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_add_sub_task, this);
        ButterKnife.bind(this, view);
        hideUnderline(addSubTask);
        addSubTask.setOnFocusChangeListener(this);
        addSubTask.setOnEditorActionListener(this);
    }

    @OnClick(R.id.add_sub_task_clear)
    public void closeAddSubTaskView(){
        setEditModeInactive();
    }

    public void hideUnderline(View v){
        v.getBackground().setColorFilter(ContextCompat.getColor(getContext(),android.R.color.transparent), PorterDuff.Mode.MULTIPLY);
    }

    public void showUnderline(View v){
        v.getBackground().clearColorFilter();
    }


    @Override
    public void onFocusChange(View view, boolean isFocused) {
        if(isFocused){
            setEditModeActive();
        }else{
            setEditModeInactive();
        }
    }

    public void setEditModeActive(){
        showUnderline(addSubTask);
        addSubTask.setText("");
        clearSubTask.setVisibility(View.VISIBLE);
    }

    public void setEditModeInactive(){
        hideUnderline(addSubTask);
        addSubTask.setText(getResources().getString(R.string.add_sub_task));
        addSubTask.clearFocus();
        clearSubTask.setVisibility(View.INVISIBLE);
    }

    public void setPresenter(TaskEditorPresenter createTaskPresenter){
        this.createTaskPresenter = createTaskPresenter;
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (!TextUtils.isEmpty(textView.getText().toString())) {
                createTaskPresenter.addSubTask(textView.getText().toString());
                textView.setText("");
                return true;
            }
        }
        return false;
    }
}
