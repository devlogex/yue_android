package com.devlogex.yue.android.repositories;

public interface FSCallback {

    void onGetDocSuccess(Object obj);

    void onGetDocFailure(Exception e);

    void onEventListenColSuccess(Object obj);

    void onEventListenColFailure(Object obj);

}
