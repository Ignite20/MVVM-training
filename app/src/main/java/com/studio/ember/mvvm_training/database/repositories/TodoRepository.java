package com.studio.ember.mvvm_training.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.studio.ember.mvvm_training.database.TodoDatabase;
import com.studio.ember.mvvm_training.database.dao.TodoDao;
import com.studio.ember.mvvm_training.database.entities.ToDo;

import java.util.List;

public class TodoRepository {
    private TodoDao mTodoDao;
    private LiveData<List<ToDo>> mAllToDos;

    public TodoRepository(Application application) {
        TodoDatabase db = TodoDatabase.getDatabase(application);
        mTodoDao = db.todoDao();
        mAllToDos = mTodoDao.getAllTodos();
    }

    public LiveData<List<ToDo>> getmAllToDos() {
        return mAllToDos;
    }

    public void insert(ToDo todo) {
        new insertAsyncTask(mTodoDao).execute(todo);
    }

    private static class insertAsyncTask extends AsyncTask<ToDo, Void, Void> {

        private TodoDao mAsyncTaskDao;

        insertAsyncTask(TodoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ToDo... toDos) {
            mAsyncTaskDao.insert(toDos[0]);
            return null;
        }
    }
}
