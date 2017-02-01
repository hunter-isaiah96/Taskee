package com.ihunter.taskee.interfaces.repositories;

import android.content.Context;

import com.ihunter.taskee.TaskeeApplication;
import com.ihunter.taskee.data.Task;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class TasksRepositoryImpl implements TaskRepository{

    private Realm realm;

    public TasksRepositoryImpl(Context context){
        realm = ((TaskeeApplication)context.getApplicationContext()).getRealm();

    }

    @Override
    public RealmResults<Task> getAllTasks() {
        return realm.where(Task.class).isNotEmpty("title").findAllSorted("id", Sort.DESCENDING);
    }

    @Override
    public RealmResults<Task> getEmptyTasks() {
        return realm.where(Task.class).equalTo("id", -1000).findAll();
    }

    @Override
    public RealmResults<Task> getTasksOnDay(long timeStamp) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTimeInMillis(timeStamp);
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        todayStart.set(Calendar.AM_PM, Calendar.AM);

        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTimeInMillis(timeStamp);
        todayEnd.set(Calendar.HOUR, 11);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        todayEnd.set(Calendar.AM_PM, Calendar.PM);
        return realm.where(Task.class).greaterThanOrEqualTo("timestamp", todayStart.getTimeInMillis()).lessThan("timestamp", todayEnd.getTimeInMillis()).findAllSorted("id", Sort.DESCENDING);
    }

}
