package com.example.mymall.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;
import com.example.mymall.models.SliderModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    private List<SliderModel> sliderModelList;

    public SliderAdapter(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.slider_item, container, false);
        ConstraintLayout constraintLayout = view.findViewById(R.id.banner_container);
        constraintLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sliderModelList.get(position).getBackgroundColor())));
        ImageView bannerImage = view.findViewById(R.id.banner_slider_image);
        //bannerImage.setImageResource(sliderModelList.get(position).getBannerImage());
        Glide.with(container.getContext()).load(sliderModelList.get(position).getBannerImage()).apply(new RequestOptions().placeholder(R.drawable.null_icon)).into(bannerImage);
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return sliderModelList == null ? 0 : sliderModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
