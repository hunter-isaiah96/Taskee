package com.ihunter.taskee.data;

import java.util.Calendar;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Task extends RealmObject{
    @PrimaryKey
    private long id;
    private String title = "";
    private String note = "";
    private long timestamp = Calendar.getInstance().getTimeInMillis();;
    private int priority = 1;
    private RealmList<SubTask> subTasks;
    private boolean isCompleted =  false;
    private String image = "";

    @Ignore
    boolean dateSet = false;

    public Task(){

    }

    public void setDateSet(boolean dateSet) {
        this.dateSet = dateSet;
    }

    public boolean isDateSet() {
        return dateSet;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setSubTasks(RealmList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public RealmList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}