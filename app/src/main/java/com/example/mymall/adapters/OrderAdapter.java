package com.example.mymall.adapters;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mymall.R;
import com.example.mymall.models.OrderItemModel;
import com.example.mymall.ui.OrderDetailsActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    List<OrderItemModel> orderItemModelList;

    public OrderAdapter(List<OrderItemModel> orderItemModelList) {
        this.orderItemModelList = orderItemModelList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItemModel order = orderItemModelList.get(position);
        int resource = order.getProductImage();
        String title = order.getProductTitle();
        String deliveredDate = order.getDeliveryStatus();
        int rating = order.getRating();

        holder.setData(resource, title, deliveredDate, rating);
    }


    @Override
    public int getItemCount() {
        return orderItemModelList == null ? 0 : orderItemModelList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        protected ImageView productImage;
        protected LinearLayout ratingStarContainer;
        protected TextView productTitle;
        protected TextView orderDeliveredDate;
        protected ImageView orderIndicator;

        public OrderViewHolder(@NonNull final View view) {
            super(view);
            productImage = (ImageView) view.findViewById(R.id.product_image);
            ratingStarContainer = (LinearLayout) view.findViewById(R.id.rating_star_container);
            productTitle = (TextView) view.findViewById(R.id.product_title);
            orderDeliveredDate = (TextView) view.findViewById(R.id.order_delivered_date);
            orderIndicator = view.findViewById(R.id.order_indicator);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startOrderDetailsActivity(view);
                }
            });
        }

        private void startOrderDetailsActivity(View view) {
            view.getContext().startActivity(new Intent(view.getContext(), OrderDetailsActivity.class));
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setData(int resource, String title, String deliveredDate, int rating) {
            productImage.setImageResource(resource);
            productTitle.setText(title);
            if (deliveredDate.equals("Cancelled")) {
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorPrimary)));
                orderDeliveredDate.setText(deliveredDate);
            } else {
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.successGreen)));
                orderDeliveredDate.setText(deliveredDate);
            }
            /////rating

            setRating(rating);
            //rateProduct();

            /////rating
        }

        private void rateProduct() {
            for (int i = 0; i < ratingStarContainer.getChildCount(); i++) {
                final int starPosition = i;
                ratingStarContainer.getChildAt(starPosition).setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        setRating(starPosition);
                    }
                });
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setRating(int starPosition) {
            for (int i = 0; i < ratingStarContainer.getChildCount(); i++) {
                ImageView star = (ImageView) ratingStarContainer.getChildAt(i);
                star.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                if (i <= starPosition) {
                    star.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
                }
            }
        }


    }

}
