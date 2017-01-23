package com.ihunter.taskee.viewholders;

import android.graphics.Color;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihunter.taskee.Constants;
import com.ihunter.taskee.R;
import com.ihunter.taskee.data.SubTask;
import com.ihunter.taskee.data.Task;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TaskItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.task_title)
    TextView taskTitle;

    @BindView(R.id.task_note)
    TextView taskNote;

    @BindView(R.id.task_date)
    TextView taskDate;

    @BindView(R.id.task_sub_tasks)
    LinearLayout taskSubTasks;

    public TaskItemViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }

    public void bind(Task task) {
        if(!TextUtils.isEmpty(task.getColor())) itemView.setBackgroundColor(Color.parseColor("#" + task.getColor()));
        else itemView.setBackgroundResource(R.color.md_red_500);
        if(task.getSubTasks().size() != 0){
            taskSubTasks.setVisibility(View.VISIBLE);
            taskSubTasks.removeAllViews();
            for(SubTask subTask : task.getSubTasks()){
                final ViewGroup nullParent = null;
                LinearLayout v = (LinearLayout)LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_sub_task, nullParent);
                AppCompatCheckBox subTaskCompletion = (AppCompatCheckBox)v.findViewById(R.id.sub_task_completion);
                TextView subTaskTitle = (TextView)v.findViewById(R.id.sub_task_title);
                AppCompatImageView removeSubTask = (AppCompatImageView)v.findViewById(R.id.remove_sub_task);
                subTaskTitle.setText(subTask.getTask());
                subTaskCompletion.setChecked(subTask.isCompleted());
                subTaskCompletion.setEnabled(false);
                subTaskCompletion.setAlpha(0.4f);
                v.removeView(removeSubTask);
                taskSubTasks.addView(v);
            }
        }else{
            taskSubTasks.setVisibility(View.GONE);
        }
        taskTitle.setText(task.getTitle());
        taskDate.setText(Constants.getFullDateTime(task.getTimestamp()));
        taskNote.setText(task.getNote());
        taskNote.setVisibility(TextUtils.isEmpty(task.getNote()) ? GONE : VISIBLE);
    }
}