package com.ihunter.taskee.data;

import io.realm.RealmObject;

/**
 * Created by Master Bison on 12/5/2016.
 */

public class SubTask extends RealmObject {

    private String task;
    private boolean isCompleted = false;

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTask() {
        return task;
    }

}
