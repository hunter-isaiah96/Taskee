package com.ihunter.taskee.activities;

import android.net.Uri;

import com.ihunter.taskee.R;
import com.ihunter.taskee.TaskeeApplication;
import com.ihunter.taskee.data.Plan;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;

/**
 * Created by Master Bison on 12/26/2016.
 */

public class TaskEditorPresenter {

    Plan plan;
    boolean inEditMode = false;

    private TaskEditorView createTaskView;
    private Realm realm;

    public TaskEditorPresenter(TaskEditorView createTaskView){
        this.createTaskView = createTaskView;
        realm = Realm.getInstance(TaskeeApplication.getRealmConfiugration());
        plan = new Plan();
    }

    public void addSubTask(String string){
        createTaskView.onAddSubTask(string);
    }

    public String getPlanDate(){
       return new SimpleDateFormat("MMM dd, yyyy - h:mma").format(plan.getTimestamp());
    }

    public void setPlanDate(long time){
        plan.setTimestamp(time);
        plan.setDateSet(true);
        createTaskView.onPlanDateSet(new SimpleDateFormat("MMM dd, yyyy - h:mma").format(time));
    }

    public void setPlanImage(String s){
        plan.setImage(s);
        createTaskView.onImageSet(Uri.parse(s));
    }

    // Validates The Plan Before Callback
    public void savePlan(){
        if(plan.getTitle().length() == 0 || plan.getDescription().length() == 0){
            createTaskView.onPlanValidationError(TaskeeApplication.getContext().getString(R.string.create_task_title_description_error));
            return;
        }else if(!plan.isDateSet()){
            createTaskView.onPlanValidationError(TaskeeApplication.getContext().getString(R.string.create_task_date_time_error));
            return;
        }else if(!isAfterCurrentTime(plan.getTimestamp())){
            createTaskView.onPlanValidationError(TaskeeApplication.getContext().getString(R.string.create_task_time_error));
            return;
        }
        // Validation was Successful
        if(!inEditMode) {
            int num = realm.where(Plan.class).max("id") == null ? 0 : (realm.where(Plan.class).max("id").intValue()) + 1;
            plan.setId(num);
        }
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(plan);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                createTaskView.onPlanSaveSuccessful();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                createTaskView.onPlanValidationError(error.getMessage());
            }
        });
    }

    // Checks if Date is in the Future
    private boolean isAfterCurrentTime(long timestamp){
        if(timestamp > Calendar.getInstance().getTimeInMillis()){
            return true;
        }else{
            return false;
        }
    }

    public void setTitle(String string){
        createTaskView.onTitleSet(string);
    }

    public Plan getPlan() {
        return plan;
    }

    public void editPlan(long id){
        Plan plan = realm.where(Plan.class).equalTo("id", id).findFirst();
        this.plan = realm.copyFromRealm(plan);
        this.plan.setDateSet(true);
        this.inEditMode = true;
        createTaskView.onPlanForEditorMode();
    }

}
