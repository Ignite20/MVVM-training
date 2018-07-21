package com.studio.ember.mvvm_training.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.GregorianCalendar;

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

    private long creationDate;

    private long editDate;

    private boolean isEdited;

    private boolean deleted;
    // endregion FIELDS

    /**
     * Constructor
     * @param task
     */
    public ToDo(String task) {
        this.task = task;
        this.isEdited = false;
        this.creationDate = new GregorianCalendar().getTimeInMillis();
        this.deleted = false;
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

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getEditDate() {
        return editDate;
    }

    public void setEditDate(long editDate) {
        this.editDate = editDate;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
