package com.example.mymall.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ProductDetailsImagesAdapter extends PagerAdapter {

    private List<String> productImagesList;

    public ProductDetailsImagesAdapter(List<String> productImagesList) {
        this.productImagesList = productImagesList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView productImage = new ImageView(container.getContext());
        Glide.with(container.getContext()).load(productImagesList.get(position)).apply(new RequestOptions().placeholder(R.drawable.null_icon)).into(productImage);
        container.addView(productImage, 0);
        return productImage;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return productImagesList == null ? 0 : productImagesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
