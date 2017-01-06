package com.ihunter.taskee.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ihunter.taskee.R;
import com.ihunter.taskee.data.SubTask;
import com.ihunter.taskee.interfaces.SubTaskItemInterface;
import com.ihunter.taskee.viewholders.SubTaskViewHolder;

import io.realm.RealmList;

/**
 * Created by Master Bison on 12/5/2016.
 */

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskViewHolder> implements SubTaskItemInterface{

    RealmList<SubTask> list;
    Context context;

    public SubTaskAdapter(Context context) {
        list = new RealmList<>();
        this.context = context;
    }

    @Override
    public SubTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_task, parent, false);
        return new SubTaskViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(final SubTaskViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    public void addTask(String string) {
        SubTask subTask = new SubTask();
        subTask.setTask(string);
        list.add(subTask);
        notifyDataSetChanged();
    }

    public RealmList<SubTask> getSubTasks() {
        return list;
    }

    public void setSubTaskList(RealmList<SubTask> subTasks){
        this.list = subTasks;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onRemoveSubTask(int position) {
        getSubTasks().remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getSubTasks().size());
    }
}
