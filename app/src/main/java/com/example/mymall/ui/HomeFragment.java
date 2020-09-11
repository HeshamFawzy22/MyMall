package com.example.mymall.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class HomeFragment extends Fragment implements View.OnClickListener {
    public static final String VIEW_TYPE = "view_type";
    public static final String NUM_OF_BANNERS = "num_of_banners";
    public static final String NUM_OF_PRODUCTS = "num_of_products";
    public static final String STRIP_AD_BANNER = "strip_ad";
    public static final String BACKGROUND = "background";


    //////Fake Lists
    private List<CategoryModel> categoryModelFakeList = new ArrayList<>();
    private List<HomePageModel> homepageModelFakeList = new ArrayList<>();
    //////Fake Lists

    ////////////Category Recycler View
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;

    ////////////Category Recycler View

    private List<CategoryModel> categoryModelList = new ArrayList<>();
    //private List<SliderModel> sliderModelList;
    //private List<HorizontalProductScrollModel> horizontalProductScrollModelList;
    //private List<WishListItemModel> viewAllProductList;

    //////////////Home Recycler View
    private RecyclerView homeRecyclerView;
    private LinearLayoutManager homeLayoutManager;
    private HomePageAdapter homePageAdapter;
    //private List<HomePageModel> homePageModelList;
    private ImageView noInternetImg;
    private Button retryBtn;
    //////////////Home Recycler View

    //refresh layout
    private SwipeRefreshLayout swipeRefreshLayout;
    //refresh layout

    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadCategoriesNames = new ArrayList<>();

    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);

        initCategoryRecyclerView();
        initHomePageRecyclerView();
        // categoryModelFakeList
        setCategoryModelFakeList();
        // categoryModelFakeList

        // homepageModelFakeList
        setHomepageModelFakeList();
        // homepageModelFakeList

        checkInternetConnection();

        reloadPage();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        retryConnect();
    }

    private void setHomepageModelFakeList() {
        List<SliderModel> sliderModelFakeList = new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null", "#dfdfdf"));

        List<HorizontalProductScrollModel> horizontalProductScrollFakeModel = new ArrayList<>();
        horizontalProductScrollFakeModel.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollFakeModel.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollFakeModel.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollFakeModel.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollFakeModel.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollFakeModel.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollFakeModel.add(new HorizontalProductScrollModel("", "", "", "", ""));

        homepageModelFakeList.add(new HomePageModel(0, sliderModelFakeList));
        homepageModelFakeList.add(new HomePageModel(1, "", "#dfdfdf"));
        homepageModelFakeList.add(new HomePageModel(2, "", "#dfdfdf", horizontalProductScrollFakeModel, new ArrayList<WishListItemModel>()));
        homepageModelFakeList.add(new HomePageModel(3, "", "#dfdfdf", horizontalProductScrollFakeModel));

        homePageAdapter = new HomePageAdapter(homepageModelFakeList);
    }

    private void setCategoryModelFakeList() {
        categoryModelFakeList.add(new CategoryModel("null", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));
        categoryModelFakeList.add(new CategoryModel("", ""));

        categoryAdapter = new CategoryAdapter(categoryModelFakeList);
    }

    private void checkInternetConnection() {
        if (isNetworkConnected()) {
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homeRecyclerView.setVisibility(View.VISIBLE);
            noInternetImg.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            categoryRecyclerView.setAdapter(categoryAdapter);
            if (categoryModelList.size() == 0) {
                setCategoryModel();
            } else {
                categoryAdapter = new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }
            categoryRecyclerView.setAdapter(categoryAdapter);

            if (lists.size() == 0) {
                loadCategoriesNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());

                homePageAdapter = new HomePageAdapter(lists.get(0));
                setHomePageAdapter();
                loadFragmentData(homeRecyclerView, getContext(), 0, "HOME");
                categoryAdapter.notifyDataSetChanged();
            }


        } else {
            Glide.with(this).load(R.drawable.no_internet_conection).into(noInternetImg);
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            categoryRecyclerView.setVisibility(View.GONE);
            homeRecyclerView.setVisibility(View.GONE);
            noInternetImg.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "No internet connection!", Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void reloadPage() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retryConnect();
            }
        });
    }

    private void retryConnect(){
        swipeRefreshLayout.setRefreshing(true);
        categoryModelList.clear();
        lists.clear();
        loadCategoriesNames.clear();

        if (isNetworkConnected()) {
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homeRecyclerView.setVisibility(View.VISIBLE);
            noInternetImg.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);

            categoryAdapter = new CategoryAdapter(categoryModelFakeList);
            homePageAdapter = new HomePageAdapter(homepageModelFakeList);
            categoryRecyclerView.setAdapter(categoryAdapter);
            homeRecyclerView.setAdapter(homePageAdapter);

            setCategoryModel();

            loadCategoriesNames.add("HOME");
            lists.add(new ArrayList<HomePageModel>());

            loadFragmentData(homeRecyclerView, getContext(), 0, "HOME");

            homePageAdapter.notifyDataSetChanged();
        } else {
            Glide.with(getContext()).load(R.drawable.no_internet_conection).into(noInternetImg);
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            categoryRecyclerView.setVisibility(View.GONE);
            homeRecyclerView.setVisibility(View.GONE);
            noInternetImg.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "No internet connection!", Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.setRefreshing(false);
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static void loadFragmentData(final RecyclerView adapter, final Context context, final int index, String categoryName) {
        //homePageModelList = new ArrayList<>();
        BannersDao.getBanners(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    getFirebaseData(task, adapter, index);
                } else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, categoryName);
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
        //setHomePageAdapter();
    }

    private void setHomePageAdapter() {
        //homePageAdapter = new HomePageAdapter(lists.get(0));
        homeRecyclerView.setAdapter(homePageAdapter);
        homePageAdapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        noInternetImg = view.findViewById(R.id.noInternetConnection);
        homeRecyclerView = view.findViewById(R.id.home_recycler_view);
        categoryRecyclerView = view.findViewById(R.id.home_category_recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary));
        retryBtn = view.findViewById(R.id.retry_btn);
        retryBtn.setOnClickListener(HomeFragment.this);
    }

    private void initHomePageRecyclerView() {
        homeLayoutManager = new LinearLayoutManager(getContext());
        homeLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        homeRecyclerView.setLayoutManager(homeLayoutManager);
    }


    private void setCategoryAdapter(List<CategoryModel> categoryModelList) {
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
        //categoryModelList = new ArrayList<>();
        CategoriesDao.getCategories(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        categoryModelList.add(new CategoryModel(documentSnapshot.get(CategoriesDao.CATEGORY_ICON).toString()
                                , documentSnapshot.get(CategoriesDao.CATEGORY_NAME).toString()));
                    }
                    setCategoryAdapter(categoryModelList);
                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void initCategoryRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private static void getFirebaseData(@NonNull Task<QuerySnapshot> task, final RecyclerView homeRecyclerView, int index) {
        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

            if ((long) documentSnapshot.get(VIEW_TYPE) == 0) {
                setSliderModel(documentSnapshot, index);
            } else if ((long) documentSnapshot.get(VIEW_TYPE) == 1) {
                setStripAddModel(documentSnapshot, index);
            } else if ((long) documentSnapshot.get(VIEW_TYPE) == 2) {
                setHorizontalProductModel(documentSnapshot, index);
            } else if ((long) documentSnapshot.get(VIEW_TYPE) == 3) {
                setGridProductModel(documentSnapshot, index);
            } else if ((long) documentSnapshot.get(VIEW_TYPE) == 4) {

            }
        }
        HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));
        homeRecyclerView.setAdapter(homePageAdapter);
        homePageAdapter.notifyDataSetChanged();
    }

    private static void setGridProductModel(QueryDocumentSnapshot documentSnapshot, int index) {
        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
        long banners_no = (long) documentSnapshot.get(NUM_OF_PRODUCTS);
        for (long i = 1; i < banners_no + 1; i++) {
            horizontalProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_id_" + i).toString()
                    , documentSnapshot.get("product_image_" + i).toString()
                    , documentSnapshot.get("product_title_" + i).toString()
                    , documentSnapshot.get("product_subtitle_" + i).toString()
                    , documentSnapshot.get("product_price_" + i).toString()));
        }
        lists.get(index).add(new HomePageModel(3, documentSnapshot.get("layout_title").toString()
                , documentSnapshot.get("layout_background").toString(), horizontalProductScrollModelList));
    }

    private static void setSliderModel(QueryDocumentSnapshot documentSnapshot, int index) {
        List<SliderModel> sliderModelList = new ArrayList<>();
        long banners_no = (long) documentSnapshot.get(NUM_OF_BANNERS);
        for (long i = 1; i < banners_no + 1; i++) {
            sliderModelList.add(new SliderModel(documentSnapshot.get("banner_" + i).toString(),
                    documentSnapshot.get("banner_" + i + "_background").toString()));
        }
        lists.get(index).add(new HomePageModel(0, sliderModelList));
        /*sliderModelList.add(new SliderModel(R.drawable.my_cart,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.my_account,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.common_google_signin_btn_icon_dark_focused,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_menu_manage,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.banner,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.forgot_password_image,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.my_orders,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.my_wishlist,"#077AE4"));*/
    }

    private static void setStripAddModel(QueryDocumentSnapshot documentSnapshot, int index) {
        lists.get(index).add(new HomePageModel(1, documentSnapshot.get(STRIP_AD_BANNER).toString(),
                documentSnapshot.get(BACKGROUND).toString()));
    }

    private static void setHorizontalProductModel(QueryDocumentSnapshot documentSnapshot, int index) {
        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
        List<WishListItemModel> viewAllProductList = new ArrayList<>();
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
        lists.get(index).add(new HomePageModel(2, documentSnapshot.get("layout_title").toString()
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.retry_btn) {
            retryConnect();
        }
    }
}