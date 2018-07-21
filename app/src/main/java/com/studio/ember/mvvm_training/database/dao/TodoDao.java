package com.studio.ember.mvvm_training.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.studio.ember.mvvm_training.database.entities.ToDo;

import java.util.List;

@Dao
public interface TodoDao {

    /**
     * Method to insert todo object into database
     *
     * @param todo object to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ToDo todo);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ToDo todo);


    @Query("SELECT * FROM todo_table WHERE deleted = 0 ORDER BY task_order ASC")
    LiveData<List<ToDo>> getAllTodos();

    @Query("DELETE FROM todo_table")
    void deleteAll();

    @Delete
    void delete(ToDo toDo);

    @Delete
    void deleteToDos(ToDo... toDos);
}
