package com.ihunter.taskee.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ihunter.taskee.R;
import com.ihunter.taskee.TaskeeApplication;
import com.ihunter.taskee.data.Task;
import com.ihunter.taskee.interfaces.interactors.TaskItemAdapterInteractor;
import com.ihunter.taskee.interfaces.views.CalendarTasksFragmentView;
import com.ihunter.taskee.services.RealmService;
import com.ihunter.taskee.viewholders.TaskItemImageViewHolder;
import com.ihunter.taskee.viewholders.TaskItemViewHolder;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.ihunter.taskee.Constants.TYPE_GROUP;
import static com.ihunter.taskee.Constants.TYPE_IMAGE;

public class TaskItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TaskItemAdapterInteractor {

    private Realm realm;
    private RealmResults<Task> tasksList;
    private CalendarTasksFragmentView calendarView = null;

    public TaskItemAdapter(Activity activity) {
        this.realm = TaskeeApplication.get(activity).getRealm();
        this.tasksList = new RealmService(activity).getEmptyResults();
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.isEmpty(tasksList.get(position).getImage())) {
            return TYPE_GROUP;
        } else {
            return TYPE_IMAGE;
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_GROUP:
                View vGroup = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
                return new TaskItemViewHolder(vGroup);
            case TYPE_IMAGE:
                View vImage = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_image, parent, false);
                return new TaskItemImageViewHolder(vImage);
            default:
                View vGroup0 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
                return new TaskItemViewHolder(vGroup0);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_GROUP:
                TaskItemViewHolder taskItemViewHolder = (TaskItemViewHolder) holder;
                taskItemViewHolder.bind(tasksList.get(position), getItemCount());
                break;
            case TYPE_IMAGE:
                TaskItemImageViewHolder taskItemImageViewHolder = (TaskItemImageViewHolder) holder;
                taskItemImageViewHolder.bind(tasksList.get(position));
                break;
        }
        /*
            boolean shouldShowHeader = false;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(plansList.get(position).getTimestamp());
            Calendar calendar2 = Calendar.getInstance();
            if(position > 0) {
                calendar2.setTimeInMillis(plansList.get(position - 1).getTimestamp());
            }
            if (position == 0 || position > 0 && !Constants.isSameMonthOfYear(calendar, calendar2)){
                shouldShowHeader = true;
            }else if(position > 0 && Constants.isSameMonthOfYear(calendar, calendar2)){
                shouldShowHeader = false;
            }
        */
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    private RealmResults<Task> getPlansList() {
        return tasksList;
    }

    public void replacePlansList(RealmResults<Task> tasksList) {
        this.tasksList = tasksList;
        notifyDataSetChanged();
    }

    public void setCalendarTaskFragmentView(CalendarTasksFragmentView calendarView) {
        this.calendarView = calendarView;
    }

    @Override
    public void onItemDelete(int position) {
        realm.beginTransaction();
        getPlansList().deleteFromRealm(position);
        realm.commitTransaction();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getPlansList().size());
        if (calendarView != null) {
            calendarView.onRefreshEvents();
        }
    }
}
