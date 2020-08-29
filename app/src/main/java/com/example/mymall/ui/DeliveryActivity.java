package com.example.mymall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.mymall.R;
import com.example.mymall.adapters.CartAdapter;
import com.example.mymall.models.CartItemModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeliveryActivity extends AppCompatActivity {

    public static final int SELECTED_ADDRESS = 0;

    protected Toolbar toolbar;
    protected RecyclerView deliveryRecyclerView;
    protected Button changeOrAddAddressBtn;
    protected Button cartContinueBtn;

    //Declare
    private List<CartItemModel> cartItemModelList;
    private LinearLayoutManager linearLayoutManager;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_delivery);
        initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setCartItemModelList();

        changeOrAddAddressesBtnToAddressesActivity();
    }

    private void changeOrAddAddressesBtnToAddressesActivity() {
        changeOrAddAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                intent.putExtra("MODE", SELECTED_ADDRESS);
                startActivity(intent);
            }
        });
    }

    private void setCartAdapter() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        deliveryRecyclerView.setLayoutManager(linearLayoutManager);
        cartAdapter = new CartAdapter(cartItemModelList);
        deliveryRecyclerView.setAdapter(cartAdapter);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        deliveryRecyclerView = (RecyclerView) findViewById(R.id.delivery_recyclerView);
        changeOrAddAddressBtn = (Button) findViewById(R.id.change_or_add_address_btn);
        cartContinueBtn = (Button) findViewById(R.id.cart_continue_btn);
        changeOrAddAddressBtn.setVisibility(View.VISIBLE);
    }
}