package com.example.mymall.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyDatabase {

    public static final String USERS_BRANCH = "USERS";
    public static final String CATEGORIES_BRANCH = "CATEGORIES";
    public static final String PRODUCTS_BRANCH = "PRODUCTS";
    public static final String TOP_DEALS_BRANCH = "TOP_DEALS";
    private static FirebaseFirestore firebaseFirestore;
    private static FirebaseAuth firebaseAuth;


    private static FirebaseFirestore getFirestore() {
        if (firebaseFirestore == null) {
            firebaseFirestore = FirebaseFirestore.getInstance();
        }
        return firebaseFirestore;
    }

    public static FirebaseAuth getFirebaseAuth(){
        if (firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public static CollectionReference getUsersReference() {
        return getFirestore().collection(USERS_BRANCH);
    }

    public static CollectionReference getCategoriesReferences() {
        return getFirestore().collection(CATEGORIES_BRANCH);
    }

    public static CollectionReference getProductsReferences() {
        return getFirestore().collection(PRODUCTS_BRANCH);
    }

}
