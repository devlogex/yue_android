package com.devlogex.yue.android.repositories;


public interface FirebaseRepository {

    void getDocument(String path, FSCallback callback);
    void listenOnCol(String path, FSCallback callback);
}
