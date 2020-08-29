package com.example.mymall.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymall.R;
import com.example.mymall.adapters.OrderAdapter;
import com.example.mymall.models.OrderItemModel;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyOrdersFragment extends Fragment {
    //ui
    protected RecyclerView myOrderRecyclerView;

    //Declare
    private OrderAdapter orderAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<OrderItemModel> orderItemModelList;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        initView(view);
        setOrderModel();

        return view;
    }

    private void setOrderModel() {
        orderItemModelList = new ArrayList<>();
        orderItemModelList.add(new OrderItemModel(R.drawable.product_image, "Google Pixel XL 2 (Mirror \nBlack,128 GB)", "Delivered on Mon,15th Jan  \n2015", 4));
        orderItemModelList.add(new OrderItemModel(R.drawable.product_image, "Google Pixel XL 2 (Mirror \nBlack,64 GB)", "Delivered on Mon,15th Jan  \n2016", 1));
        orderItemModelList.add(new OrderItemModel(R.drawable.product_image, "Google Pixel XL 2 (Mirror \nBlack,128 GB)", "Cancelled", 0));
        orderItemModelList.add(new OrderItemModel(R.drawable.product_image, "Google Pixel XL 2 (Mirror \nBlack,32 GB)", "Delivered on Mon,15th Jan  \n2019", 3));
        orderItemModelList.add(new OrderItemModel(R.drawable.product_image, "Google Pixel XL 2 (Mirror \nBlack,128 GB)", "Delivered on Mon,15th Jan  \n2020", 5));
        setOrderAdapter(orderItemModelList);
    }

    private void initView(View rootView) {
        myOrderRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_order_recycler_view);
    }

    private void setOrderAdapter(List<OrderItemModel> orderItemModelList) {

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        myOrderRecyclerView.setLayoutManager(linearLayoutManager);
        orderAdapter = new OrderAdapter(orderItemModelList);
        myOrderRecyclerView.setAdapter(orderAdapter);
    }
}