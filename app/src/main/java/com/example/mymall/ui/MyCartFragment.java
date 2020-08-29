package com.example.mymall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mymall.R;
import com.example.mymall.adapters.CartAdapter;
import com.example.mymall.models.CartItemModel;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyCartFragment extends Fragment {
    //ui
    private RecyclerView cartItemRecyclerView;
    private Button continueBtn;

    //Declare
    private List<CartItemModel> cartItemModelList;
    private LinearLayoutManager linearLayoutManager;
    private CartAdapter cartAdapter;

    public MyCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);
        initView(view);

        setCartItemModelList();
        continueBtnToAddAddressActivity();

        return view;
    }

    private void continueBtnToAddAddressActivity() {
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddAddressActivity.class));
            }
        });
    }

    private void initView(View view) {
        cartItemRecyclerView = view.findViewById(R.id.cartItemRecyclerView);
        continueBtn = view.findViewById(R.id.cart_continue_btn);
    }

    private void setCartAdapter() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        cartItemRecyclerView.setLayoutManager(linearLayoutManager);
        cartAdapter = new CartAdapter(cartItemModelList);
        cartItemRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
    }

    private void setCartItemModelList() {
        cartItemModelList = new ArrayList<>();
        cartItemModelList.add(new CartItemModel(0, R.drawable.product_image, "Pixel 2", 2, "Rs.59999/-", "Rs.169999/-", 2, 2, 2));
        cartItemModelList.add(new CartItemModel(0, R.drawable.product_image, "Pixel 2", 1, "Rs.59999/-", "Rs.169999/-", 1, 1, 1));
        cartItemModelList.add(new CartItemModel(0, R.drawable.product_image, "Pixel 2", 0, "Rs.59999/-", "Rs.169999/-", 0, 0, 0));
        cartItemModelList.add(new CartItemModel(1, "Price (3 items)", "Rs.169999/-", "Free", "Rs.169999/-", "Rs.5999/-"));

        setCartAdapter();
    }

}