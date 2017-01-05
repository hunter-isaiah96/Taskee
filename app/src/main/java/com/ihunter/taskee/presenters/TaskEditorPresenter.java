package com.ihunter.taskee.presenters;

import com.ihunter.taskee.Constants;
import com.ihunter.taskee.TaskeeApplication;
import com.ihunter.taskee.data.Plan;
import com.ihunter.taskee.interfaces.TaskEditorInterface;

import java.util.Calendar;

import io.realm.Realm;

/**
 * Created by Master Bison on 12/26/2016.
 */

public class TaskEditorPresenter {

    boolean inEditMode = false;

    private TaskEditorInterface createTaskView;
    private Realm realm;

    public TaskEditorPresenter(TaskEditorInterface createTaskView){
        this.createTaskView = createTaskView;
        realm = Realm.getInstance(TaskeeApplication.getRealmConfiugration());
    }

    public void setPlanDate(long time){
        createTaskView.onPlanDateSet(time);
    }

    public void savePlan(final Plan plan){
        if(plan.getTitle().length() == 0){
            createTaskView.onPlanValidationError(Constants.ValidationError.TITLE_ERR);
            return;
        }else if(!plan.isDateSet()){
            createTaskView.onPlanValidationError(Constants.ValidationError.DATE_ERR);
            return;
        }else if(!isAfterCurrentTime(plan.getTimestamp())){
            createTaskView.onPlanValidationError(Constants.ValidationError.FUTURE_ERR);
            return;
        }

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
                createTaskView.onPlanValidationError(Constants.ValidationError.SAVE_ERR);
            }
        });
    }

    private boolean isAfterCurrentTime(long timestamp){
        if(timestamp > Calendar.getInstance().getTimeInMillis()){
            return true;
        }else{
            return false;
        }
    }

    public void editPlan(long id){
        Plan plan = realm.where(Plan.class).equalTo("id", id).findFirst();
        this.inEditMode = true;
        createTaskView.onPlanForEditorMode(realm.copyFromRealm(plan));
    }

}
