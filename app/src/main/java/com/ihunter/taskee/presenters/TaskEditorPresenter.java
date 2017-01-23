package com.ihunter.taskee.presenters;

import com.ihunter.taskee.Constants;
import com.ihunter.taskee.TaskeeApplication;
import com.ihunter.taskee.data.Task;
import com.ihunter.taskee.interfaces.TaskEditorView;

import java.util.Calendar;

import io.realm.Realm;

/**
 * Created by Master Bison on 12/26/2016.
 */

public class TaskEditorPresenter {

    private boolean inEditMode = false;
    private TaskEditorView createTaskView;
    private Realm realm;

    public TaskEditorPresenter(TaskEditorView createTaskView){
        this.createTaskView = createTaskView;
        realm = Realm.getInstance(TaskeeApplication.getRealmConfiugration());
    }

    public void saveTask(final Task task){
        if(task.getTitle().length() == 0){
            createTaskView.onTaskValidationError(Constants.ValidationError.TITLE_ERR);
            return;
        }else if(!task.isDateSet()){
            createTaskView.onTaskValidationError(Constants.ValidationError.DATE_ERR);
            return;
        }else if(!isAfterCurrentTime(task.getTimestamp())){
            createTaskView.onTaskValidationError(Constants.ValidationError.FUTURE_ERR);
            return;
        }

        if(!inEditMode) {
            int num = realm.where(Task.class).max("id") == null ? 0 : (realm.where(Task.class).max("id").intValue()) + 1;
            task.setId(num);
        }

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(task);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                createTaskView.onTaskSaveSuccessful(task.getId());
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                createTaskView.onTaskValidationError(Constants.ValidationError.SAVE_ERR);
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

    public void editTask(int id){
        Task plan = realm.where(Task.class).equalTo("id", id).findFirst();
        this.inEditMode = true;
        createTaskView.onTaskForEditorMode(realm.copyFromRealm(plan));
    }

}
