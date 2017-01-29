package com.ihunter.taskee.services;

import android.app.Activity;

import com.ihunter.taskee.TaskeeApplication;
import com.ihunter.taskee.data.Task;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmService {

    private Realm realm;

    public RealmService(Activity activity){
        realm = TaskeeApplication.get(activity).getRealm();
    }

    private Task getTaskByID(long id){
        return realm.where(Task.class).equalTo("id", id).findFirst();
    }

    public RealmResults<Task> getAllTasks(){
        return realm.where(Task.class).findAllSorted("id", Sort.DESCENDING);
    }


    public RealmResults<Task> getResultsOnDay(long date){
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTimeInMillis(date);
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        todayStart.set(Calendar.AM_PM, Calendar.AM);

        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTimeInMillis(date);
        todayEnd.set(Calendar.HOUR, 11);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        todayEnd.set(Calendar.AM_PM, Calendar.PM);
        return realm.where(Task.class).greaterThanOrEqualTo("timestamp", todayStart.getTimeInMillis()).lessThan("timestamp", todayEnd.getTimeInMillis()).findAllSorted("id", Sort.DESCENDING);
    }

    public RealmResults<Task> getEmptyResults(){
        return realm.where(Task.class).equalTo("id", -1000).findAll();
    }

    public RealmConfiguration getRealmConfiugration(){
        return new RealmConfiguration.Builder()
                .name("taskmanager.realm")
                .schemaVersion(0)
                .build();
    }

}
