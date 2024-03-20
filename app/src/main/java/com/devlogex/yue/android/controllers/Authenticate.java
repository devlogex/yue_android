package com.devlogex.yue.android.controllers;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public interface Authenticate {

    void login(Activity activity);

    void onGoogleSignInResult(Activity activity, Task<GoogleSignInAccount> account);

    void logout();
}
