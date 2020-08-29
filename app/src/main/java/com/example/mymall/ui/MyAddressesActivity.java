package com.example.mymall.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mymall.R;
import com.example.mymall.adapters.Addresses_Adapter;
import com.example.mymall.models.AddressesModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import static com.example.mymall.ui.DeliveryActivity.SELECTED_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {

    //Declare
    private static Addresses_Adapter addresses_adapter;
    //ui
    protected Toolbar toolbar;
    protected TextView addNewAddress;
    protected TextView addressSaved;
    protected RecyclerView addressesRecyclerView;
    protected Button deliverHereBtn;
    private LinearLayoutManager linearLayoutManager;
    private List<AddressesModel> addressesModelList;

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

        setAddressesModelList();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Addresses");
    }

    private void setAddressesModelList() {
        addressesModelList = new ArrayList<>();
        addressesModelList.add(new AddressesModel("Hisham", "Egypt", "1657892", true));
        addressesModelList.add(new AddressesModel("Hisham", "Egypt", "1657892", false));
        addressesModelList.add(new AddressesModel("Hisham", "Egypt", "1657892", false));
        addressesModelList.add(new AddressesModel("Hisham", "Egypt", "1657892", false));
        addressesModelList.add(new AddressesModel("Hisham", "Egypt", "1657892", false));
        addressesModelList.add(new AddressesModel("Hisham", "Egypt", "1657892", false));
        addressesModelList.add(new AddressesModel("Hisham", "Egypt", "1657892", false));

        setAddresses_adapter(addressesModelList);
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
        addNewAddress = (TextView) findViewById(R.id.add_new_address);
        addressSaved = (TextView) findViewById(R.id.address_saved);
        addressesRecyclerView = (RecyclerView) findViewById(R.id.addresses_recyclerView);
        deliverHereBtn = (Button) findViewById(R.id.deliver_here_btn);
    }
}