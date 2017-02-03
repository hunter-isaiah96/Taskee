package com.ihunter.taskee.presenters;

import com.ihunter.taskee.Constants;
import com.ihunter.taskee.data.Task;
import com.ihunter.taskee.interfaces.views.TaskEditorView;

import java.util.Calendar;

import io.realm.Realm;

public class QuickAddPresenter {

    private TaskEditorView taskEditorView;

    public QuickAddPresenter(TaskEditorView taskEditorView) {
        this.taskEditorView = taskEditorView;
    }

    public void saveTask(Realm realm, Task task) {
        if(task.getTitle().trim().isEmpty()){
            taskEditorView.onTaskValidationError(Constants.ValidationError.TITLE_ERR);
            return;
        }else if(task.hasReminder() &&  !isAfterCurrentTime(task.getTimestamp())){
            taskEditorView.onTaskValidationError(Constants.ValidationError.FUTURE_ERR);
            return;
        }
        int num = realm.where(Task.class).max("id") == null ? 0 : (realm.where(Task.class).max("id").intValue()) + 1;
        task.setId(num);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(task);
        realm.commitTransaction();
        taskEditorView.onTaskSaveSuccessful(task.getId());
    }

    private boolean isAfterCurrentTime(long timestamp){
       return timestamp > Calendar.getInstance().getTimeInMillis();
    }

}
