package com.example.mymall.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;
import com.example.mymall.models.WishListItemModel;
import com.example.mymall.ui.ProductDetailsActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.MyWishlistViewHolder> {


    private List<WishListItemModel> wishListItemModelList;
    private Boolean isWishlist;

    public WishListAdapter(List<WishListItemModel> wishListItemModelList, Boolean isWishlist) {
        this.wishListItemModelList = wishListItemModelList;
        this.isWishlist = isWishlist;
    }

    @NonNull
    @Override
    public MyWishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_wishlist_item, parent, false);
        return new MyWishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWishlistViewHolder holder, int position) {
        WishListItemModel wishlistModel = wishListItemModelList.get(position);
        String resource = wishlistModel.getProductImage();
        String title = wishlistModel.getProductTitle();
        long freeCoupen = wishlistModel.getFreeCoupens();
        String rating = wishlistModel.getRating();
        long totalRatings = wishlistModel.getTotalRating();
        String price = wishlistModel.getProductPrice();
        String cuttedPrice = wishlistModel.getCuttedPrice();
        boolean COD = wishlistModel.isCOD();

        holder.setData(resource, title, freeCoupen, rating, totalRatings, price, cuttedPrice, COD);
    }

    @Override
    public int getItemCount() {
        return wishListItemModelList == null ? 0 : wishListItemModelList.size();
    }


    class MyWishlistViewHolder extends RecyclerView.ViewHolder {
        protected ImageView productImage;
        protected ImageView coupenIcon;
        protected ImageView deleteIcon;
        protected TextView rating;
        protected TextView productTitle;
        protected TextView freeCoupen;
        protected TextView totalRatings;
        protected TextView productPrice;
        protected TextView cuttedPrice;
        protected TextView paymentMethod;

        public MyWishlistViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = (ImageView) itemView.findViewById(R.id.product_image);
            coupenIcon = (ImageView) itemView.findViewById(R.id.coupen_icon);
            deleteIcon = (ImageView) itemView.findViewById(R.id.delete_icon);
            rating = (TextView) itemView.findViewById(R.id.tv_product_rating_mini_view);
            productTitle = (TextView) itemView.findViewById(R.id.product_title);
            freeCoupen = (TextView) itemView.findViewById(R.id.free_coupen);
            totalRatings = (TextView) itemView.findViewById(R.id.total_ratings);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            cuttedPrice = (TextView) itemView.findViewById(R.id.cutted_price);
            paymentMethod = (TextView) itemView.findViewById(R.id.payment_method);
        }

        void setData(String resource, String title, long freeCoupenNo, String averageRate, long totalRatingsValue, String price, String cuttedPriceValue, boolean COD) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_home)).into(productImage);
            productTitle.setText(title);
            if (freeCoupenNo > 0) {
                coupenIcon.setVisibility(View.VISIBLE);
                freeCoupen.setText("free " + freeCoupenNo + " coupen");
            } else {
                freeCoupen.setVisibility(View.GONE);
                coupenIcon.setVisibility(View.GONE);
            }
            rating.setText(averageRate);
            totalRatings.setText("(" + totalRatingsValue + ")" + "ratings");
            productPrice.setText("RS." + price + "/-");
            cuttedPrice.setText("Rs." + cuttedPriceValue + "/-");

            if (COD) {
                paymentMethod.setVisibility(View.VISIBLE);
            } else {
                paymentMethod.setVisibility(View.INVISIBLE);
            }

            if (isWishlist) {
                deleteIcon.setVisibility(View.VISIBLE);
            } else {
                deleteIcon.setVisibility(View.GONE);
            }
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "deleted", Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), ProductDetailsActivity.class));
                }
            });
        }
    }
}
