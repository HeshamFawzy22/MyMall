package com.example.mymall.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class ProductsDao {

    public static void getProducts(String productId , OnCompleteListener<DocumentSnapshot> onCompleteListener){
        MyDatabase.getProductsReferences()
                .document(productId)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }
    public static void updateProductRating(String productId , Map<String , Object> productRating , OnCompleteListener onCompleteListener){
        MyDatabase.getProductsReferences()
                .document(productId)
                .update(productRating)
                .addOnCompleteListener(onCompleteListener);
    }
}
