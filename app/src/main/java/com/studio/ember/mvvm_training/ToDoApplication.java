package com.studio.ember.mvvm_training;

import android.app.Application;

public class ToDoApplication extends Application {

    private static ToDoApplication singleton;

    private ToDoApplication(){

    }

    public static ToDoApplication getSingleton() {
        if(singleton == null){
            singleton = new ToDoApplication();
        }
        return singleton;
    }
}
