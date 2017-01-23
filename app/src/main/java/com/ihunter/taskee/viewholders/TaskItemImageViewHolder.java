package com.ihunter.taskee.viewholders;

import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ihunter.taskee.Constants;
import com.ihunter.taskee.R;
import com.ihunter.taskee.data.Task;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Master Bison on 1/21/2017.
 */

public class TaskItemImageViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.task_title)
    TextView taskTitle;

    @BindView(R.id.task_note)
    TextView taskNote;

    @BindView(R.id.task_date)
    TextView taskDate;

    @BindView(R.id.task_image)
    AppCompatImageView taskImage;

    public TaskItemImageViewHolder(View v){
        super(v);
        ButterKnife.bind(this, v);
    }

    public void bind(Task task){
        if(!TextUtils.isEmpty(task.getColor())) itemView.setBackgroundColor(Color.parseColor("#" + task.getColor()));
        Glide.with(itemView.getContext()).load(task.getImage()).into(taskImage);
        taskNote.setText(task.getNote());
        taskNote.setVisibility(TextUtils.isEmpty(task.getNote()) ? GONE : VISIBLE);
        taskTitle.setText(task.getTitle());
        taskDate.setText(Constants.getFullDateTime(task.getTimestamp()));
    }
}
