package com.devlogex.yue.android.controllers.impl;


import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import com.devlogex.yue.android.R;
import com.devlogex.yue.android.controllers.Authenticate;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GoogleSSO implements Authenticate {
    public static final int RC_SIGN_IN = 002;

    private static GoogleSSO instance = null;
    public static GoogleSSO getInstance() {
        if (instance == null) {
            instance = new GoogleSSO();
        }
        return instance;
    }
    private GoogleSSO() {
    }

    @Override
    public void login(Activity activity) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("637017001670-09ogfl5lvj3n8fg1hishbb9bj8hgivss.apps.googleusercontent.com")
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(activity, gso);

        Intent signInIntent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onSignInResult(Task task) {
        try {
            GoogleSignInAccount account = null;
            account = (GoogleSignInAccount) task.getResult(ApiException.class);
            String name = account.getAccount().name;
            System.out.println("Name: " + name);
        } catch (Throwable e) {
        }
    }


    @Override
    public void logout() {

    }
}
