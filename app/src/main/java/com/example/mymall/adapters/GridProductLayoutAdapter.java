package com.example.mymall.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;
import com.example.mymall.models.HorizontalProductScrollModel;
import com.example.mymall.ui.ProductDetailsActivity;

import java.util.List;

import androidx.annotation.RequiresApi;

public class GridProductLayoutAdapter extends BaseAdapter {
    public static final String PRODUCT_ID = "PRODUCT_ID";

    List<HorizontalProductScrollModel> gridProductModelList;

    public GridProductLayoutAdapter(List<HorizontalProductScrollModel> gridProductModelList) {
        this.gridProductModelList = gridProductModelList;
    }

    @Override
    public int getCount() {
        return gridProductModelList == null ? 0 : gridProductModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item, null);
            view.setElevation(0);
            view.setBackgroundColor(Color.parseColor("#ffffff"));

            //on item click go to ProductDetailsActivity
            startProductDetailsActivity(view, parent , position);

            ImageView productImage = view.findViewById(R.id.h_s_item_image);
            TextView productTitle = view.findViewById(R.id.h_s_item_title);
            TextView productPrice = view.findViewById(R.id.h_s_item_price);
            TextView productDescription = view.findViewById(R.id.h_s_item_description);

            //productImage.setImageResource(gridProductModelList.get(position).getProductImage());
            Glide.with(parent.getContext()).load(gridProductModelList.get(position).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.null_icon)).into(productImage);
            productTitle.setText(gridProductModelList.get(position).getProductTitle());
            productDescription.setText(gridProductModelList.get(position).getProductDescription());
            productPrice.setText("Rs." + gridProductModelList.get(position).getProductPrice() + "-/");

        } else {
            view = convertView;
        }
        return view;
    }

    private void startProductDetailsActivity(View view, final ViewGroup parent , final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(),ProductDetailsActivity.class);
                intent.putExtra(PRODUCT_ID , gridProductModelList.get(position).getProductId());
                parent.getContext().startActivity(intent);
            }
        });
    }
}
