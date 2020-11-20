package com.example.mymall.models;

import android.widget.ImageView;

public class AddressesModel {

    private String name;
    private String mobileNo;
    private String address;
    private String pincode;
    private Boolean selected;
    private ImageView icon;

    public AddressesModel(String name , String mobileNo, String address, String pincode, boolean selected) {
        this.name = name;
        this.mobileNo = mobileNo;
        this.address = address;
        this.pincode = pincode;
        this.selected = selected;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public ImageView getIcon() {
        return icon;
    }

    public void setIcon(ImageView icon) {
        this.icon = icon;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
