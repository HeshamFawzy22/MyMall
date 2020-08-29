package com.example.mymall.adapters;

import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

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

        holder.setProductImage(resource);
        holder.setProductDescription(description);
        holder.setProductPrice(price);
        holder.setProductTitle(title);
    }

    @Override
    public int getItemCount() {
        return productScrollAdapterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), ProductDetailsActivity.class));
            }
        };
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

            itemView.setOnClickListener(onClickListener);
        }

        private void setProductImage(String resource) {
            //productImage.setImageResource(resource);
            Glide.with(itemView.getContext()).load(resource)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_home)).into(productImage);
        }

        private void setProductTitle(String title) {
            productTitle.setText(title);
        }

        private void setProductDescription(String description) {
            productDescription.setText(description);
        }

        private void setProductPrice(String price) {
            productPrice.setText("Rs." + price + "/-");
        }
    }
}
