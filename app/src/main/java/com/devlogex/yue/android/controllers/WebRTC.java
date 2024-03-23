package com.devlogex.yue.android.controllers;


import android.app.Activity;

import com.devlogex.yue.android.exceptions.PermissionRequireException;

public interface WebRTC {
    void createConnection() throws PermissionRequireException;
    void send(String message);
    void closeConnection();

}
