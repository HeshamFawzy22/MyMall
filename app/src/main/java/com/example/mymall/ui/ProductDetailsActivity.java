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
import android.widget.TextView;

import com.example.mymall.R;
import com.example.mymall.adapters.ProductDetailsImagesAdapter;
import com.example.mymall.adapters.ProductDetailsViewPagerAdapter;
import com.example.mymall.adapters.RewardsAdapter;
import com.example.mymall.models.RewardsItemModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import static com.example.mymall.ui.MainActivity.showCart;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    //Coupen Redeem Dialog item
    public static TextView coupenTitle;
    public static TextView coupenExpiryDate;
    public static TextView coupenBody;
    private static LinearLayoutManager linearLayoutManager;
    private static LinearLayout selectedCoupen;
    private static RecyclerView coupensRecyclerView;
    private static boolean ALREADY_ADDED_TO_WISHLIST = false;
    //ui
    protected ViewPager productDetailsImagesViewPager;
    protected TabLayout viewPagerIndicator;
    protected FloatingActionButton addToWishListBtn;
    protected ViewPager productDetailsViewPager;
    protected TabLayout productDetailsTabLayout;
    private Button buyNowBtn;
    private Button coupenRedeemBtn;
    ////////////////Declare Coupen Redeem Dialog
    private RewardsAdapter rewardsAdapter;
    private List<RewardsItemModel> rewardsItemModelList;
    //Coupen Redeem Dialog ui
    private ImageView toggleRecyclerView;
    private TextView discountedPrice;
    ////////////////Declare Coupen Redeem Dialog
    private TextView originalPrice;

    /////rating layout container
    /////rating layout container
    private LinearLayout starRatingContainer;
    //Declare
    private List<Integer> productImages;
    private ProductDetailsImagesAdapter productDetailsImagesAdapter;
    private ProductDetailsViewPagerAdapter productDetailsViewPagerAdapter;

    public static void showDialogRecyclerView() {
        if (coupensRecyclerView.getVisibility() == View.GONE) {
            coupensRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupen.setVisibility(View.GONE);
        } else {
            coupensRecyclerView.setVisibility(View.GONE);
            selectedCoupen.setVisibility(View.VISIBLE);
        }
    }

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

        setProductImages();
        setDetailsViewPagerAdapter();

        buyNowBtnToDeliveryActivity();

/*        productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTabLayout));
        productDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

    }

    private void buyNowBtnToDeliveryActivity() {
        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailsActivity.this, DeliveryActivity.class));
            }
        });
    }

    private void rateProduct() {
        for (int i = 0; i < starRatingContainer.getChildCount(); i++) {
            final int starPosition = i;
            starRatingContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    setRating(starPosition);
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
        productDetailsViewPagerAdapter = new ProductDetailsViewPagerAdapter(getSupportFragmentManager(), productDetailsTabLayout.getTabCount());
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
            Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
            showCart = true;
            startActivity(cartIntent);
            return true;
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
        addToWishListBtn = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        addToWishListBtn.setOnClickListener(ProductDetailsActivity.this);
        coupenRedeemBtn = findViewById(R.id.coupen_redemption_btn);
        coupenRedeemBtn.setOnClickListener(ProductDetailsActivity.this);

    }

    private void setProductImages() {
        productImages = new ArrayList<>();
        productImages.add(R.drawable.banner);
        productImages.add(R.drawable.forgot_password_image);
        productImages.add(R.drawable.ic_mail_green);
        productImages.add(R.drawable.ic_mail_red);

        setImagesViewPagerAdapter(productImages);
    }

    private void setImagesViewPagerAdapter(List<Integer> productImages) {
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
            if (ALREADY_ADDED_TO_WISHLIST) {
                ALREADY_ADDED_TO_WISHLIST = false;
                addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            } else {
                ALREADY_ADDED_TO_WISHLIST = true;
                addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
            }
        } else if (view.getId() == R.id.coupen_redemption_btn) {
            showCoupenRedeemDialog();
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