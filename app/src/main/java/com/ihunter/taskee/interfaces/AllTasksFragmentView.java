package com.ihunter.taskee.interfaces;

import com.ihunter.taskee.data.Task;

import io.realm.RealmResults;

/**
 * Created by Master Bison on 1/12/2017.
 */

public interface AllTasksFragmentView {
    void onRefreshAllTasks(RealmResults<Task> tasks);
}
