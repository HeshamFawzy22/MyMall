package com.example.mymall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mymall.R;

import androidx.fragment.app.Fragment;

public class MyAccountFragment extends Fragment {

    public static final int MANAGED_ADDRESS = 1;

    private Button viewAllAddressBtn;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        viewAllAddressBtn = view.findViewById(R.id.view_all_addresses_btn);

        viewAllAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyAddressesActivity.class);
                intent.putExtra("MODE", MANAGED_ADDRESS);
                startActivity(intent);
            }
        });
        return view;
    }
}