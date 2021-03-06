package com.example.mymall.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymall.R;
import com.example.mymall.adapters.WishListAdapter;
import com.example.mymall.models.WishListItemModel;

import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.mymall.ui.ProductDetailsActivity.loadUserWishList;
import static com.example.mymall.ui.ProductDetailsActivity.loadingDialog;
import static com.example.mymall.ui.ProductDetailsActivity.showProgressDialog;
import static com.example.mymall.ui.ProductDetailsActivity.wishList;

public class MyWishListFragment extends Fragment {
    //ui
    private RecyclerView myWishListRecyclerView;
    //Declare
    public static WishListAdapter wishlistAdapter;
    private LinearLayoutManager linearLayoutManager;

    public MyWishListFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_wishlist, container, false);

        initView(view);
        showProgressDialog(getContext());

        setWishListItemModelList();
        return view;
    }

    void initView(View view) {
        myWishListRecyclerView = view.findViewById(R.id.my_wishlist_recycler_view);
    }

    void setWishListItemModelList() {
        /*wishListItemModelList = new ArrayList<>();
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google Pixel XL 2 (Mirror Black,128 GB)", 2 , "3.5" , 25,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google Pixel XL 2 (Mirror Black,128 GB)", 3 , "5" , 23,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google Pixel (Mirror Black,64 GB)", 0 , "2" , 15,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google Pixel XL 2 (Mirror Black,128 GB)", 2 , "4" , 25,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google (Mirror Black,32 GB)", 1 , "3.5" , 10,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google Pixel XL 2 (Mirror Black,128 GB)", 2 , "2.5" , 25,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));*/
        if (ProductDetailsActivity.wishListModelList.size() == 0){
            wishList.clear();
            loadUserWishList(getContext(),true);
        }
        loadingDialog.dismiss();
        setWishListAdapter(ProductDetailsActivity.wishListModelList);
    }

    void setWishListAdapter(List<WishListItemModel> wishListItemModelList) {
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        wishlistAdapter = new WishListAdapter(wishListItemModelList, true);
        myWishListRecyclerView.setLayoutManager(linearLayoutManager);
        myWishListRecyclerView.setAdapter(wishlistAdapter);
    }
}