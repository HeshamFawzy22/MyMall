package com.example.mymall.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProductsDao {

    public static void getProducts(String productId , OnCompleteListener<DocumentSnapshot> onCompleteListener){
        MyDatabase.getProductsReferences()
                .document(productId)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }
}
