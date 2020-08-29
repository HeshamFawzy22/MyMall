package com.example.mymall.models;

import java.util.List;

public class HomePageModel {
    public static final int BANNER_SLIDER = 0;
    public static final int STRIP_AD_BANNER = 1;
    public static final int HORIZONTAL_PRODUCT = 2;
    public static final int Grid_PRODUCT = 3;
    private int type;
    private String backgroundColor;


    ////////////Banner Slider Model
    private List<SliderModel> sliderModelList;
    ///////////Strip Ad Model
    private String resourceImage;
    private String title;
    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;
    ///////////Horizontal Product Model
    private List<WishListItemModel> viewAllProductList;

    public HomePageModel(int type, List<SliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }
    ////////////Banner Slider Model

    public HomePageModel(int type, String resourceImage, String backgroundColor) {
        this.type = type;
        this.resourceImage = resourceImage;
        this.backgroundColor = backgroundColor;
    }


    public HomePageModel(int type, String title, String backgroundColor, List<HorizontalProductScrollModel> horizontalProductScrollModelList, List<WishListItemModel> viewAllProductList) {
        this.type = type;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
        this.viewAllProductList = viewAllProductList;
    }

    ///////////Grid Product Model
    public HomePageModel(int type, String title, String backgroundColor, List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.type = type;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<SliderModel> getSliderModelList() {
        return sliderModelList;
    }
    ///////////Strip Ad Model

    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }

    public String getResourceImage() {
        return resourceImage;
    }

    public void setResourceImage(String resourceImage) {
        this.resourceImage = resourceImage;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<WishListItemModel> getViewAllProductList() {
        return viewAllProductList;
    }
    ///////////Horizontal Product Model

    public void setViewAllProductList(List<WishListItemModel> viewAllProductList) {
        this.viewAllProductList = viewAllProductList;
    }
    ///////////Grid Product Model

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HorizontalProductScrollModel> getHorizontalProductScrollModelList() {
        return horizontalProductScrollModelList;
    }

    public void setHorizontalProductScrollModelList(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }


}
