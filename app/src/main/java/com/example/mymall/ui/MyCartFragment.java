package com.example.mymall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymall.R;
import com.example.mymall.adapters.CartAdapter;
import com.example.mymall.database.UserDao;
import com.example.mymall.models.AddressesModel;
import com.example.mymall.models.CartItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.mymall.ui.AddAddressActivity.addressesModelList;
import static com.example.mymall.ui.ProductDetailsActivity.loadingDialog;
import static com.example.mymall.ui.ProductDetailsActivity.showProgressDialog;

public class MyCartFragment extends Fragment implements View.OnClickListener {
    //ui
    private RecyclerView cartItemRecyclerView;
    private Button continueBtn;
    private TextView totalAmount;

    //Declare
    private LinearLayoutManager linearLayoutManager;
    public static CartAdapter cartAdapter;

    public static int selectedAddress = -1;

    public MyCartFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);
        initView(view);
        showProgressDialog(getContext());

        setCartItemModelList();

        return view;
    }


    private void initView(View view) {
        cartItemRecyclerView = view.findViewById(R.id.cartItemRecyclerView);
        continueBtn = view.findViewById(R.id.cart_continue_btn);
        continueBtn.setOnClickListener(this);
        totalAmount = view.findViewById(R.id.total_cart_amount);
    }

    private void setCartAdapter(List<CartItemModel> cartItemModelList) {
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        cartItemRecyclerView.setLayoutManager(linearLayoutManager);
        cartAdapter = new CartAdapter(cartItemModelList , totalAmount , true);
        cartItemRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
    }

    private void setCartItemModelList() {
        if (ProductDetailsActivity.cartItemModelList.size() == 0){
            ProductDetailsActivity.cartList.clear();
            ProductDetailsActivity.loadUserCartList(getContext(),true , new TextView(getContext()) , totalAmount);
        }else {
            if (ProductDetailsActivity.cartItemModelList.get(ProductDetailsActivity.cartItemModelList.size() - 1).getType() == CartItemModel.CART_TOTAL_AMOUNT){
                LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
                parent.setVisibility(View.VISIBLE);
            }
        }
        loadingDialog.dismiss();
        setCartAdapter(ProductDetailsActivity.cartItemModelList);
    }

    public static void loadAddresses(final Context context){
        addressesModelList.clear();
        UserDao.loadUserAddresses(FirebaseAuth.getInstance().getUid(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Intent deliveryIntent;
                    if ((long)task.getResult().get("list_size") == 0){
                        deliveryIntent = new Intent(context,AddAddressActivity.class);
                        deliveryIntent.putExtra("INTENT" , "deliveryIntent");
                    }else {
                        for (long i = 1 ; i < (long)task.getResult().get("list_size") + 1 ; i++){
                            addressesModelList.add(new AddressesModel(task.getResult().get("fullname_" + i).toString() ,
                                    task.getResult().get("mobile_no_" + i).toString(),
                                    task.getResult().get("address_" + i).toString() ,
                                    task.getResult().get("pincode_" + i).toString() ,
                                    (boolean)task.getResult().get("selected_" + i)));
                            if ((boolean)task.getResult().get("selected_" + i)){
                                selectedAddress = Integer.parseInt(String.valueOf(i - 1));
                            }
                        }
                        deliveryIntent = new Intent(context,DeliveryActivity.class);
                    }
                    context.startActivity(deliveryIntent);
                }else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cart_continue_btn){
            DeliveryActivity.cartItemModelList = new ArrayList<>();
            DeliveryActivity.fromCart = true;
            for (int i = 0 ; i < ProductDetailsActivity.cartItemModelList.size() ; i++){
                CartItemModel cartItemModel = ProductDetailsActivity.cartItemModelList.get(i);
                if (cartItemModel.isInStock()){
                    DeliveryActivity.cartItemModelList.add(cartItemModel);
                }
            }
//            DeliveryActivity.cartItemModelList.add(cartItemModelList.size() ,new CartItemModel(CartItemModel.CART_TOTAL_AMOUNT));
            DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.CART_TOTAL_AMOUNT));
            showProgressDialog(getContext());
            if (addressesModelList.size() == 0){
                loadAddresses(getContext());
            }else {
                loadingDialog.dismiss();
                startActivity(new Intent(getContext() , DeliveryActivity.class));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        cartAdapter.notifyDataSetChanged();
//        if (ProductDetailsActivity.cartItemModelList.size() == 0){
//            ProductDetailsActivity.cartList.clear();
//            ProductDetailsActivity.loadUserCartList(getContext(),true , new TextView(getContext()) , totalAmount);
//        }else {
//            if (ProductDetailsActivity.cartItemModelList.get(ProductDetailsActivity.cartItemModelList.size() - 1).getType() == CartItemModel.CART_TOTAL_AMOUNT){
//                LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
//                parent.setVisibility(View.VISIBLE);
//            }
//        }
//        loadingDialog.dismiss();
    }
}