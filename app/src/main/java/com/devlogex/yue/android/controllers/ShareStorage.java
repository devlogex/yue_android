package com.devlogex.yue.android.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.devlogex.yue.android.serializers.UserSerializer;

public class ShareStorage {
    private static String AUTH_PREFS = "auth";
    private static String ACCESS_TOKEN_KEY = "access_token";
    private static String USER_INFO_KEY = "user_info";

    public static void saveToken(Activity activity, String access_token) {
        SharedPreferences preferences = activity.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ACCESS_TOKEN_KEY, access_token);
        editor.apply();
    }

    public static String getToken(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        return preferences.getString(ACCESS_TOKEN_KEY, null);
    }

    public static void clearToken(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static boolean hasToken(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        return preferences.contains(ACCESS_TOKEN_KEY);
    }

    public static void saveUserInfo(Activity activity, String user_info) {
        SharedPreferences preferences = activity.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_INFO_KEY, user_info);
        editor.apply();
    }

    public static UserSerializer getUserInfo(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        String json = preferences.getString(USER_INFO_KEY, null);
        if (json == null) {
            return null;
        }
        return UserSerializer.fromJson(json);
    }

    public static void clearUserInfo(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(USER_INFO_KEY);
        editor.apply();
    }

}
