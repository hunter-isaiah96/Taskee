package com.ihunter.taskee.viewholders;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ihunter.taskee.R;
import com.ihunter.taskee.adapters.SubTaskAdapter;
import com.ihunter.taskee.ui.CustomTextView;
import com.ihunter.taskee.data.SubTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Master Bison on 12/23/2016.
 */

public class SubTaskViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.sub_task_title)
    CustomTextView subTaskTitle;

    @BindView(R.id.remove_sub_task)
    AppCompatImageView removeSubTask;

    SubTaskAdapter adapter;

    public SubTaskViewHolder(View v, SubTaskAdapter adapter){
        super(v);
        this.adapter = adapter;
        ButterKnife.bind(this, v);
    }

    public void bind(SubTask subTask){
        subTaskTitle.setText(subTask.getTask());
    }

    @OnClick(R.id.remove_sub_task)
    public void removeSubTask(){
        adapter.getSubTasks().remove(getAdapterPosition());
        adapter.notifyItemRemoved(getAdapterPosition());
        adapter.notifyItemRangeChanged(getAdapterPosition(), adapter.getSubTasks().size());
    }

}
