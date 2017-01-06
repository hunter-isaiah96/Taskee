package com.ihunter.taskee.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ihunter.taskee.R;
import com.ihunter.taskee.data.Task;
import com.ihunter.taskee.interfaces.CalendarTasksFragmentInterface;
import com.ihunter.taskee.interfaces.PlanItemInterface;
import com.ihunter.taskee.services.RealmService;
import com.ihunter.taskee.viewholders.PlanItemViewHolder;

import io.realm.RealmResults;

/**
 * Created by Master Bison on 11/29/2016.
 */

public class PlanItemAdapter extends RecyclerView.Adapter<PlanItemViewHolder> implements PlanItemInterface {

    Context context;
    RealmResults<Task> plansList;
    RealmService realm;
    CalendarTasksFragmentInterface calendarInterface = null;


    public PlanItemAdapter(Context context){
        this.context = context;
        this.realm = new RealmService();
        plansList = realm.getEmptyResults();
    }

    @Override
    public void onBindViewHolder(final PlanItemViewHolder holder, int position) {
        holder.bind(plansList.get(position), getItemCount());
    }

    public PlanItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_view, parent, false);
        return new PlanItemViewHolder(v, this);
    }

    @Override
    public int getItemCount() {
        return plansList.size();
    }

    public RealmResults<Task> getPlansList(){
        return plansList;
    }

    public void replacePlansList(RealmResults<Task> plansList){
        this.plansList = plansList;
        notifyDataSetChanged();
    }

    public void setCalendarTaskFragmentInterface(CalendarTasksFragmentInterface calendarInterface){
        this.calendarInterface = calendarInterface;
    }

    @Override
    public void onItemDelete(int position) {
        getPlansList().deleteFromRealm(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getPlansList().size());
        if(calendarInterface != null){
            calendarInterface.onRefreshEvents();
        }
    }
}
