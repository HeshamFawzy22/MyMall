package com.example.mymall.adapters;

import com.example.mymall.ui.ProductDescriptionFragment;
import com.example.mymall.ui.ProductSpecificationFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProductDetailsViewPagerAdapter extends FragmentPagerAdapter {

    int totalTabs;

    public ProductDetailsViewPagerAdapter(@NonNull FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new ProductDescriptionFragment();
                break;
            case 1:
                fragment = new ProductSpecificationFragment();
                break;
            case 2:
                fragment = new ProductDescriptionFragment();
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
