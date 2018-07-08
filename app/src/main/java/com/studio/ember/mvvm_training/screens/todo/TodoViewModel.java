package com.studio.ember.mvvm_training.screens.todo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.studio.ember.mvvm_training.database.entities.ToDo;
import com.studio.ember.mvvm_training.database.repositories.TodoRepository;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {
    private TodoRepository mRepository;
    private LiveData<List<ToDo>> mToDos;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TodoRepository(application);
        mToDos = mRepository.getmAllToDos();
    }

    /**
     * Get all todoes from the repository
     *
     * @return
     */
    public LiveData<List<ToDo>> getmToDos() {
        return mToDos;
    }

    /**
     * Insert wrapper for repository insert method
     *
     * @param todo the object to be inserted
     */
    public void insert(ToDo todo) {
        mRepository.insert(todo);
    }

    public void update(ToDo todo){
        mRepository.update(todo);
    }

    public void update(List<ToDo> toDos){
        mRepository.update(toDos);
    }

    public void delete(ToDo toDo){
        mRepository.delete(toDo);
    }
}
