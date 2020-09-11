package com.example.mymall.adapters;

import com.example.mymall.models.ProductSpecificationModel;
import com.example.mymall.ui.ProductDescriptionFragment;
import com.example.mymall.ui.ProductSpecificationFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProductDetailsViewPagerAdapter extends FragmentPagerAdapter {

    private int totalTabs;
    private String productDescription;
    private String productOtherDetails;
    private List<ProductSpecificationModel> specificationModelList;

    public ProductDetailsViewPagerAdapter(@NonNull FragmentManager fm, int totalTabs, String productDescription, String productOtherDetails, List<ProductSpecificationModel> specificationModelList) {
        super(fm);
        this.totalTabs = totalTabs;
        this.productDescription = productDescription;
        this.productOtherDetails = productOtherDetails;
        this.specificationModelList = specificationModelList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new ProductDescriptionFragment();
                ((ProductDescriptionFragment) fragment).body = productDescription;
                break;
            case 1:
                fragment = new ProductSpecificationFragment();
                ((ProductSpecificationFragment) fragment).specificationModelList = specificationModelList;
                break;
            case 2:
                fragment = new ProductDescriptionFragment();
                ((ProductDescriptionFragment) fragment).body = productOtherDetails;
                break;

            default:
                fragment = null;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String fragment;
        switch (position) {
            case 0:
                fragment = "Description";
                break;
            case 1:
                fragment = "Specification";
                break;
            case 2:
                fragment = "Details";
                break;
            default:
                fragment = null;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
