package com.ihunter.taskee.data;

import java.util.Calendar;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Task extends RealmObject{

    @PrimaryKey
    private int id;

    private String title = "";
    private String note = "";
    private String image = "";
    private String color = "";
    private long timestamp = Calendar.getInstance().getTimeInMillis();
    private boolean isCompleted =  false;
    private boolean hasReminder = false;
    private RealmList<SubTask> subTasks;

    @Ignore
    private boolean dateSet = false;

    public Task(){

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDateSet(boolean dateSet) {
        this.dateSet = dateSet;
    }

    public boolean isDateSet() {
        return dateSet;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
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

    public boolean hasReminder(){
        return this.hasReminder;
    }

    public void setHasReminder(boolean hasReminder) {
        this.hasReminder = hasReminder;
    }

}