package com.devlogex.yue.android.repositories;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public interface FSCallback {

    void onGetDocSuccess(Map<String, Object> data);

    void onGetDocFailure(Exception e);

    void onEventListenColSuccess(QuerySnapshot snapshot);

    void onEventListenColFailure(FirebaseFirestoreException e);

}
