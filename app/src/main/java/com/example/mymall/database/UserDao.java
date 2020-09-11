package com.example.mymall.database;

import com.example.mymall.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class UserDao {
    public static final String TOP_DEALS = "TOP_DEALS";
    public static final String MY_WISHLIST = "MY_WISHLIST";

    public static void addUser(User user, OnSuccessListener onSuccessListener,
                               OnFailureListener onFailureListener) {
        MyDatabase.getUsersReference()
                .document(user.getId())
                .set(user)
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

    public static void addUserWishList(String userId , Map<String,Object> listSize , OnCompleteListener onCompleteListener){
        MyDatabase.getUsersReference()
                .document(userId)
                .collection(TOP_DEALS)
                .document(MY_WISHLIST)
                .set(listSize)
                .addOnCompleteListener(onCompleteListener);
    }

    // load specific document <DocumentSnapshot>
    public static void loadUserWishList(String userId , OnCompleteListener<DocumentSnapshot> onCompleteListener){
        MyDatabase.getUsersReference()
                .document(userId)
                .collection(TOP_DEALS)
                .document(MY_WISHLIST)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public static void updateUserWishList(String userId , Map<String,Object> updateListSize ,OnCompleteListener onCompleteListener){
        MyDatabase.getUsersReference()
                .document(userId)
                .collection(TOP_DEALS)
                .document(MY_WISHLIST)
                .update(updateListSize)
                .addOnCompleteListener(onCompleteListener);
    }
}
