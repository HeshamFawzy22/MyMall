package com.example.mymall.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mymall.R;
import com.example.mymall.models.RewardsItemModel;
import com.example.mymall.ui.ProductDetailsActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.MyRewardsViewHolder> {
    private List<RewardsItemModel> rewardsItemModelList;
    private Boolean useMiniLayout = false;

    public RewardsAdapter(List<RewardsItemModel> rewardsItemModelList, Boolean useMiniLayout) {
        this.rewardsItemModelList = rewardsItemModelList;
        this.useMiniLayout = useMiniLayout;
    }

    @NonNull
    @Override
    public RewardsAdapter.MyRewardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (useMiniLayout) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_rewards_layout, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_rewards_item, parent, false);
        }
        return new MyRewardsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardsAdapter.MyRewardsViewHolder holder, int position) {
        RewardsItemModel rewardsItemModel = rewardsItemModelList.get(position);
        String title = rewardsItemModel.getTitle();
        String date = rewardsItemModel.getExpiryDate();
        String body = rewardsItemModel.getCoupenBody();

        holder.setData(title, date, body);
    }

    @Override
    public int getItemCount() {
        return rewardsItemModelList == null ? 0 : rewardsItemModelList.size();
    }

    class MyRewardsViewHolder extends RecyclerView.ViewHolder {
        TextView coupenTitle, coupenDate, coupenBody;

        public MyRewardsViewHolder(@NonNull View itemView) {
            super(itemView);
            coupenTitle = itemView.findViewById(R.id.coupen_title);
            coupenDate = itemView.findViewById(R.id.coupen_expiry_date);
            coupenBody = itemView.findViewById(R.id.coupen_body);
        }

        void setData(final String title, final String date, final String body) {
            coupenTitle.setText(title);
            coupenDate.setText(date);
            coupenBody.setText(body);

            if (useMiniLayout) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductDetailsActivity.coupenTitle.setText(title);
                        ProductDetailsActivity.coupenBody.setText(body);
                        ProductDetailsActivity.coupenExpiryDate.setText(date);
                        ProductDetailsActivity.showDialogRecyclerView();
                    }
                });
            }
        }
    }
}
