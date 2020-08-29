package com.example.mymall.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymall.R;
import com.example.mymall.adapters.RewardsAdapter;
import com.example.mymall.models.RewardsItemModel;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyRewardsFragment extends Fragment {

    //ui
    private RecyclerView myRewardsRecyclerView;

    //Declare
    private RewardsAdapter rewardsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<RewardsItemModel> rewardsItemModelList;

    public MyRewardsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_rewards, container, false);
        initView(view);
        setRewardsItemModelList();

        return view;
    }

    private void initView(View view) {
        myRewardsRecyclerView = view.findViewById(R.id.my_rewards_recycler_view);
    }

    private void setRewardsAdapter(List<RewardsItemModel> rewardsItemModelList) {
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rewardsAdapter = new RewardsAdapter(rewardsItemModelList, false);
        myRewardsRecyclerView.setLayoutManager(linearLayoutManager);
        myRewardsRecyclerView.setAdapter(rewardsAdapter);
    }

    private void setRewardsItemModelList() {
        rewardsItemModelList = new ArrayList<>();
        rewardsItemModelList.add(new RewardsItemModel("Rewards", "till 3rd, August 2018", "GET 20% OFF on any product above Rs.500/- and below Rs.2500/-"));
        rewardsItemModelList.add(new RewardsItemModel("Rewards", "till 3rd, August 2018", "GET 20% OFF on any product above Rs.500/- and below Rs.2500/-"));
        rewardsItemModelList.add(new RewardsItemModel("Rewards", "till 3rd, August 2018", "GET 20% OFF on any product above Rs.500/- and below Rs.2500/-"));
        rewardsItemModelList.add(new RewardsItemModel("Coupens", "till 3rd, August 2018", "GET 20% OFF on any product above Rs.500/- and below Rs.2500/-"));
        rewardsItemModelList.add(new RewardsItemModel("Rewards", "till 3rd, August 2018", "GET 20% OFF on any product above Rs.500/- and below Rs.2500/-"));
        rewardsItemModelList.add(new RewardsItemModel("Rewards", "till 3rd, August 2018", "GET 20% OFF on any product above Rs.500/- and below Rs.2500/-"));

        setRewardsAdapter(rewardsItemModelList);
    }
}