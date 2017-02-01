package com.ihunter.taskee.interfaces.views;

import com.ihunter.taskee.data.Task;

import io.realm.RealmResults;

public interface AllTasksFragmentView {
    void onTasksLoaded(RealmResults<Task> tasks);
}
