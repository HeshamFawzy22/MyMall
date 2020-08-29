package com.example.mymall.adapters;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymall.R;
import com.example.mymall.models.CartItemModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter {
    private List<CartItemModel> cartItemModelList;

    public CartAdapter(List<CartItemModel> cartItemModelList) {
        this.cartItemModelList = cartItemModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.CART_TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
                return new CartItemViewHolder(cartItemView);
            case CartItemModel.CART_TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout, parent, false);
                return new CartItemViewHolder(cartTotalView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CartItemModel itemModel = cartItemModelList.get(position);
        switch (cartItemModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                int resource = itemModel.getProductImage();
                String title = itemModel.getProductTitle();
                int freeCoupens = itemModel.getFreeCoupens();
                String productPrice = itemModel.getProductPrice();
                String cuttedPrice = itemModel.getCuttedPrice();
                int offersApplied = itemModel.getOffersApplied();

                ((CartItemViewHolder) holder).setCartItemDetails(resource, title, freeCoupens, productPrice, cuttedPrice, offersApplied);
                break;
            case CartItemModel.CART_TOTAL_AMOUNT:
                String totalItems = itemModel.getTotalItems();
                String totalItemsPrice = itemModel.getTotalItemsPrice();
                String deliveryPrice = itemModel.getDeliveryPrice();
                String totalAmount = itemModel.getTotalAmount();
                String savedAmount = itemModel.getSavedAmount();
                if (holder instanceof CartTotalAmountViewHolder) {
                    ((CartTotalAmountViewHolder) holder).setTotalAmountDetails(totalItems, totalItemsPrice, deliveryPrice, totalAmount, savedAmount);
                }
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList == null ? 0 : cartItemModelList.size();
    }

    private void openQuantityDialog(View itemView, final TextView productQuantity) {
        final Dialog quantityDialog = new Dialog(itemView.getContext());
        quantityDialog.setContentView(R.layout.quantity_dialog);
        quantityDialog.setCancelable(false);
        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final EditText quantityNo = quantityDialog.findViewById(R.id.quntity_no);
        Button cancelBtn = quantityDialog.findViewById(R.id.cancel_btn);
        Button okBtn = quantityDialog.findViewById(R.id.ok_btn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productQuantity.setText("Qty: " + quantityNo.getText());
                quantityDialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityDialog.dismiss();
            }
        });
        quantityDialog.show();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage, freeCoupenIcon;
        private TextView productTitle, freeCoupens, productPrice, cuttedPrice, offersApplied, coupensApplied, productQuantity;

        public CartItemViewHolder(@NonNull View view) {
            super(view);
            productImage = view.findViewById(R.id.product_image);
            freeCoupenIcon = view.findViewById(R.id.free_coupen_icon);
            productTitle = view.findViewById(R.id.product_title);
            freeCoupens = view.findViewById(R.id.tv_free_coupen);
            productPrice = view.findViewById(R.id.product_price);
            cuttedPrice = view.findViewById(R.id.cutted_price);
            offersApplied = view.findViewById(R.id.offers_applied);
            coupensApplied = view.findViewById(R.id.coupens_applied);
            productQuantity = view.findViewById(R.id.product_quantity);
        }

        void setCartItemDetails(int resource, String productTitleTxt, int freeCoupenNo, String productPriceTxt, String cuttedPriceTxt, int offersAppliedNo) {
            productImage.setImageResource(resource);
            productTitle.setText(productTitleTxt);
            if (freeCoupenNo > 0) {
                freeCoupenIcon.setVisibility(View.VISIBLE);
                freeCoupens.setText("Free" + freeCoupenNo + " coupens");
            } else {
                freeCoupenIcon.setVisibility(View.INVISIBLE);
                freeCoupens.setVisibility(View.INVISIBLE);
                coupensApplied.setVisibility(View.INVISIBLE);
            }
            productPrice.setText(productPriceTxt);
            cuttedPrice.setText(cuttedPriceTxt);
            if (offersAppliedNo > 0) {
                offersApplied.setVisibility(View.VISIBLE);
                offersApplied.setText(offersAppliedNo + " Offers applied");
                coupensApplied.setVisibility(View.VISIBLE);
            } else {
                offersApplied.setVisibility(View.INVISIBLE);
            }
            productQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openQuantityDialog(itemView, productQuantity);
                }
            });
        }
    }

    class CartTotalAmountViewHolder extends RecyclerView.ViewHolder {
        TextView totalItems, totalItemsPrice, deliveryPrice, totalAmount, savedAmount;

        public CartTotalAmountViewHolder(@NonNull View rootView) {
            super(rootView);
            totalItems = (TextView) rootView.findViewById(R.id.total_items);
            totalItemsPrice = (TextView) rootView.findViewById(R.id.total_items_price);
            deliveryPrice = (TextView) rootView.findViewById(R.id.delivery_price);
            totalAmount = (TextView) rootView.findViewById(R.id.total_price);
            savedAmount = (TextView) rootView.findViewById(R.id.saved_amount);
        }

        void setTotalAmountDetails(String totalItemsTxt, String totalItemsPriceTxt, String deliveryPriceTxt, String totalAmountTxt, String savedAmountTxt) {
            totalItems.setText(totalItemsTxt);
            totalItemsPrice.setText(totalItemsPriceTxt);
            deliveryPrice.setText(deliveryPriceTxt);
            totalAmount.setText(totalAmountTxt);
            savedAmount.setText(savedAmountTxt);
        }
    }
}
