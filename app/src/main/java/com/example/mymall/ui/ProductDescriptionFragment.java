package com.example.mymall.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mymall.R;

import androidx.fragment.app.Fragment;

public class ProductDescriptionFragment extends Fragment {

    protected TextView tvProductDescription;
    public String body;

    public ProductDescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_description, container, false);
        initView(view);

        tvProductDescription.setText(body);
        return view;
    }

    private void initView(View rootView) {
        tvProductDescription = rootView.findViewById(R.id.tv_product_description);
    }
}