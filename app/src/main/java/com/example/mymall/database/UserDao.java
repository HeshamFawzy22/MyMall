package com.example.mymall.database;

import com.example.mymall.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserDao {

    public static void addUser(User user, OnSuccessListener onSuccessListener,
                               OnFailureListener onFailureListener) {
        MyDatabase.getUsersReference()
                .add(user)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public static void getUser(String userId,
                               OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        MyDatabase.getUsersReference()
                .document(userId)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }
}
