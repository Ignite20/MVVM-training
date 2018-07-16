package com.studio.ember.mvvm_training.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.studio.ember.mvvm_training.database.TodoDatabase;
import com.studio.ember.mvvm_training.database.dao.TodoDao;
import com.studio.ember.mvvm_training.database.entities.ToDo;
import com.studio.ember.mvvm_training.utils.Utils;

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
        new insertAsyncTask(mTodoDao, false).execute(todo);
    }

    public void update(ToDo todo){
        new insertAsyncTask(mTodoDao, true).execute(todo);
    }

    public void update(List<ToDo> toDos){
        if(toDos != null)
            new insertAsyncList(mTodoDao).execute(toDos);
    }

    public void delete(ToDo toDo){
        new deleteAsyncToDo(mTodoDao).execute(toDo);
    }

    private static class insertAsyncTask extends AsyncTask<ToDo, Void, Void> {

        private TodoDao mAsyncTaskDao;

        private boolean update;
        insertAsyncTask(TodoDao dao, boolean update) {
            this.update = update;
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(ToDo... toDos) {
            if(update){
                mAsyncTaskDao.update(toDos[0]);
            }else {
                mAsyncTaskDao.insert(toDos[0]);
            }
            return null;
        }
    }

    private static class insertAsyncList extends AsyncTask<List<ToDo>, Void, Void> {

        private TodoDao mAsyncTaskDao;


        public insertAsyncList(TodoDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(List<ToDo>... lists) {
            for (int i = 0; i < lists[0].size(); i++) {
                ToDo todo = lists[0].get(i);
                if(todo.getEditDate() > 0){
                    todo.setEdited(true);
                    todo.setEditDate(Utils.getDateMiliseconds());
                }
                todo.setTaskOrder(i);
                mAsyncTaskDao.update(todo);
            }
            return null;
        }
    }

    private static class deleteAsyncToDo extends AsyncTask<ToDo, Void, Void> {

        private TodoDao mAsyncTaskDao;


        public deleteAsyncToDo(TodoDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(ToDo... toDos) {
            mAsyncTaskDao.delete(toDos[0]);
            return null;
        }
    }

}
