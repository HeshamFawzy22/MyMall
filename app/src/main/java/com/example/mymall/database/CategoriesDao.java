package com.example.mymall.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

public class CategoriesDao {

    public static final String CATEGORY_ICON = "icon";
    public static final String CATEGORY_NAME = "categoryName";

    public static void getCategories(OnCompleteListener<QuerySnapshot> onCompleteListener) {
        MyDatabase.getCategoriesReferences()
                .orderBy("index")
                .get()
                .addOnCompleteListener(onCompleteListener);
    }
}
