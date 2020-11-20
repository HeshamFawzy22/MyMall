package com.example.mymall.adapters;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;
import com.example.mymall.models.CartItemModel;
import com.example.mymall.ui.DeliveryActivity;
import com.example.mymall.ui.HomeFragment;
import com.example.mymall.ui.MainActivity;
import com.example.mymall.ui.ProductDetailsActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter {
    private int lastPosition = -1;
    private List<CartItemModel> cartItemModelList;
    private TextView totalCartAmount;
    private boolean showDeleteIcon;
    private LinearLayout coupenRedemptionLayout;

    public static int totalAmount = 0;

    public CartAdapter(List<CartItemModel> cartItemModelList , TextView totalCartAmount , boolean showDeleteIcon) {
        this.cartItemModelList = cartItemModelList;
        this.totalCartAmount = totalCartAmount;
        this.showDeleteIcon = showDeleteIcon;
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
                return new CartTotalAmountViewHolder(cartTotalView);
            default:
                return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CartItemModel itemModel = cartItemModelList.get(position);
        switch (cartItemModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                String productId = itemModel.getProductId();
                String resource = itemModel.getProductImage();
                String title = itemModel.getProductTitle();
                Long freeCoupens = itemModel.getFreeCoupens();
                String productPrice = itemModel.getProductPrice();
                String cuttedPrice = itemModel.getCuttedPrice();
                Long offersApplied = itemModel.getOffersApplied();
                Long productQuantity = itemModel.getProductQuantity();
                Long maxQuantity = itemModel.getMaxQuantity();
                boolean inStock = itemModel.isInStock();

                if (holder instanceof CartItemViewHolder) {
                    ((CartItemViewHolder) holder).setCartItemDetails(productId, resource, title, freeCoupens, productPrice, cuttedPrice, offersApplied, position , inStock , String.valueOf(productQuantity) , maxQuantity);
                }
                break;
            case CartItemModel.CART_TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemsPrice = 0;
                String deliveryPrice;
//                int totalAmount = 0;
                int savedAmount = 0;

                for (int i = 0 ; i < cartItemModelList.size() ; i++){
                    if (cartItemModelList.get(i).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(i).isInStock()) {
                        totalItems++;
                        totalItemsPrice +=  Integer.parseInt(cartItemModelList.get(i).getProductPrice());
                    }
                }
                if (totalItemsPrice > 500){
                    deliveryPrice = "Free";
                    totalAmount = totalItemsPrice;
                }else {
                    deliveryPrice = "60";
                    totalAmount = totalItemsPrice + 60;
                }

                if (holder instanceof CartTotalAmountViewHolder) {
                    ((CartTotalAmountViewHolder) holder).setTotalAmountDetails(totalItems, totalItemsPrice, deliveryPrice, totalAmount, savedAmount);
                }
                break;
            default:
                return;
        }
        //Enter Animation
        if (lastPosition < position){
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList == null ? 0 : cartItemModelList.size();
    }

    private void openQuantityDialog(final int position , final View itemView, final TextView productQuantity , final Long maxQuantity) {
        final Dialog quantityDialog = new Dialog(itemView.getContext());
        quantityDialog.setContentView(R.layout.quantity_dialog);
        quantityDialog.setCancelable(false);
        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final EditText quantityNo = quantityDialog.findViewById(R.id.quntity_no);
        Button cancelBtn = quantityDialog.findViewById(R.id.cancel_btn);
        Button okBtn = quantityDialog.findViewById(R.id.ok_btn);
        quantityNo.setHint("Max " + maxQuantity);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = quantityNo.getText().toString();
                if (!TextUtils.isEmpty(quantityNo.getText())) {
                    if (Long.valueOf(quantity) <= maxQuantity && Long.valueOf(quantityNo.getText().toString()) != 0) {
                        if (itemView.getContext() instanceof MainActivity){
                            ProductDetailsActivity.cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantity));
                        }else {
                            if (DeliveryActivity.fromCart) {
                                ProductDetailsActivity.cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantity));
                            }else {
                                DeliveryActivity.cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantity));
                            }
                        }
                        productQuantity.setText("Qty: " + quantityNo.getText());

                    } else {
                        Toast.makeText(itemView.getContext(), "Max quantity : " + maxQuantity, Toast.LENGTH_SHORT).show();
                    }
                    quantityDialog.dismiss();
                }
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
        private LinearLayout deleteBtn;

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
            deleteBtn = view.findViewById(R.id.remove_item_btn);
            coupenRedemptionLayout = view.findViewById(R.id.coupen_redemption_layout);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        void setCartItemDetails(String productId, String resource, String productTitleTxt, Long freeCoupenNo, String productPriceTxt, String cuttedPriceTxt, Long offersAppliedNo , final int position , boolean inStock , String quantity, final Long maxQuantity) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.null_icon)).into(productImage);
            productTitle.setText(productTitleTxt);

            if (inStock) {
                productPrice.setText("Rs." + productPriceTxt + "/-");
                productPrice.setTextColor(Color.parseColor("#000000"));
                cuttedPrice.setText("Rs." + cuttedPriceTxt + "/-");
                coupenRedemptionLayout.setVisibility(View.VISIBLE);

                productQuantity.setText("Qty: " + quantity);
                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openQuantityDialog(position ,itemView, productQuantity , maxQuantity);
                    }
                });
                if (freeCoupenNo > 0) {
                    freeCoupenIcon.setVisibility(View.VISIBLE);
                    freeCoupens.setText("Free " + freeCoupenNo + " coupens");
                } else {
                    freeCoupenIcon.setVisibility(View.INVISIBLE);
                    freeCoupens.setVisibility(View.INVISIBLE);
                    coupensApplied.setVisibility(View.INVISIBLE);
                }
                if (offersAppliedNo > 0) {
                    offersApplied.setVisibility(View.VISIBLE);
                    offersApplied.setText(offersAppliedNo + " Offers applied");
                    coupensApplied.setVisibility(View.VISIBLE);
                } else {
                    offersApplied.setVisibility(View.INVISIBLE);
                }

            }else {
                productPrice.setText("Out of stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                cuttedPrice.setText("");
                coupenRedemptionLayout.setVisibility(View.GONE);
                productQuantity.setVisibility(View.INVISIBLE);
                freeCoupens.setVisibility(View.INVISIBLE);
                freeCoupenIcon.setVisibility(View.GONE);
                coupensApplied.setVisibility(View.GONE);
                offersApplied.setVisibility(View.GONE);
            }

            if (showDeleteIcon){
                deleteBtn.setVisibility(View.VISIBLE);
            }else {
                deleteBtn.setVisibility(View.GONE);
            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (HomeFragment.isNetworkConnected(itemView.getContext())) {
                        if (!ProductDetailsActivity.RUNNING_CART_QUERY) {
                            ProductDetailsActivity.RUNNING_CART_QUERY = true;
                            ProductDetailsActivity.removeProductFromCart(position, itemView.getContext() , totalCartAmount);
                        }
                    }
                }
            });
        }
    }

    class CartTotalAmountViewHolder extends RecyclerView.ViewHolder {
        private TextView totalItems, totalItemsPrice, deliveryPrice, totalAmount, savedAmount;

        public CartTotalAmountViewHolder(@NonNull View rootView) {
            super(rootView);
            totalItems = rootView.findViewById(R.id.total_items);
            totalItemsPrice = rootView.findViewById(R.id.total_items_price);
            deliveryPrice = rootView.findViewById(R.id.delivery_price);
            totalAmount = rootView.findViewById(R.id.total_price);
            savedAmount = rootView.findViewById(R.id.saved_amount);
        }

        void setTotalAmountDetails(int totalItemsTxt, int totalItemsPriceTxt, String deliveryPriceTxt, int totalAmountTxt, int savedAmountTxt) {
            totalItems.setText("Price " + totalItemsTxt + " Items");
            totalItemsPrice.setText("Rs." + totalItemsPriceTxt + "/-");
            if (deliveryPriceTxt.equals("Free")) {
                deliveryPrice.setText(deliveryPriceTxt);
            }else {
                deliveryPrice.setText("Rs." + deliveryPriceTxt + "/-");
            }
            totalAmount.setText("Rs." + totalAmountTxt + "/-");
            totalCartAmount.setText("Rs." + totalAmountTxt + "/-");
            savedAmount.setText("You saved Rs." + savedAmountTxt + "/- in this order");

            LinearLayout parent = (LinearLayout) totalCartAmount.getParent().getParent();
            if (totalItemsPriceTxt == 0){
                cartItemModelList.remove(cartItemModelList.size() - 1);
                parent.setVisibility(View.GONE);
            }else {
                parent.setVisibility(View.VISIBLE);
            }
        }
    }
}
