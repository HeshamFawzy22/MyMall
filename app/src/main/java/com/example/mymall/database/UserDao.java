package com.example.mymall.database;

import com.example.mymall.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDao {
    public static final String USER_DATA = "USER_DATA";
    public static final String MY_WISHLIST_DOCUMENT = "MY_WISHLIST";
    public static final String MY_RATING_DOCUMENT = "MY_RATINGS";
    public static final String MY_CART_DOCUMENT = "MY_CART";
    public static final String MY_ADDRESSES_DOCUMENT = "MY_ADDRESSES";
    public static List<String> myRatedIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();

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
                .collection(USER_DATA)
                .document(MY_WISHLIST_DOCUMENT)
                .set(listSize)
                .addOnCompleteListener(onCompleteListener);
    }

    // load specific document <DocumentSnapshot>
    public static void loadUserWishList(String userId , OnCompleteListener<DocumentSnapshot> onCompleteListener){
        MyDatabase.getUsersReference()
                .document(userId)
                .collection(USER_DATA)
                .document(MY_WISHLIST_DOCUMENT)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public static void updateUserWishList(String userId , Map<String,Object> updateListSize ,OnCompleteListener onCompleteListener){
        MyDatabase.getUsersReference()
                .document(userId)
                .collection(USER_DATA)
                .document(MY_WISHLIST_DOCUMENT)
                .update(updateListSize)
                .addOnCompleteListener(onCompleteListener);
    }
    public static void loadRatingList(String userId ,OnCompleteListener<DocumentSnapshot> onCompleteListener){
        MyDatabase.getUsersReference()
                .document(userId)
                .collection(USER_DATA)
                .document(MY_RATING_DOCUMENT)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }
    public static void updateProductRatingListSize(String userId , Map<String,Object> updateListSize , OnCompleteListener onCompleteListener){
        MyDatabase.getUsersReference()
                .document(userId)
                .collection(USER_DATA)
                .document(MY_RATING_DOCUMENT)
                .update(updateListSize)
                .addOnCompleteListener(onCompleteListener);
    }

    public static void addUserCartList(String userId , Map<String,Object> listSize , OnCompleteListener onCompleteListener){
        MyDatabase.getUsersReference()
                .document(userId)
                .collection(USER_DATA)
                .document(MY_CART_DOCUMENT)
                .set(listSize)
                .addOnCompleteListener(onCompleteListener);
    }

    // load specific document <DocumentSnapshot>
    public static void loadUserCartList(String userId , OnCompleteListener<DocumentSnapshot> onCompleteListener){
        MyDatabase.getUsersReference()
                .document(userId)
                .collection(USER_DATA)
                .document(MY_CART_DOCUMENT)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }
    public static void updateUserCartList(String userId , Map<String,Object> updateListSize ,OnCompleteListener onCompleteListener){
        MyDatabase.getUsersReference()
                .document(userId)
                .collection(USER_DATA)
                .document(MY_CART_DOCUMENT)
                .update(updateListSize)
                .addOnCompleteListener(onCompleteListener);
    }
    public static void loadUserAddresses(String userId , OnCompleteListener<DocumentSnapshot> onCompleteListener){
        MyDatabase.getUsersReference()
                .document(userId)
                .collection(USER_DATA)
                .document(MY_ADDRESSES_DOCUMENT)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }
    public static void updateUserAddresses(String userId , Map<String,Object> updateListSize ,OnCompleteListener onCompleteListener){
        MyDatabase.getUsersReference()
                .document(userId)
                .collection(USER_DATA)
                .document(MY_ADDRESSES_DOCUMENT)
                .update(updateListSize)
                .addOnCompleteListener(onCompleteListener);
    }
}
