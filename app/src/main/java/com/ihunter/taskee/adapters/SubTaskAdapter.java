package com.ihunter.taskee.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ihunter.taskee.R;
import com.ihunter.taskee.data.SubTask;
import com.ihunter.taskee.interfaces.interactors.SubTaskItemAdapterInteractor;
import com.ihunter.taskee.interfaces.views.TaskEditorView;
import com.ihunter.taskee.services.RealmService;
import com.ihunter.taskee.viewholders.SubTaskViewHolder;
import io.realm.Realm;
import io.realm.RealmList;

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskViewHolder> implements SubTaskItemAdapterInteractor {

    private RealmList<SubTask> list;
    private RealmService realmService;
    private TaskEditorView taskEditorView;

    public SubTaskAdapter(Activity activity) {
        list = new RealmList<>();
        realmService = new RealmService(activity);
    }

    @Override
    public SubTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_task, parent, false);
        return new SubTaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SubTaskViewHolder holder, int position) {
        holder.setSubTaskItemAdapterInteractor(this);
        holder.bind(list.get(position));
    }

    public void addTask(String string) {
        SubTask subTask = new SubTask();
        subTask.setTask(string);
        list.add(subTask);
        notifyDataSetChanged();
        if (taskEditorView != null) {
            taskEditorView.onSubTaskAdded();
        }
    }

    public RealmList<SubTask> getSubTasks() {
        return list;
    }

    public void setSubTaskList(RealmList<SubTask> subTasks){
        this.list = subTasks;
        notifyDataSetChanged();
    }

    public void setTaskEditorView(TaskEditorView taskEditorView){
        this.taskEditorView = taskEditorView;
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
        Realm realm = Realm.getInstance(realmService.getRealmConfiugration());
        realm.beginTransaction();
        getSubTasks().remove(position);
        realm.commitTransaction();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getSubTasks().size());
    }

    @Override
    public void onSubTaskCompletionChange(int position, boolean checked) {
        Realm realm = Realm.getInstance(realmService.getRealmConfiugration());
        realm.beginTransaction();
        getSubTasks().get(position).setCompleted(checked);
        realm.commitTransaction();
    }
}
