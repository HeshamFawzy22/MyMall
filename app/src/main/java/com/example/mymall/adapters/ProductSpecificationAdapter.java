package com.example.mymall.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mymall.R;
import com.example.mymall.models.ProductSpecificationModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductSpecificationAdapter extends RecyclerView.Adapter<ProductSpecificationAdapter.ViewHolder> {
    List<ProductSpecificationModel> productSpecificationModelList;

    public ProductSpecificationAdapter(List<ProductSpecificationModel> productSpecificationModelList) {
        this.productSpecificationModelList = productSpecificationModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (productSpecificationModelList.get(position).getType()) {
            case 0:
                return ProductSpecificationModel.SPECIFICATION_TITLE;
            case 1:
                return ProductSpecificationModel.SPECIFICATION_BODY;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public ProductSpecificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case ProductSpecificationModel.SPECIFICATION_TITLE:
                ////Declare and set TextView title
                TextView title = new TextView(parent.getContext());
                title.setTypeface(null, Typeface.BOLD);
                title.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(setDp(16, parent.getContext()),
                        setDp(16, parent.getContext()),
                        setDp(16, parent.getContext()),
                        setDp(8, parent.getContext()));
                title.setLayoutParams(layoutParams);
                return new ViewHolder(title);
            case ProductSpecificationModel.SPECIFICATION_BODY:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_specification_item, parent, false);
                return new ViewHolder(view);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ProductSpecificationAdapter.ViewHolder holder, int position) {
        switch (productSpecificationModelList.get(position).getType()) {
            case ProductSpecificationModel.SPECIFICATION_TITLE:
                holder.setTitle(productSpecificationModelList.get(position).getTitle());
                break;
            case ProductSpecificationModel.SPECIFICATION_BODY:
                String featureName = productSpecificationModelList.get(position).getFeatureName();
                String featureValue = productSpecificationModelList.get(position).getFeatureValue();

                holder.setFeature(featureName, featureValue);
                break;
            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return productSpecificationModelList == null ? 0 : productSpecificationModelList.size();
    }

    ////Set title dp
    int setDp(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView featureName, featureValue, title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        void setTitle(String titleTxt) {
            title = (TextView) itemView;
            title.setText(titleTxt);
        }

        void setFeature(String name, String value) {
            featureName = itemView.findViewById(R.id.product_specification_feature_name);
            featureValue = itemView.findViewById(R.id.product_specification_feature_value);
            featureName.setText(name);
            featureValue.setText(value);
        }
    }
}
