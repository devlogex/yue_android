package com.devlogex.yue.android.controllers;

import com.devlogex.yue.android.ui.SharedViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public interface Authenticate {

    void login();

    void onGoogleSignInResult(Task<GoogleSignInAccount> account, SharedViewModel sharedViewModel);

    void logout();
}
