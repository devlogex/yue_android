package com.devlogex.yue.android.configs;

import android.util.Log;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultUEH;

    public GlobalExceptionHandler() {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.e("GlobalExceptionHandler", "Uncaught exception is: ", e);
        defaultUEH.uncaughtException(t, e);
    }
}
