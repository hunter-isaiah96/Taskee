package com.ihunter.taskee.viewholders;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ihunter.taskee.R;
import com.ihunter.taskee.data.SubTask;
import com.ihunter.taskee.interfaces.SubTaskItemInterface;
import com.ihunter.taskee.ui.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Master Bison on 12/23/2016.
 */

public class SubTaskViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.sub_task_title)
    CustomTextView subTaskTitle;

    @BindView(R.id.remove_sub_task)
    AppCompatImageView removeSubTask;

    @BindView(R.id.sub_task_completion)
    AppCompatCheckBox planCompletion;

    SubTaskItemInterface subTaskItemInterface;

    public SubTaskViewHolder(View v, SubTaskItemInterface subTaskItemInterface){
        super(v);
        ButterKnife.bind(this, v);
        this.subTaskItemInterface = subTaskItemInterface;
    }

    public void bind(final SubTask subTask, final boolean canEditCompletion, boolean canRemoveItems){
        subTaskTitle.setText(subTask.getTask());
        planCompletion.setChecked(subTask.isCompleted());
        if(!canEditCompletion){
            planCompletion.setEnabled(false);
        }
        if(!canRemoveItems){
            removeSubTask.setVisibility(View.GONE);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canEditCompletion){
                    planCompletion.setChecked(!subTask.isCompleted());
                }
            }
        });
    }

    @OnCheckedChanged(R.id.sub_task_completion)
    protected void subTaskCompletionChange(boolean checked){
        subTaskItemInterface.onSubTaskCompleationChange(getAdapterPosition(), checked);
    }

    @OnClick(R.id.remove_sub_task)
    public void removeSubTask(){
        subTaskItemInterface.onRemoveSubTask(getAdapterPosition());
    }

}
