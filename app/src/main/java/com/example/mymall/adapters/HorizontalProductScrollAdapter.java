package com.example.mymall.adapters;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;
import com.example.mymall.models.HorizontalProductScrollModel;
import com.example.mymall.ui.ProductDetailsActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.mymall.adapters.GridProductLayoutAdapter.PRODUCT_ID;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {

    private List<HorizontalProductScrollModel> productScrollAdapterList;

    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> productScrollAdapterList) {
        this.productScrollAdapterList = productScrollAdapterList;
    }

    @NonNull
    @Override
    public HorizontalProductScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductScrollAdapter.ViewHolder holder, int position) {
        String resource = productScrollAdapterList.get(position).getProductImage();
        String title = productScrollAdapterList.get(position).getProductTitle();
        String description = productScrollAdapterList.get(position).getProductDescription();
        String price = productScrollAdapterList.get(position).getProductPrice();
        String productId = productScrollAdapterList.get(position).getProductId();

        holder.setData(productId , resource , description , price , title);
    }

    @Override
    public int getItemCount() {
        return productScrollAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;
        private TextView productDescription;
        private TextView productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.h_s_item_image);
            productTitle = itemView.findViewById(R.id.h_s_item_title);
            productPrice = itemView.findViewById(R.id.h_s_item_price);
            productDescription = itemView.findViewById(R.id.h_s_item_description);

        }

        private void setData(final String productId , String resource , String title , String description , String price) {
            //productImage.setImageResource(resource);
            Glide.with(itemView.getContext()).load(resource)
                    .apply(new RequestOptions().placeholder(R.drawable.null_icon)).into(productImage);
            productTitle.setText(title);
            productDescription.setText(description);
            productPrice.setText("Rs." + price + "/-");

            if (!title.equals("")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ProductDetailsActivity.class);
                        intent.putExtra(PRODUCT_ID , productId);
                        v.getContext().startActivity(intent);
                    }
                });
            }
        }
    }
}
