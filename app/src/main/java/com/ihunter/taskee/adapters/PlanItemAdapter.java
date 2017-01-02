package com.ihunter.taskee.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ihunter.taskee.R;
import com.ihunter.taskee.TaskeeApplication;
import com.ihunter.taskee.data.Plan;
import com.ihunter.taskee.fragments.CalendarTasksFragment;
import com.ihunter.taskee.viewholders.PlanItemViewHolder;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Master Bison on 11/29/2016.
 */

public class PlanItemAdapter extends RecyclerView.Adapter<PlanItemViewHolder>{

    Context context;
    RealmResults<Plan> plansList;
    CalendarTasksFragment calendarTasksFragment = null;
    Realm realm;

    public PlanItemAdapter(Context context, RealmResults<Plan> plansModelList){
        this.context = context;
        this.plansList = plansModelList;
        realm = Realm.getInstance(TaskeeApplication.getRealmConfiugration());
    }

    @Override
    public void onBindViewHolder(final PlanItemViewHolder holder, int position) {
        holder.bind(plansList.get(position));
    }

    public PlanItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_view, parent, false);
        return new PlanItemViewHolder(v, this);
    }

    @Override
    public int getItemCount() {
        return plansList.size();
    }

    public RealmResults<Plan> getPlansList(){
        return plansList;
    }

    public void replacePlansList(RealmResults<Plan> plansList){
        this.plansList = plansList;
        notifyDataSetChanged();

    }

    public void getplans(RealmResults<Plan> plans){
        plansList = plans;
        notifyDataSetChanged();
    }

    public CalendarTasksFragment getCalendarTasksFragment() {
        return calendarTasksFragment;
    }

    public void setCalendarFragment(CalendarTasksFragment calendarFragment){
        this.calendarTasksFragment = calendarFragment;
    }

    public void refreshCalendarEvents(){
        if(calendarTasksFragment != null){
            calendarTasksFragment.addCalendarEvents();
        }
    }

}
