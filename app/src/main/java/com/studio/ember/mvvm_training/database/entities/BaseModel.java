package com.studio.ember.mvvm_training.database.entities;

import com.google.gson.GsonBuilder;

public class BaseModel {
    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
