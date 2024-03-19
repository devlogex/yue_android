package com.devlogex.yue.android.controllers;

import android.app.Activity;

import com.google.android.gms.tasks.Task;

public interface Authenticate {

    void login(Activity activity);

    void onSignInResult(Task completedTask);
    void logout();
}
