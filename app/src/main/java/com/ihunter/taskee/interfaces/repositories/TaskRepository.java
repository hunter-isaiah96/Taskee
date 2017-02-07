package com.ihunter.taskee.interfaces.repositories;

import com.ihunter.taskee.data.Task;

import io.realm.RealmResults;

/**
 * Created by Master Bison on 2/1/2017.
 */

public interface TaskRepository {

    RealmResults<Task> getAllTasks();
    RealmResults<Task> getEmptyTasks();
    RealmResults<Task> getTasksOnDay(long timeStamp);
    Task getOneTask(int id);

}
