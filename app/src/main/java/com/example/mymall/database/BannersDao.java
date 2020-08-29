package com.example.mymall.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

public class BannersDao {
    public static final String HOME_DOCUMENT = "HOME";
    public static final String INDEX = "index";

    public static void getBanners(OnCompleteListener<QuerySnapshot> onCompleteListener) {
        MyDatabase.getCategoriesReferences()
                .document(HOME_DOCUMENT)
                .collection(MyDatabase.TOP_DEALS_BRANCH)
                .orderBy(INDEX)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }
}