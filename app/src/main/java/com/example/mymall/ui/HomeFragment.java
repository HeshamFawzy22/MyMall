package com.example.mymall.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mymall.R;
import com.example.mymall.adapters.CategoryAdapter;
import com.example.mymall.adapters.HomePageAdapter;
import com.example.mymall.database.BannersDao;
import com.example.mymall.database.CategoriesDao;
import com.example.mymall.models.CategoryModel;
import com.example.mymall.models.HomePageModel;
import com.example.mymall.models.HorizontalProductScrollModel;
import com.example.mymall.models.SliderModel;
import com.example.mymall.models.WishListItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HomeFragment extends Fragment {
    public static final String VIEW_TYPE = "view_type";
    public static final String NUM_OF_BANNERS = "num_of_banners";
    public static final String NUM_OF_PRODUCTS = "num_of_products";
    public static final String STRIP_AD_BANNER = "strip_ad";
    public static final String BACKGROUND = "background";

    ////////////Category Recycler View
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    ////////////Category Recycler View

    private List<CategoryModel> categoryModelList;
    private List<SliderModel> sliderModelList;
    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;
    private List<WishListItemModel> viewAllProductList;

    //////////////Home Recycler View
    private RecyclerView homeRecyclerView;
    private LinearLayoutManager homeLayoutManager;
    private HomePageAdapter homePageAdapter;
    private List<HomePageModel> homePageModelList;
    private ImageView noInternetImg;
    //////////////Home Recycler View


    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);

        initCategoryRecyclerView(view);

        checkInternetConnection();

        return view;
    }

    private void checkInternetConnection() {
        if (isNetworkConnected()) {
            noInternetImg.setVisibility(View.GONE);
            setCategoryModel();

            setHomePageModel();
        } else {
            Glide.with(this).load(R.drawable.no_internet_conection).into(noInternetImg);

            noInternetImg.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void setHomePageModel() {
        homePageModelList = new ArrayList<>();
        BannersDao.getBanners(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    getHomeFirebaseData(task);
                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
//        homePageModelList.add(new HomePageModel(0,sliderModelList));
//        homePageModelList.add(new HomePageModel(1,R.drawable.strip_ad,"#000000"));
//        homePageModelList.add(new HomePageModel(2,"Deals of the Day!!!",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(3,"Monday Deals!!!",horizontalProductScrollModelList));
//
//        homePageModelList.add(new HomePageModel(2,"Deals of the Day!!!",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(1,R.drawable.strip_ad,"#000000"));
//
//        homePageModelList.add(new HomePageModel(3,"Monday Deals!!!",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(1,R.drawable.strip_ad,"#000000"));
//        homePageModelList.add(new HomePageModel(1,R.drawable.strip_ad,"#000000"));
//        homePageModelList.add(new HomePageModel(3,"Monday Deals!!!",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(1,R.drawable.strip_ad,"#000000"));
//        homePageModelList.add(new HomePageModel(2,"Deals of the Day!!!",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(2,"Deals of the Day!!!",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(2,"Deals of the Day!!!",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(2,"Deals of the Day!!!",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(3,"Monday Deals!!!",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(3,"Monday Deals!!!",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(3,"Monday Deals!!!",horizontalProductScrollModelList));
//        homePageModelList.add(new HomePageModel(3,"Monday Deals!!!",horizontalProductScrollModelList));
        setHomePageAdapter();
    }

    private void setHomePageAdapter() {
        homePageAdapter = new HomePageAdapter(homePageModelList);
        homeRecyclerView.setAdapter(homePageAdapter);
        homePageAdapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        noInternetImg = view.findViewById(R.id.noInternetConnection);
        homeRecyclerView = view.findViewById(R.id.home_recycler_view);
        homeLayoutManager = new LinearLayoutManager(getContext());
        homeLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homeRecyclerView.setLayoutManager(homeLayoutManager);
    }


    private void setCategoryAdapter() {
        categoryAdapter = new CategoryAdapter(categoryModelList);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();
    }

    private void setCategoryModel() {
//        categoryModelList.add(new CategoryModel("IconLink","Home"));
//        categoryModelList.add(new CategoryModel("IconLink","Electronics"));
//        categoryModelList.add(new CategoryModel("IconLink","Appliances"));
//        categoryModelList.add(new CategoryModel("IconLink","Furniture"));
//        categoryModelList.add(new CategoryModel("IconLink","Toys"));
//        categoryModelList.add(new CategoryModel("IconLink","Sports"));
//        categoryModelList.add(new CategoryModel("IconLink","Wall Arts"));
//        categoryModelList.add(new CategoryModel("IconLink","Books"));
//        categoryModelList.add(new CategoryModel("IconLink","Shoes"));
        categoryModelList = new ArrayList<>();
        CategoriesDao.getCategories(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        categoryModelList.add(new CategoryModel(documentSnapshot.get(CategoriesDao.CATEGORY_ICON).toString()
                                , documentSnapshot.get(CategoriesDao.CATEGORY_NAME).toString()));
                    }
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        setCategoryAdapter();
    }

    private void initCategoryRecyclerView(View view) {
        categoryRecyclerView = view.findViewById(R.id.home_category_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getHomeFirebaseData(@NonNull Task<QuerySnapshot> task) {
        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

            if ((long) documentSnapshot.get(VIEW_TYPE) == 0) {
                setSliderModel(documentSnapshot);
            } else if ((long) documentSnapshot.get(VIEW_TYPE) == 1) {
                setStripAddModel(documentSnapshot);
            } else if ((long) documentSnapshot.get(VIEW_TYPE) == 2) {
                setHorizontalProductModel(documentSnapshot);
            } else if ((long) documentSnapshot.get(VIEW_TYPE) == 3) {
                setGridProductModel(documentSnapshot);
            } else if ((long) documentSnapshot.get(VIEW_TYPE) == 4) {

            }

        }
    }

    private void setGridProductModel(QueryDocumentSnapshot documentSnapshot) {
        horizontalProductScrollModelList = new ArrayList<>();
        long banners_no = (long) documentSnapshot.get(NUM_OF_PRODUCTS);
        for (long i = 1; i < banners_no + 1; i++) {
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_id_" + i).toString()
                    , documentSnapshot.get("product_image_" + i).toString()
                    , documentSnapshot.get("product_title_" + i).toString()
                    , documentSnapshot.get("product_subtitle_" + i).toString()
                    , documentSnapshot.get("product_price_" + i).toString()));
        }
        homePageModelList.add(new HomePageModel(3, documentSnapshot.get("layout_title").toString()
                , documentSnapshot.get("layout_background").toString(), horizontalProductScrollModelList));
    }

    private void setSliderModel(QueryDocumentSnapshot documentSnapshot) {
        sliderModelList = new ArrayList<>();
        long banners_no = (long) documentSnapshot.get(NUM_OF_BANNERS);
        for (long i = 1; i < banners_no + 1; i++) {
            sliderModelList.add(new SliderModel(documentSnapshot.get("banner_" + i).toString(),
                    documentSnapshot.get("banner_" + i + "_background").toString()));
        }
        homePageModelList.add(new HomePageModel(0, sliderModelList));
        /*sliderModelList.add(new SliderModel(R.drawable.my_cart,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.my_account,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.common_google_signin_btn_icon_dark_focused,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_menu_manage,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.banner,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.forgot_password_image,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.my_orders,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.my_wishlist,"#077AE4"));*/
    }

    private void setStripAddModel(QueryDocumentSnapshot documentSnapshot) {
        homePageModelList.add(new HomePageModel(1, documentSnapshot.get(STRIP_AD_BANNER).toString(),
                documentSnapshot.get(BACKGROUND).toString()));
    }

    private void setHorizontalProductModel(QueryDocumentSnapshot documentSnapshot) {
        horizontalProductScrollModelList = new ArrayList<>();
        viewAllProductList = new ArrayList<>();
        long banners_no = (long) documentSnapshot.get(NUM_OF_PRODUCTS);
        for (long i = 1; i < banners_no + 1; i++) {
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_id_" + i).toString()
                    , documentSnapshot.get("product_image_" + i).toString()
                    , documentSnapshot.get("product_title_" + i).toString()
                    , documentSnapshot.get("product_subtitle_" + i).toString()
                    , documentSnapshot.get("product_price_" + i).toString()));

            viewAllProductList.add(new WishListItemModel(documentSnapshot.get("product_image_" + i).toString()
                    , documentSnapshot.get("product_full_title_" + i).toString()
                    , (long) documentSnapshot.get("free_coupens_" + i)
                    , documentSnapshot.get("average_rating_" + i).toString()
                    , (long) documentSnapshot.get("total_ratings_" + i)
                    , documentSnapshot.get("product_price_" + i).toString()
                    , documentSnapshot.get("cutted_price_" + i).toString()
                    , (boolean) documentSnapshot.get("COD_" + i)));
        }
        homePageModelList.add(new HomePageModel(2, documentSnapshot.get("layout_title").toString()
                , documentSnapshot.get("layout_background").toString(), horizontalProductScrollModelList, viewAllProductList));

        /*horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.profile_placeholder,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.forgot_password_image,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.banner,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.strip_ad,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_store_mall_directory,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_menu_share,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_mail_green,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_mail_green,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_mail_green,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_mail_green,"Redmi A","SD 625 Processor","Rs.5999/-"));*/
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}