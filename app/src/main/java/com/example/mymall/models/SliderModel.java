package com.example.mymall.models;

public class SliderModel {

    private String bannerImage;
    private String backgroundColor;


    public SliderModel(String bannerImage, String backgroundColor) {
        this.bannerImage = bannerImage;
        this.backgroundColor = backgroundColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }
}
