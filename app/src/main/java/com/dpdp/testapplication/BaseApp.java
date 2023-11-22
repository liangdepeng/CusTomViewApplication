package com.dpdp.testapplication;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class BaseApp extends Application {

    @SuppressLint("StaticFieldLeak")
    public static Context globalContext;

    @Override
    public void onCreate() {
        super.onCreate();
        globalContext =  getApplicationContext();
    }
}
