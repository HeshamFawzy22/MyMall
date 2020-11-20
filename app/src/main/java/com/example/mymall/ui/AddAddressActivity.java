package com.example.mymall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mymall.R;
import com.example.mymall.database.UserDao;
import com.example.mymall.models.AddressesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.example.mymall.ui.MyCartFragment.selectedAddress;
import static com.example.mymall.ui.ProductDetailsActivity.loadingDialog;
import static com.example.mymall.ui.ProductDetailsActivity.showProgressDialog;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {

    protected Toolbar toolbar;
    protected Button saveBtn;
    protected EditText city;
    protected EditText locality;
    protected EditText flatNo;
    protected EditText landmark;
    protected EditText name;
    protected EditText mobileNo;
    protected EditText alternateMobileNo;
    protected EditText pincode;
    protected Spinner stateSpinner;

    private String [] stateList ;
    private String selectedState;
    public static List<AddressesModel> addressesModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        initView();

        createSpinner(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add new Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void createSpinner(Context context) {
        stateList = getResources().getStringArray(R.array.us_states);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(context , android.R.layout.simple_spinner_item , stateList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        stateSpinner.setAdapter(spinnerAdapter);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = stateList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        saveBtn = (Button) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(AddAddressActivity.this);
        city = (EditText) findViewById(R.id.city);
        locality = (EditText) findViewById(R.id.locality);
        flatNo = (EditText) findViewById(R.id.flat_no);
        landmark = (EditText) findViewById(R.id.landmark);
        name = (EditText) findViewById(R.id.name);
        mobileNo = (EditText) findViewById(R.id.mobile_no);
        alternateMobileNo = (EditText) findViewById(R.id.alternate_mobile_no);
        pincode = (EditText) findViewById(R.id.pincode);
        stateSpinner = findViewById(R.id.state_spinner);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.save_btn){
                checkEntryData();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkEntryData() {
        if (!TextUtils.isEmpty(city.getText())){
            if (!TextUtils.isEmpty(locality.getText())){
                if (!TextUtils.isEmpty(flatNo.getText())){
                    if (!TextUtils.isEmpty(pincode.getText()) && pincode.getText().length() == 6){
                        if (!TextUtils.isEmpty(name.getText())){
                            if (!TextUtils.isEmpty(mobileNo.getText()) && mobileNo.getText().length() == 11){
                                showProgressDialog(AddAddressActivity.this);
                                updateUserAddress();
                            }else {
                                mobileNo.requestFocus();
                                Toast.makeText(this, "Please provide valid Number", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            name.requestFocus();
                        }
                    }else {
                        pincode.requestFocus();
                        Toast.makeText(this, "Please provide valid pincode", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    flatNo.requestFocus();
                }
            }else {
                locality.requestFocus();
            }
        }else {
            city.requestFocus();
        }
    }

    private void updateUserAddress() {
        final String fullAddress = flatNo.getText().toString() + "- " +locality.getText().toString() + "- " + landmark.getText().toString() + "- " + city.getText().toString() +  "- " + selectedState;
        Map<String , Object> addAddress = new HashMap<>();
        String position =  String.valueOf((long)addressesModelList.size() + 1);
        addAddress.put("list_size" , (long)addressesModelList.size() + 1);
        if (TextUtils.isEmpty(alternateMobileNo.getText())) {
            addAddress.put("mobile_no_" + position, mobileNo.getText().toString());
        }else {
            addAddress.put("mobile_no_" + position, mobileNo.getText().toString() + " - " + alternateMobileNo.getText().toString());
        }
        addAddress.put("fullname_" + position, name.getText().toString());
        addAddress.put("address_" + position , fullAddress);
        addAddress.put("pincode_" + position , pincode.getText().toString());
        addAddress.put("selected_" + position , true);
        if (addressesModelList.size() > 0) {
            addAddress.put("selected_" + (selectedAddress + 1), false);
        }
        UserDao.updateUserAddresses(FirebaseAuth.getInstance().getUid(), addAddress , new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    if (addressesModelList.size() > 0) {
                        addressesModelList.get(selectedAddress).setSelected(false);
                    }
                    if (TextUtils.isEmpty(alternateMobileNo.getText())) {
                        addressesModelList.add(new AddressesModel(name.getText().toString()
                                , mobileNo.getText().toString()
                                , fullAddress
                                , pincode.getText().toString()
                                , true));
                    }else {
                        addressesModelList.add(new AddressesModel(name.getText().toString()
                                , mobileNo.getText().toString() + " or " + alternateMobileNo.getText().toString()
                                , fullAddress
                                , pincode.getText().toString()
                                , true));
                    }
                    //if i don't have an address so i will add one and go to delivery activity
                    if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                        startActivity(new Intent(AddAddressActivity.this, DeliveryActivity.class));
                    }else{
                        //if i have an address and i want to add another then i will back to my addressesActivity after i added a new address to refresh Items
                        MyAddressesActivity.refreshItem(selectedAddress , addressesModelList.size() - 1);
                    }
                    selectedAddress = addressesModelList.size() - 1;
                    finish();
                }else {
                    Toast.makeText(AddAddressActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }
        });
    }
}