package com.example.mymall.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymall.R;
import com.example.mymall.adapters.ProductDetailsImagesAdapter;
import com.example.mymall.adapters.ProductDetailsViewPagerAdapter;
import com.example.mymall.adapters.RewardsAdapter;
import com.example.mymall.database.ProductsDao;
import com.example.mymall.database.UserDao;
import com.example.mymall.models.ProductSpecificationModel;
import com.example.mymall.models.RewardsItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import static com.example.mymall.adapters.GridProductLayoutAdapter.PRODUCT_ID;
import static com.example.mymall.ui.MainActivity.showCart;
import static com.example.mymall.ui.MainActivity.startSignDialog;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private static boolean ALREADY_ADDED_TO_WISHLIST = false;

    //Specification List, Description and other details Fragment body
    private String productDescription;
    private String productOtherDetails;

    private List<ProductSpecificationModel> specificationModelList;
    //Description and other details Fragment body

    //Coupen Redeem Dialog item
    public static TextView coupenTitle;
    public static TextView coupenExpiryDate;
    public static TextView coupenBody;
    private static LinearLayoutManager linearLayoutManager;
    private static LinearLayout selectedCoupen;
    private static RecyclerView coupensRecyclerView;

    //product description
    private ConstraintLayout productDetailsOnlyContainer;
    private ConstraintLayout productDetailsTabsContainer;
    protected ViewPager productDetailsViewPager;
    protected TabLayout productDetailsTabLayout;
    //product description

    //product details only
    private TextView productOnlyDescriptionBody;
    //product details only

    //ui
    protected ViewPager productDetailsImagesViewPager;
    protected TabLayout viewPagerIndicator;
    protected FloatingActionButton addToWishListBtn;

    protected LinearLayout coupenRedemptionLayout;
    private LinearLayout addToCartBtn;
    private Button buyNowBtn;
    private Button coupenRedeemBtn;
    private TextView productTitle;
    private TextView averageRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private TextView cuttedPrice;
    private TextView tvCodIndicator;
    private ImageView codIndicator;
    private TextView rewardTitle;
    private TextView rewardBody;


    ////////////////Declare Coupen Redeem Dialog
    private RewardsAdapter rewardsAdapter;
    private List<RewardsItemModel> rewardsItemModelList;
    //Coupen Redeem Dialog ui
    private ImageView toggleRecyclerView;
    private TextView discountedPrice;

    ////////////////Declare Coupen Redeem Dialog
    private TextView originalPrice;

    /////rating layout container
    private LinearLayout starRatingContainer;
    private TextView averageRating;
    private TextView totalRatings;
    private LinearLayout ratingNoContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    /////rating layout container

    //Declare
    private List<String> productImages;
    private ProductDetailsImagesAdapter productDetailsImagesAdapter;
    private ProductDetailsViewPagerAdapter productDetailsViewPagerAdapter;

    private FirebaseUser currentUser;
    private String productId;
    public static List<String> wishList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        rateProduct();

        setProductDetailsData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            coupenRedemptionLayout.setVisibility(View.GONE);
        }else {
            coupenRedemptionLayout.setVisibility(View.VISIBLE);
        }
    }

    public static void showDialogRecyclerView() {
        if (coupensRecyclerView.getVisibility() == View.GONE) {
            coupensRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupen.setVisibility(View.GONE);
        } else {
            coupensRecyclerView.setVisibility(View.GONE);
            selectedCoupen.setVisibility(View.VISIBLE);
        }
    }
    private void rateProduct() {
        for (int i = 0; i < starRatingContainer.getChildCount(); i++) {
            final int starPosition = i;
            starRatingContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    if (currentUser == null){
                        startSignDialog(ProductDetailsActivity.this);
                    }else {
                        setRating(starPosition);
                    }
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setRating(int starPosition) {
        for (int i = 0; i < starRatingContainer.getChildCount(); i++) {
            ImageView star = (ImageView) starRatingContainer.getChildAt(i);
            star.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (i <= starPosition) {
                star.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }

    private void setDetailsViewPagerAdapter() {
        productDetailsViewPagerAdapter = new ProductDetailsViewPagerAdapter(getSupportFragmentManager(), productDetailsTabLayout.getTabCount(),productDescription,productOtherDetails,specificationModelList);
        productDetailsViewPager.setAdapter(productDetailsViewPagerAdapter);
        productDetailsTabLayout.setupWithViewPager(productDetailsViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.main_search_icon) {

            return true;
        } else if (id == R.id.main_cart_icon) {
            //todo: cart
            if (currentUser == null){
                startSignDialog(this);
            }else {
                Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                showCart = true;
                startActivity(cartIntent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        productDetailsImagesViewPager = (ViewPager) findViewById(R.id.product_details_images_view_pager);
        viewPagerIndicator = (TabLayout) findViewById(R.id.view_pager_indicator);
        productDetailsViewPager = findViewById(R.id.product_details_view_pager);
        productDetailsTabLayout = findViewById(R.id.product_details_tab_layout);
        starRatingContainer = findViewById(R.id.rating_star_container);
        buyNowBtn = findViewById(R.id.buy_now_btn);
        buyNowBtn.setOnClickListener(this);
        addToWishListBtn = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        addToWishListBtn.setOnClickListener(ProductDetailsActivity.this);
        coupenRedeemBtn = findViewById(R.id.coupen_redemption_btn);
        coupenRedeemBtn.setOnClickListener(ProductDetailsActivity.this);
        productTitle = findViewById(R.id.product_title);
        productPrice = findViewById(R.id.product_price);
        averageRatingMiniView = findViewById(R.id.tv_product_rating_mini_view);
        totalRatingMiniView = findViewById(R.id.total_rating_mini_view);
        tvCodIndicator = findViewById(R.id.tv_cod_indicator);
        codIndicator = findViewById(R.id.cod_indecator);
        selectedCoupen = (LinearLayout) findViewById(R.id.selected_coupen);
        selectedCoupen = (LinearLayout) findViewById(R.id.selected_coupen);
        coupenRedemptionLayout = (LinearLayout) findViewById(R.id.coupen_redemption_layout);
        cuttedPrice = (TextView) findViewById(R.id.cutted_price);
        rewardTitle = findViewById(R.id.reward_title);
        rewardBody = findViewById(R.id.reward_body);
        productDetailsTabsContainer = findViewById(R.id.product_details_tabs_container);
        productDetailsOnlyContainer = findViewById(R.id.product_details_only_container);
        productOnlyDescriptionBody = findViewById(R.id.product_details_body);
        totalRatings = findViewById(R.id.total_ratings);
        ratingNoContainer = findViewById(R.id.ratings_numbers_container);
        totalRatingsFigure = findViewById(R.id.total_ratings_figure);
        ratingsProgressBarContainer = findViewById(R.id.ratings_progress_bar_container);
        averageRating = findViewById(R.id.average_rating);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);
        addToCartBtn.setOnClickListener(this);
    }

    private void setProductDetailsData() {
        productId = getIntent().getStringExtra(PRODUCT_ID);
        ProductsDao.getProducts(productId,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    setProductDetailsImagesLayout(documentSnapshot);

                    setRewardWithProductLayout(documentSnapshot);
                    setProductDescriptionLayout(documentSnapshot);
                    setProductRatingLayout(documentSnapshot);

                    if (wishList.size() == 0){
                        setUserWishList();
                    }

                    if (wishList.contains(productId)){
                        ALREADY_ADDED_TO_WISHLIST = true;
                        addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                    }else {
                        ALREADY_ADDED_TO_WISHLIST = false;
                    }

                } else {
                    Toast.makeText(ProductDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setUserWishList() {
        UserDao.loadUserWishList(currentUser.getUid(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    for (long i = 0 ; i < (long) task.getResult().get("list_size") ; i++){
                        wishList.add(task.getResult().get("product_ID_" + i).toString());
                    }
                }else {
                    Toast.makeText(ProductDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setProductRatingLayout(DocumentSnapshot documentSnapshot) {
        totalRatings.setText((long)documentSnapshot.get("total_ratings") + " ratings");
        for (int i = 0 ; i < 5 ; i ++){

            TextView ratings = (TextView)ratingNoContainer.getChildAt(i);
            ratings.setText(String.valueOf((long) documentSnapshot.get((5-i) + "_star")));


            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(i);
            progressBar.setMax(Integer.parseInt(String.valueOf((long)documentSnapshot.get("total_ratings"))));
            progressBar.setProgress(Integer.parseInt(String.valueOf((long)documentSnapshot.get((5-i) + "_star"))));

        }
        totalRatingsFigure.setText(String.valueOf((long)documentSnapshot.get("total_ratings")));
    }

    private void setProductDescriptionLayout(DocumentSnapshot documentSnapshot) {
        if ((boolean)documentSnapshot.get("use_tab_layout")){
            productDetailsTabsContainer.setVisibility(View.VISIBLE);
            productDetailsOnlyContainer.setVisibility(View.GONE);

            productDescription = documentSnapshot.get("product_description").toString();
            productOtherDetails = documentSnapshot.get("product_other_details").toString();

            specificationModelList = new ArrayList<>();
            for (long x = 1 ; x < (long)documentSnapshot.get("total_specification_titles") + 1 ; x++){
                specificationModelList.add(new ProductSpecificationModel(0,documentSnapshot.get("spec_title_" + x).toString()));
                for (long y = 1 ; y < (long)documentSnapshot.get("spec_title_" + x + "_total_fields") + 1 ; y++){
                    specificationModelList.add(new ProductSpecificationModel(1,documentSnapshot.get("spec_title_" + x +"_field_" + y + "_name").toString() ,documentSnapshot.get("spec_title_" + x +"_field_" + y + "_value").toString()));
                }
            }
        }else {
            setProductDetailsOnlyLayout(documentSnapshot);
        }
        setDetailsViewPagerAdapter();
    }

    private void setProductDetailsOnlyLayout(DocumentSnapshot documentSnapshot){
        productDetailsTabsContainer.setVisibility(View.GONE);
        productDetailsOnlyContainer.setVisibility(View.VISIBLE);
        productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
    }

    private void setRewardWithProductLayout(DocumentSnapshot documentSnapshot) {
        rewardTitle.setText(documentSnapshot.get("free_coupens") + " " + documentSnapshot.get("free_coupen_title").toString());
        rewardBody.setText(documentSnapshot.get("free_coupen_body").toString());
    }

    private void setProductDetailsImagesLayout(DocumentSnapshot documentSnapshot) {
        productImages = new ArrayList<>();

        for (long i = 1; i < (long) documentSnapshot.get("num_of_product_images") + 1; i++) {
            productImages.add(documentSnapshot.get("product_image_" + i).toString());
        }
        setImagesViewPagerAdapter(productImages);
        productTitle.setText(documentSnapshot.get("product_title").toString());
        averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
        totalRatingMiniView.setText("(" + documentSnapshot.get("total_ratings").toString() + ") ratings");
        productPrice.setText("Rs." + documentSnapshot.get("product_price").toString() + "/-");
        cuttedPrice.setText("Rs." + documentSnapshot.get("cutted_price").toString() + "/-");
        if ((boolean) documentSnapshot.get("COD")) {
            codIndicator.setVisibility(View.INVISIBLE);
            tvCodIndicator.setVisibility(View.INVISIBLE);
        } else {
            codIndicator.setVisibility(View.VISIBLE);
            tvCodIndicator.setVisibility(View.VISIBLE);
        }
    }

    private void setImagesViewPagerAdapter(List<String> productImages) {
        productDetailsImagesAdapter = new ProductDetailsImagesAdapter(productImages);
        productDetailsImagesViewPager.setAdapter(productDetailsImagesAdapter);
        viewPagerIndicator.setupWithViewPager(productDetailsImagesViewPager, true);
    }

    private void setRewardsAdapter() {
        linearLayoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rewardsAdapter = new RewardsAdapter(rewardsItemModelList, true);
        coupensRecyclerView.setLayoutManager(linearLayoutManager);
        coupensRecyclerView.setAdapter(rewardsAdapter);
    }

    private void setRewardsItemModelList() {
        rewardsItemModelList = new ArrayList<>();
        rewardsItemModelList.add(new RewardsItemModel("Rewards", "till 3rd, August 2018", "GET 20% OFF on any product above Rs.500/- and below Rs.2500/-"));
        rewardsItemModelList.add(new RewardsItemModel("Rewards", "till 3rd, August 2018", "GET 20% OFF on any product above Rs.500/- and below Rs.2500/-"));
        rewardsItemModelList.add(new RewardsItemModel("Rewards", "till 3rd, August 2018", "GET 20% OFF on any product above Rs.500/- and below Rs.2500/-"));
        rewardsItemModelList.add(new RewardsItemModel("Coupens", "till 3rd, August 2018", "GET 20% OFF on any product above Rs.500/- and below Rs.2500/-"));
        rewardsItemModelList.add(new RewardsItemModel("Rewards", "till 3rd, August 2018", "GET 20% OFF on any product above Rs.500/- and below Rs.2500/-"));
        rewardsItemModelList.add(new RewardsItemModel("Rewards", "till 3rd, August 2018", "GET 20% OFF on any product above Rs.500/- and below Rs.2500/-"));

        setRewardsAdapter();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.floatingActionButton) {
            if (currentUser == null) {
                startSignDialog(this);
            } else {
                if (ALREADY_ADDED_TO_WISHLIST) {
                    ALREADY_ADDED_TO_WISHLIST = false;
                    addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
                } else {
                    Map<String , Object> addProduct = new HashMap<>();
                    addProduct.put("product_ID_" + wishList.size() , productId);
                    UserDao.addUserWishList(currentUser.getUid(), addProduct, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                Map<String , Object> updateUserWishList = new HashMap<>();
                                updateUserWishList.put("list_size" , wishList.size() + 1 );

                                UserDao.updateUserWishList(currentUser.getUid(), updateUserWishList , new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()){
                                            ALREADY_ADDED_TO_WISHLIST = true;
                                            addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                                            Toast.makeText(ProductDetailsActivity.this, "Added to WishList Successfully!", Toast.LENGTH_SHORT).show();
                                            wishList.add(productId);
                                        }else {
                                            Toast.makeText(ProductDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                Toast.makeText(ProductDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }else if (view.getId() == R.id.coupen_redemption_btn) {
            showCoupenRedeemDialog();
        }else if (view.getId() == R.id.add_to_cart_btn){
            if (currentUser == null){
                startSignDialog(this);
            }else {
                /// todo: add to cart
            }
        }else if (view.getId() == R.id.buy_now_btn){
            buyNowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser == null){
                        startSignDialog(ProductDetailsActivity.this);
                    }else {
                        startActivity(new Intent(ProductDetailsActivity.this, DeliveryActivity.class));
                    }
                }
            });
        }
    }

    private void showCoupenRedeemDialog() {
        Dialog checkCoupenPriceDialog = new Dialog(this);
        checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
        checkCoupenPriceDialog.setCancelable(true);
        checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        toggleRecyclerView = checkCoupenPriceDialog.findViewById(R.id.toggle_recyclerView);
        selectedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen);
        discountedPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);
        originalPrice = checkCoupenPriceDialog.findViewById(R.id.original_price);
        coupensRecyclerView = checkCoupenPriceDialog.findViewById(R.id.coupens_recyclerView);
        coupenTitle = checkCoupenPriceDialog.findViewById(R.id.coupen_title);
        coupenBody = checkCoupenPriceDialog.findViewById(R.id.coupen_body);
        coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.coupen_expiry_date);

        setRewardsItemModelList();

        toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRecyclerView();
            }
        });

        checkCoupenPriceDialog.show();

    }


}