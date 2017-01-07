package com.ihunter.taskee.services;

import com.ihunter.taskee.data.Task;
import com.ihunter.taskee.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Master Bison on 1/5/2017.
 */

public class RealmService {

    private Realm realm;
    private ArrayList emptyArray;

    public RealmService(){
        realm = Realm.getInstance(getRealmConfiugration());
        emptyArray = new ArrayList();
    }

    public Realm getRealm() {
        return realm;
    }

    public RealmResults<Task> getAllTasks(){
        return realm.where(Task.class).findAllSorted("timestamp", Sort.ASCENDING);
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
        RealmResults<Task> plan = realm.where(Task.class).greaterThanOrEqualTo("timestamp", todayStart.getTimeInMillis()).lessThan("timestamp", todayEnd.getTimeInMillis()).findAllSorted("id", Sort.DESCENDING);
        LogUtils.logSystem(plan.size());
        return plan;
    }

    public RealmResults<Task> getEmptyResults(){
        return realm.where(Task.class).equalTo("id", -1000).findAll();
    }

    private RealmConfiguration getRealmConfiugration(){
        return new RealmConfiguration.Builder()
                .name("taskmanager.realm")
                .schemaVersion(0)
                .build();
    }

}
