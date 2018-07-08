package com.studio.ember.mvvm_training.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "todo_table")
public class ToDo extends BaseModel{

    //region FIELDS
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    private int id;

    private String task;

    @ColumnInfo(name = "task_order")
    private int taskOrder;

    private boolean done;
    // endregion FIELDS

    public ToDo(String task) {
        this.task = task;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(@NonNull String task) {
        this.task = task;
    }

    public int getTaskOrder() {
        return taskOrder;
    }

    public void setTaskOrder(int taskOrder) {
        this.taskOrder = taskOrder;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
