package com.example.mymall.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymall.R;
import com.example.mymall.adapters.Addresses_Adapter;
import com.example.mymall.database.UserDao;
import com.example.mymall.models.AddressesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import static com.example.mymall.ui.DeliveryActivity.SELECTED_ADDRESS;
import static com.example.mymall.ui.ProductDetailsActivity.loadingDialog;
import static com.example.mymall.ui.ProductDetailsActivity.showProgressDialog;

public class MyAddressesActivity extends AppCompatActivity implements View.OnClickListener {

    //Declare
    private static Addresses_Adapter addresses_adapter;
    private LinearLayoutManager linearLayoutManager;
    private int previousAddress;
    //ui
    protected Toolbar toolbar;
    protected TextView addNewAddress;
    protected TextView addressSaved;
    protected RecyclerView addressesRecyclerView;
    protected Button deliverHereBtn;



    // refresh selected item changed in recycler view
    public static void refreshItem(int deselected, int selected) {
        addresses_adapter.notifyItemChanged(deselected);
        addresses_adapter.notifyItemChanged(selected);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_addresses);
        initView();

        previousAddress = MyCartFragment.selectedAddress;
        setAddressesModelList();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Addresses");
    }

    @Override
    protected void onStart() {
        super.onStart();
        addressSaved.setText(AddAddressActivity.addressesModelList.size() + " saved addresses");
    }

    private void setAddressesModelList() {
        setAddresses_adapter(AddAddressActivity.addressesModelList);
        setDeliverHereBtnVisibility();
    }

    private int getIntentMode() {
        int mode = getIntent().getIntExtra("MODE", -1);
        return mode;
    }

    private void setDeliverHereBtnVisibility() {
        if (getIntentMode() == SELECTED_ADDRESS) {
            deliverHereBtn.setVisibility(View.VISIBLE);
        } else {
            deliverHereBtn.setVisibility(View.GONE);
        }
    }

    private void setAddresses_adapter(List<AddressesModel> addressesModelList) {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        addressesRecyclerView.setLayoutManager(linearLayoutManager);
        addresses_adapter = new Addresses_Adapter(addressesModelList, getIntentMode());
        addressesRecyclerView.setAdapter(addresses_adapter);
        //Hide Animation when select another address
        ((SimpleItemAnimator) addressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        addresses_adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_new_address){
            Intent addAddressIntent = new Intent(this, AddAddressActivity.class);
            addAddressIntent.putExtra("INTENT" , "null");
            startActivity(addAddressIntent);
        }else if (v.getId() == R.id.deliver_here_btn){
            if (MyCartFragment.selectedAddress != previousAddress){
                showProgressDialog(this);
                updateSelectedAddress();
            }else {
                finish();
            }
        }
    }

    private void updateSelectedAddress() {
        final int previousAddressIndex = previousAddress;
        Map<String , Object> updateSelectedAddress = new HashMap<>();
        updateSelectedAddress.put("selected_" + previousAddress + 1 , false);
        updateSelectedAddress.put("selected_" + MyCartFragment.selectedAddress + 1 , true);
        UserDao.updateUserAddresses(FirebaseAuth.getInstance().getUid(), updateSelectedAddress , new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    finish();
                }else {
                    previousAddress = previousAddressIndex;
                    Toast.makeText(MyAddressesActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //it's means that: you choosed another address but you did'nt press deliver here btn to save another address
            if (MyCartFragment.selectedAddress != previousAddress){
                AddAddressActivity.addressesModelList.get(MyCartFragment.selectedAddress).setSelected(false);
                AddAddressActivity.addressesModelList.get(previousAddress).setSelected(true);
                MyCartFragment.selectedAddress = previousAddress;
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //it's means that: you choosed another address but you did'nt press deliver here btn to save another address
        if (MyCartFragment.selectedAddress != previousAddress){
            AddAddressActivity.addressesModelList.get(MyCartFragment.selectedAddress).setSelected(false);
            AddAddressActivity.addressesModelList.get(previousAddress).setSelected(true);
            MyCartFragment.selectedAddress = previousAddress;
        }
        super.onBackPressed();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        addNewAddress = (TextView) findViewById(R.id.add_new_address);
        addNewAddress.setOnClickListener(this);
        addressSaved = (TextView) findViewById(R.id.address_saved);
        addressesRecyclerView = (RecyclerView) findViewById(R.id.addresses_recyclerView);
        deliverHereBtn = (Button) findViewById(R.id.deliver_here_btn);
        deliverHereBtn.setOnClickListener(this);
    }


}