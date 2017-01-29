package com.ihunter.taskee.viewholders;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ihunter.taskee.R;
import com.ihunter.taskee.data.SubTask;
import com.ihunter.taskee.interfaces.interactors.SubTaskItemAdapterInteractor;
import com.ihunter.taskee.ui.CustomTextView;
import com.mikepenz.iconics.view.IconicsImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class SubTaskViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.sub_task_title)
    CustomTextView subTaskTitle;

    @BindView(R.id.remove_sub_task)
    IconicsImageView removeSubTask;

    @BindView(R.id.sub_task_completion)
    AppCompatCheckBox planCompletion;

    private SubTaskItemAdapterInteractor subTaskItemAdapterInteractor;

    public SubTaskViewHolder(View v){
        super(v);
        ButterKnife.bind(this, v);
    }

    public void bind(SubTask subTask){
        subTaskTitle.setText(subTask.getTask());
        planCompletion.setChecked(subTask.isCompleted());
    }

    public void setSubTaskItemAdapterInteractor(SubTaskItemAdapterInteractor subTaskItemAdapterInteractor) {
        this.subTaskItemAdapterInteractor = subTaskItemAdapterInteractor;
    }

    @OnCheckedChanged(R.id.sub_task_completion)
    void setSubTaskCompletion(boolean checked){
        subTaskItemAdapterInteractor.onSubTaskCompletionChange(getAdapterPosition(), checked);
    }

}
