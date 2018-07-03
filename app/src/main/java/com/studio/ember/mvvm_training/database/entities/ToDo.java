package com.studio.ember.mvvm_training.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "todo_table")
public class ToDo {

    //region FIELDS
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    private int id;

    @NonNull
    private String task;

    @ColumnInfo(name = "task_order")
    private int taskOrder;

    // endregion FEILDS

    public ToDo(String task) {
        this.task = task;
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

}
