package com.example.mymall.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mymall.R;
import com.example.mymall.models.AddressesModel;
import com.example.mymall.ui.MyCartFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.mymall.ui.DeliveryActivity.SELECTED_ADDRESS;
import static com.example.mymall.ui.MyAccountFragment.MANAGED_ADDRESS;
import static com.example.mymall.ui.MyAddressesActivity.refreshItem;

public class Addresses_Adapter extends RecyclerView.Adapter<Addresses_Adapter.AddressesViewHolder> {

    private List<AddressesModel> addressesModelList;
    private int MODE;
    private int preAddressSelected;

    public Addresses_Adapter(List<AddressesModel> addressesModelList, int MODE) {
        this.addressesModelList = addressesModelList;
        this.MODE = MODE;
        preAddressSelected = MyCartFragment.selectedAddress;
    }

    @NonNull
    @Override
    public AddressesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_item, parent, false);
        return new AddressesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressesViewHolder holder, int position) {

        AddressesModel addressesModel = addressesModelList.get(position);
        String name = addressesModel.getName();
        String mobileNo = addressesModel.getMobileNo();
        String address = addressesModel.getAddress();
        String pincode = addressesModel.getPincode();
        Boolean selected = addressesModel.getSelected();

        holder.setData(name , mobileNo, address, pincode, selected, position);
    }

    @Override
    public int getItemCount() {
        return addressesModelList == null ? 0 : addressesModelList.size();
    }


    class AddressesViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView address;
        protected TextView pincode;
        protected ImageView icon;
        private LinearLayout optionContainer;

        public AddressesViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            pincode = itemView.findViewById(R.id.pincode);
            icon = itemView.findViewById(R.id.icon_check);
            optionContainer = itemView.findViewById(R.id.option_container);
        }

        private void setData(String userName , String mobileNo, String userAddress, String UserPincode, final boolean selected, final int position) {
            name.setText(userName + " - " + mobileNo);
            address.setText(userAddress);
            pincode.setText(UserPincode);

            if (MODE == SELECTED_ADDRESS) {
                icon.setImageResource(R.drawable.check);
                if (selected) {
                    icon.setVisibility(View.VISIBLE);
                    preAddressSelected = position;
                } else {
                    icon.setVisibility(View.GONE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (preAddressSelected != position) {
                            addressesModelList.get(position).setSelected(true);
                            addressesModelList.get(preAddressSelected).setSelected(false);
                            refreshItem(preAddressSelected, position);
                            preAddressSelected = position;
                            MyCartFragment.selectedAddress = position;
                        }
                    }
                });
            } else if (MODE == MANAGED_ADDRESS) {
                optionContainer.setVisibility(View.GONE);
                icon.setImageResource(R.drawable.vertical_dots);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionContainer.setVisibility(View.VISIBLE);
                        refreshItem(preAddressSelected, preAddressSelected);
                        preAddressSelected = position;
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refreshItem(preAddressSelected, preAddressSelected);
                        preAddressSelected = -1;
                    }
                });
            }
        }
    }
}
