package com.ihunter.taskee.viewholders;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihunter.taskee.Constants;
import com.ihunter.taskee.R;
import com.ihunter.taskee.activities.ViewTaskActivity;
import com.ihunter.taskee.data.Task;
import com.ihunter.taskee.interfaces.interactors.TaskItemAdapterInteractor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TaskItemViewHolder extends RecyclerView.ViewHolder {

    private Task mTask;

    @BindView(R.id.task_title)
    TextView taskTitle;

    @BindView(R.id.task_note)
    TextView taskNote;

    @BindView(R.id.task_date)
    TextView taskDate;

    @BindView(R.id.set_task_complete)
    AppCompatCheckBox setTaskComplete;
    private TaskItemAdapterInteractor taskItemAdapterInteractor;

    public TaskItemViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }

    public void bind(Task task, TaskItemAdapterInteractor taskItemAdapterInteractor, int tasksSize) {
        Context context = itemView.getContext();
        this.taskItemAdapterInteractor = taskItemAdapterInteractor;
        this.mTask = task;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(getAdapterPosition() == tasksSize - 1){
            params.setMargins((int) context.getResources().getDimension(R.dimen.item_margin),
                    (int) context.getResources().getDimension(R.dimen.item_margin),
                    (int) context.getResources().getDimension(R.dimen.item_margin),
                    (int) context.getResources().getDimension(R.dimen.item_margin));
        }else{
            params.setMargins((int) context.getResources().getDimension(R.dimen.item_margin),
                    (int) context.getResources().getDimension(R.dimen.item_margin),
                    (int) context.getResources().getDimension(R.dimen.item_margin),0);
        }
        itemView.setLayoutParams(params);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked}, // unchecked
                        new int[]{android.R.attr.state_checked} , // checked
                },
                new int[]{
                        Color.parseColor("#" + task.getColor()),
                        Color.parseColor("#" + task.getColor()),
                }
        );
        setTaskComplete.setSupportButtonTintList(colorStateList);
        setTaskComplete.setChecked(task.isCompleted());
        taskTitle.setText(task.getTitle());
        taskDate.setText(Constants.getFullDateTime(task.getTimestamp()));
        taskNote.setText(task.getNote());
        taskNote.setVisibility(TextUtils.isEmpty(task.getNote()) ? GONE : VISIBLE);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemView.getContext(), ViewTaskActivity.class);
                intent.putExtra("item_id", mTask.getId());
                itemView.getContext().startActivity(intent);
            }
        });
    }

    @OnCheckedChanged(R.id.set_task_complete)
    void SetTaskCompletion(boolean isComplete){
        taskItemAdapterInteractor.setTaskCompete(isComplete, getAdapterPosition());
    }
}