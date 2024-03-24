package com.devlogex.yue.android.repositories;


import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.PersistentCacheSettings;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreRepository {

    private FirebaseFirestore db;
    private static FirestoreRepository instance = new FirestoreRepository();

    public static FirestoreRepository getInstance() {
        return instance;
    }

    public static void releaseInstance() {
        if (instance != null) {
            instance.destroy();
            instance = null;
        }
    }


    private FirestoreRepository() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings =
                new FirebaseFirestoreSettings.Builder(db.getFirestoreSettings())
                        // Use persistent disk cache (default)
                        .setLocalCacheSettings(PersistentCacheSettings.newBuilder()
                                .build())
                        .build();
        db.setFirestoreSettings(settings);
    }

    public void getDocument(String path, FSCallback callback) {
        String[] pathParts = path.split("/");

        DocumentReference docRef = db.collection(pathParts[0]).document(pathParts[1]);

        for (int i = 2; i < pathParts.length; i += 2) {
            if (i + 1 < pathParts.length) {
                docRef = docRef.collection(pathParts[i]).document(pathParts[i + 1]);
            }
        }

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    callback.onGetDocSuccess(document.getData());
                }
            } else {
                callback.onGetDocFailure(task.getException());
            }
        });
    }

    public void listenOnCol(String path, FSCallback callback) {
        String[] pathParts = path.split("/");

        CollectionReference colRef = db.collection(pathParts[0]);

        for (int i = 1; i < pathParts.length; i += 2) {
            if (i + 1 < pathParts.length) {
                colRef = colRef.document(pathParts[i]).collection(pathParts[i + 1]);
            }
        }

        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    callback.onEventListenColFailure(error);
                    return;
                }
                callback.onEventListenColSuccess(value);
            }
        });

    }

    private void destroy() {
        if (instance != null) {
            db.terminate();
        }
    }
}
