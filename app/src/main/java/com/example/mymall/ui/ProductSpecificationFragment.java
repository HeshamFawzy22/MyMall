package com.example.mymall.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymall.R;
import com.example.mymall.adapters.ProductSpecificationAdapter;
import com.example.mymall.models.ProductSpecificationModel;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductSpecificationFragment extends Fragment {

    //ui
    protected RecyclerView productSpecificationRecyclerView;

    //Declare
    private ProductSpecificationAdapter productSpecificationAdapter;
    private LinearLayoutManager linearLayoutManager;
    public List<ProductSpecificationModel> specificationModelList;

    public ProductSpecificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_specification, container, false);
        initView(view);
        setProductModel();
        return view;
    }

    private void initView(View rootView) {
        productSpecificationRecyclerView = rootView.findViewById(R.id.product_specification_recycler_view);
    }

    private void setProductSpecificationAdapter() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        productSpecificationRecyclerView.setLayoutManager(linearLayoutManager);

        productSpecificationAdapter = new ProductSpecificationAdapter(specificationModelList);
        productSpecificationAdapter.notifyDataSetChanged();
        productSpecificationRecyclerView.setAdapter(productSpecificationAdapter);
    }

    private void setProductModel() {
        /*specificationModelList = new ArrayList<>();
        specificationModelList.add(new ProductSpecificationModel(0, "General"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(0, "Display"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(0, "General"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(0, "Display"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));
        specificationModelList.add(new ProductSpecificationModel(1, "Ram", "4GB"));*/

        setProductSpecificationAdapter();
    }
}