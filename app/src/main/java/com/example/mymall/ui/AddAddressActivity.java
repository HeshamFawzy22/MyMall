package com.example.mymall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mymall.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddAddressActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected Button saveBtn;
    protected EditText city;
    protected EditText locality;
    protected EditText flateNo;
    protected EditText landmark;
    protected EditText name;
    protected EditText mobileNo;
    protected EditText alternateMobileNo;
    protected EditText pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        initView();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add new Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveBtnToDeliveryActivity();
    }

    private void saveBtnToDeliveryActivity() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddAddressActivity.this, DeliveryActivity.class));
                finish();
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
        city = (EditText) findViewById(R.id.city);
        locality = (EditText) findViewById(R.id.locality);
        flateNo = (EditText) findViewById(R.id.flate_no);
        landmark = (EditText) findViewById(R.id.landmark);
        name = (EditText) findViewById(R.id.name);
        mobileNo = (EditText) findViewById(R.id.mobile_no);
        alternateMobileNo = (EditText) findViewById(R.id.alternate_mobile_no);
        pincode = (EditText) findViewById(R.id.pincode);
    }
}