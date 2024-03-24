package com.devlogex.yue.android.controllers;


import static com.devlogex.yue.android.controllers.ShareStorage.saveToken;
import static com.devlogex.yue.android.controllers.ShareStorage.saveUserInfo;
import static com.devlogex.yue.android.utils.RestAPI.aPost;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.devlogex.yue.android.R;
import com.devlogex.yue.android.ui.SharedViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GoogleSSO implements Authenticate {
    public static final int RC_SIGN_IN = 215;

    private GoogleSignInClient googleSignInClient = null;
    private Activity activity;

    private static GoogleSSO instance = null;

    public static GoogleSSO getInstance(Activity activity) {
        if (instance == null) {
            instance = new GoogleSSO(activity);
        }
        return instance;
    }

    private GoogleSSO(Activity activity) {
        this.activity = activity;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.google_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    @Override
    public void login() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onGoogleSignInResult(Task<GoogleSignInAccount> task, SharedViewModel sharedViewModel) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            String code = account.getIdToken();

            JsonObject payload = new JsonObject();
            payload.addProperty("code", code);
            aPost("https://devlogex.com/api/v1/authenticate/", payload.toString(), null, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    //TODO: handle login failed

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            String responseBody = response.body().string();
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            String token = jsonResponse.getString("access_token");
                            String userInfo = jsonResponse.getString("user_info");
                            saveToken(activity, token);
                            saveUserInfo(activity, userInfo);
                            sharedViewModel.setIsLoginInBackGround(true);
                        } else {
                            //TODO: handle login failed
                        }
                    } catch (JSONException e) {
                        //TODO: handle login failed

                    }
                }
            });
        } catch (Throwable e) {
            // TODO: handle login failed
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    @Override
    public void logout() {

    }
}
