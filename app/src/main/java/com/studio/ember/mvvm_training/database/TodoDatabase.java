package com.studio.ember.mvvm_training.database;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.studio.ember.mvvm_training.database.dao.TodoDao;
import com.studio.ember.mvvm_training.database.entities.ToDo;


@Database(
        entities = {
            ToDo.class
        },
        version = 6
)
public abstract class TodoDatabase extends RoomDatabase {

    // Singleton instance
    private static TodoDatabase INSTANCE;
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };


    public static TodoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TodoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),TodoDatabase.class,"todo_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // TodoDao abstract method
    public abstract TodoDao todoDao();

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final TodoDao mDao;

        PopulateDbAsync(TodoDatabase db) {
            mDao = db.todoDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }


}
