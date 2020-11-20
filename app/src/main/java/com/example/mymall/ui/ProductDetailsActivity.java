package com.example.mymall.ui;

import android.app.Dialog;
import android.content.Context;
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
import com.example.mymall.database.MyDatabase;
import com.example.mymall.database.ProductsDao;
import com.example.mymall.database.UserDao;
import com.example.mymall.models.CartItemModel;
import com.example.mymall.models.ProductSpecificationModel;
import com.example.mymall.models.RewardsItemModel;
import com.example.mymall.models.WishListItemModel;
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
import static com.example.mymall.database.UserDao.myRatedIds;
import static com.example.mymall.database.UserDao.myRating;
import static com.example.mymall.ui.AddAddressActivity.addressesModelList;
import static com.example.mymall.ui.MainActivity.cartItem;
import static com.example.mymall.ui.MainActivity.showCart;
import static com.example.mymall.ui.MainActivity.startSignDialog;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static boolean ALREADY_ADDED_TO_CARTLIST = false;
    public static boolean RUNNING_RATING_QUERY = false;
    public static boolean RUNNING_WISHLIST_QUERY = false;
    public static boolean RUNNING_CART_QUERY = false;
    public static int initialRating;
    public static ProductDetailsActivity productDetailsActivity;

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
    public static FloatingActionButton addToWishListBtn;

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
    public static LinearLayout starRatingContainer;
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

    public static FirebaseUser currentUser;
    public static String productId;
    public static Dialog loadingDialog;
    private DocumentSnapshot documentSnapshot;
    private TextView badgeCount ;

    public static List<String> wishList = new ArrayList<>();
    public static List<WishListItemModel> wishListModelList = new ArrayList<>();

    public static List<String> cartList = new ArrayList<>();
    public static List<CartItemModel> cartItemModelList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialRating = -1;
        initView();
        showProgressDialog(this);
        setProductDetailsData();
        selectStarToRateProduct();

        loadUserRatings(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            coupenRedemptionLayout.setVisibility(View.GONE);
        }else {
            coupenRedemptionLayout.setVisibility(View.VISIBLE);
        }

        if (currentUser != null) {
            if (wishList.size() == 0){
                loadUserWishList(ProductDetailsActivity.this,false);
            }
            if (myRatedIds.size() == 0) {
                loadUserRatings(this);
            }
//            if (cartList.size() == 0){
//                loadUserCartList(ProductDetailsActivity.this,false);
//            }
            invalidateOptionsMenu();
        }

        if (myRatedIds.contains(productId)){
            int index = myRatedIds.indexOf(productId);
            //rating value but as index forExample: He rates 3 stars so initialValue = 2;
            //myRating.get(index) = get the rate of product in index (index);
            initialRating = Integer.parseInt(String.valueOf(myRating.get(index))) - 1;
            setRating(initialRating);
        }

        if (wishList.contains(productId)){
            ALREADY_ADDED_TO_WISHLIST = true;
            addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
        }else {
            ALREADY_ADDED_TO_WISHLIST = false;
        }
        ALREADY_ADDED_TO_CARTLIST = cartList.contains(productId);
        loadingDialog.dismiss();
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void showProgressDialog(Context context){
        loadingDialog = new Dialog(context);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
    }
    private void selectStarToRateProduct() {
        for (int i = 0; i < starRatingContainer.getChildCount(); i++) {
            final int starPosition = i;
            starRatingContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    if (currentUser == null){
                        startSignDialog(ProductDetailsActivity.this);
                    }else {
                        if (starPosition != initialRating) {
                            if (!RUNNING_RATING_QUERY) {
                                RUNNING_RATING_QUERY = true;
                                setRating(starPosition);

                                rateNewProduct(starPosition);
                            }
                        }
                    }
                }
            });
        }
    }

    private void rateNewProduct(final int starPosition) {
        Map<String, Object> currentUserRating = new HashMap<>();
        if (myRatedIds.contains(productId)) {
            currentUserRating.put("rating_" + myRatedIds.indexOf(productId) , (long)starPosition + 1);
        }else {
            currentUserRating.put("list_size" , (long) myRatedIds.size() + 1);
            currentUserRating.put("product_ID_" + myRatedIds.size(), productId);
            currentUserRating.put("rating_" + myRatedIds.size(), (long) starPosition + 1);
        }
        UserDao.updateProductRatingListSize(FirebaseAuth.getInstance().getUid(), currentUserRating, new OnCompleteListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    changeProductTotalAndAverageRating(starPosition);
                } else {
                    setRating(initialRating);
                    Toast.makeText(ProductDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                RUNNING_RATING_QUERY = false;
            }
        });
    }

    private void changeProductTotalAndAverageRating(final int starPosition) {
        Map<String , Object> updateRating = new HashMap<>();
        if (myRatedIds.contains(productId)) {

            TextView oldRatings = (TextView) ratingNoContainer.getChildAt(5 - initialRating - 1);
            TextView newRatings = (TextView) ratingNoContainer.getChildAt(5 - starPosition - 1);

//            updateRating.put(initialRating + 1 + "_star" , (long)documentSnapshot.get(initialRating + 1 + "_star") - 1);
            updateRating.put(initialRating + 1 + "_star" , Long.parseLong(oldRatings.getText().toString()) - 1);
            updateRating.put(starPosition + 1 + "_star" , Long.parseLong(newRatings.getText().toString()) + 1);
            updateRating.put("average_rating" , calculateAvgRating(starPosition - initialRating , true));
        } else {
            updateRating.put(starPosition + 1 + "_star" , (long) documentSnapshot.get(starPosition + 1 + "_star")+1);
            updateRating.put("average_rating", calculateAvgRating(starPosition + 1 , false));
            updateRating.put("total_ratings" , (long) documentSnapshot.get("total_ratings") + 1);
        }
        ProductsDao.updateProductRating(productId, updateRating, new OnCompleteListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    if (myRatedIds.contains(productId)) {
                        //update product rating value in list myRating
                        myRating.set(myRatedIds.indexOf(productId) , (long) starPosition + 1);
                        TextView oldRatings = (TextView) ratingNoContainer.getChildAt(5 - initialRating - 1);
                        TextView newRatings = (TextView) ratingNoContainer.getChildAt(5 - starPosition - 1);

                        oldRatings.setText(String.valueOf(Integer.parseInt(oldRatings.getText().toString()) - 1));
                        newRatings.setText(String.valueOf(Integer.parseInt(newRatings.getText().toString()) + 1));
                    } else {
                        myRatedIds.add(productId);
                        myRating.add((long) (starPosition + 1));

                        TextView ratings = (TextView) ratingNoContainer.getChildAt(5 - starPosition - 1);

                        ratings.setText(String.valueOf(Integer.parseInt(ratings.getText().toString()) + 1));

                        totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));
                        totalRatings.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");
                        totalRatingMiniView.setText("(" + documentSnapshot.get("total_ratings").toString() + 1 + ") ratings");

                        Toast.makeText(ProductDetailsActivity.this, "Thanks for rating!", Toast.LENGTH_SHORT).show();
                    }
                    for (int i = 0; i < 5; i++) {
                        TextView ratingFigures = (TextView) ratingNoContainer.getChildAt(i);
                        ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(i);
//                           progressBar.setMax(Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings") + 1)));
                        progressBar.setMax(Integer.parseInt(totalRatingsFigure.getText().toString()));
                        progressBar.setProgress(Integer.parseInt(ratingFigures.getText().toString()));
                    }
                    initialRating = starPosition;
                    averageRating.setText(calculateAvgRating(0 , true));
                    averageRatingMiniView.setText(calculateAvgRating(0, true));

                    if (wishList.contains(productId) && wishListModelList.size() != 0){
                        int index = wishList.indexOf(productId);
                        WishListItemModel changeRating = wishListModelList.get(index);
                        changeRating.setRating(averageRating.getText().toString());
                        changeRating.setTotalRating(Long.parseLong(totalRatingsFigure.getText().toString()));
                    }
                }else {
                    RUNNING_RATING_QUERY = false;
                    setRating(initialRating);
                    Toast.makeText(ProductDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setRating(int starPosition) {
        for (int i = 0; i < starRatingContainer.getChildCount(); i++) {
            ImageView star = (ImageView) starRatingContainer.getChildAt(i);
            star.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (i <= starPosition) {
                star.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }

    public static void  loadUserRatings(final Context context){
        if (!RUNNING_RATING_QUERY) {
            RUNNING_RATING_QUERY = true;
            UserDao.loadRatingList(FirebaseAuth.getInstance().getUid(), new OnCompleteListener<DocumentSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        myRatedIds.clear();
                        myRating.clear();

                        for (long i = 0; i < (long) task.getResult().get("list_size"); i++) {
                            myRatedIds.add(task.getResult().get("product_ID_" + i).toString());
                            myRating.add((Long) task.getResult().get("rating_" + i));

                            if (task.getResult().get("product_ID_" + i).toString().equals(ProductDetailsActivity.productId)) {
                                initialRating = Integer.parseInt(String.valueOf((Long) task.getResult().get("rating_" + i))) - 1;
                                if (starRatingContainer != null) {
                                    ProductDetailsActivity.setRating(initialRating);
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    RUNNING_RATING_QUERY = false;
                }
            });
        }
    }

    private String calculateAvgRating(long currentUserRating , boolean update){
        Double totalStars = Double.valueOf(0);
        //calculate total stars for the product of all users
        for (int i = 1 ; i < 6 ; i++){
//            totalStars += ((long)documentSnapshot.get(i + "_star") * i);
            TextView ratingNo = (TextView) ratingNoContainer.getChildAt(5 - i);
            totalStars += (Long.parseLong(ratingNo.getText().toString()) * i);
        }
        totalStars += currentUserRating;
        if (update) {
//            return totalStars / ((long) documentSnapshot.get("total_ratings") + 1);
            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0 , 3);
        }else {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingsFigure.getText().toString()) + 1)).substring(0 , 3);
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

        cartItem = menu.findItem(R.id.main_cart_icon);
        cartItem.setActionView(R.layout.badge_layout);
        badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);
        if (currentUser != null){
            if (cartList.size() == 0){
                loadUserCartList(ProductDetailsActivity.this,false , badgeCount , new TextView(ProductDetailsActivity.this));
            }else {
                badgeCount.setVisibility(View.VISIBLE);
                if (cartList.size() < 99) {
                    badgeCount.setText(String.valueOf(ProductDetailsActivity.cartList.size()));
                }else {
                    badgeCount.setText("99");
                }
            }
        }
        ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
        badgeIcon.setImageResource(R.drawable.ic_shopping_cart);

        cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                showCart = true;
                startActivity(cartIntent);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            productDetailsActivity = null;
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
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();
                    setProductDetailsImagesLayout(documentSnapshot);

                    setRewardWithProductLayout(documentSnapshot);
                    setProductDescriptionLayout(documentSnapshot);
                    setProductRatingLayout(documentSnapshot);

                    if (currentUser != null) {
                        if (myRating.size() == 0){
                            loadUserRatings(ProductDetailsActivity.this);
                        }
                        if (wishList.size() == 0){
                            loadUserWishList(ProductDetailsActivity.this,false);
                        }
                        if (cartList.size() == 0){
                            loadUserCartList(ProductDetailsActivity.this,false , badgeCount , new TextView(ProductDetailsActivity.this));
                        }
                        if (myRatedIds.contains(productId)){
                            int index = myRatedIds.indexOf(productId);
                            initialRating = Integer.parseInt(String.valueOf(myRating.get(index))) - 1;
                            setRating(initialRating);
                        }
                        if (wishList.contains(productId)){
                            ALREADY_ADDED_TO_WISHLIST = true;
                            addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                        }else {
                            ALREADY_ADDED_TO_WISHLIST = false;
                        }
                        ALREADY_ADDED_TO_CARTLIST = cartList.contains(productId);

                        if(!((boolean)documentSnapshot.get("in_stock"))){
                            buyNowBtn.setVisibility(View.GONE);
                            TextView outOfStock = (TextView) addToCartBtn.getChildAt(0);
                            outOfStock.setText("Out Of Stock");
                            outOfStock.setTextColor(getResources().getColor(R.color.colorPrimary));
                            outOfStock.setCompoundDrawables(null , null , null , null);
                        }
                    }

                    loadingDialog.dismiss();
                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(ProductDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public static void loadUserWishList(final Context context , final boolean loadProductData) {
        wishList.clear();
        UserDao.loadUserWishList(FirebaseAuth.getInstance().getUid(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().get("list_size") != null) {
                        for (long i = 0; i < (long) task.getResult().get("list_size"); i++) {

                            wishList.add(task.getResult().get("product_ID_" + i).toString());

                            if (wishList.contains(productId)) {
                                ALREADY_ADDED_TO_WISHLIST = true;
                                if (addToWishListBtn != null) {
                                    addToWishListBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                                }
                            } else {
                                if (addToWishListBtn != null) {
                                    addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
                                }
                                ALREADY_ADDED_TO_WISHLIST = false;
                            }

                            ////set MyWishList Fragment Recycler view
                            if (loadProductData) {
                                setWishlistFragmentRecyclerView(context, task, i);
                            }
                            ////set MyWishList Fragment Recycler view
                        }
                    }
                }else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static void setWishlistFragmentRecyclerView(final Context context , @NonNull Task<DocumentSnapshot> task , long position) {
        wishListModelList.clear();
        String product_id = task.getResult().get("product_ID_" + position).toString();
        ProductsDao.getProducts(product_id, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot product = task.getResult();
                    wishListModelList.add(new WishListItemModel(productId, product.get("product_image_1").toString()
                            , product.get("product_title").toString()
                            , (long) product.get("free_coupens")
                            , product.get("average_rating").toString()
                            , (long) product.get("total_ratings")
                            , product.get("product_price").toString()
                            , product.get("cutted_price").toString()
                            , (boolean) product.get("COD")
                            , (boolean)product.get("in_stock")));
                    MyWishListFragment.wishlistAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void loadUserCartList(final Context context , final boolean loadProductData , final TextView badgeCount , final TextView totalCartAmount) {
        cartList.clear();
        UserDao.loadUserCartList(FirebaseAuth.getInstance().getUid(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().get("list_size") != null) {
                        for (long i = 0; i < (long) task.getResult().get("list_size"); i++) {

                            cartList.add(task.getResult().get("product_ID_" + i).toString());

                            ALREADY_ADDED_TO_CARTLIST = cartList.contains(productId);
                            ////set MyCartList Fragment Recycler view
                            if (loadProductData) {
                                setCartListFragmentRecyclerView(context, task, i , totalCartAmount);
                            }
                            ////set MyCartList Fragment Recycler view
                        }
                    }
                    if (cartList.size() != 0){
                        badgeCount.setVisibility(View.VISIBLE);
                    }else {
                        badgeCount.setVisibility(View.INVISIBLE);
                    }
                    if (cartList.size() < 99) {
                        badgeCount.setText(String.valueOf(ProductDetailsActivity.cartList.size()));
                    }else {
                        badgeCount.setText("99");
                    }
                }else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static void setCartListFragmentRecyclerView(final Context context, Task<DocumentSnapshot> task, long position , final TextView totalCartAmount) {
        cartItemModelList.clear();
        MyDatabase.getProductsReferences()
                .document(task.getResult().get("product_ID_" + position).toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot product = task.getResult();
                            int index = 0;
                            if (cartList.size() >= 2){
                                index = cartList.size() - 2;
                            }

                            cartItemModelList.add(index , new CartItemModel(CartItemModel.CART_ITEM , productId, product.get("product_image_1").toString()
                                    , product.get("product_title").toString()
                                    , (long) product.get("free_coupens")
                                    , product.get("product_price").toString()
                                    , product.get("cutted_price").toString()
                                    , (long) 1
                                    , (long) 0
                                    , (long) 0
                                    , (boolean) product.get("in_stock")
                                    , (Long) product.get("max-quantity")
                                    , (Long) product.get("stock_quantity")));


//                            if (cartList.size() == 1){
                            if (cartList.size() > 0){
//                                cartItemModelList.add(new CartItemModel(CartItemModel.CART_TOTAL_AMOUNT));
                                cartItemModelList.add(cartItemModelList.size(),new CartItemModel(CartItemModel.CART_TOTAL_AMOUNT));
                                LinearLayout parent = (LinearLayout) totalCartAmount.getParent().getParent();
                                parent.setVisibility(View.VISIBLE);
                            }
                            if (cartList.size() == 0){
                                cartItemModelList.clear();
                            }
                            MyCartFragment.cartAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void removeProductFromWishlist(final int index , final Context context){
        final String removedProductId = wishList.get(index);
        wishList.remove(index);
        Map<String , Object> updateWishlist = new HashMap<>();
        for (int i = 0 ; i < wishList.size() ; i++){
            updateWishlist.put("product_ID_" + i , wishList.get(i));
        }
        updateWishlist.put("list_size" , (long)wishList.size());
        UserDao.addUserWishList(currentUser.getUid(), updateWishlist, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    if (wishListModelList.size() != 0) {
                        wishListModelList.remove(index);
                        MyWishListFragment.wishlistAdapter.notifyDataSetChanged();
                    }
                    RUNNING_WISHLIST_QUERY = false;
                    ALREADY_ADDED_TO_WISHLIST = false;
                    Toast.makeText(context, "Removed Successfully!", Toast.LENGTH_SHORT).show();
                }else {
                    RUNNING_WISHLIST_QUERY = false;
                    if (addToWishListBtn != null) {
                        addToWishListBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                    }
//                    wishList.add(index , removedProductId);
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                RUNNING_WISHLIST_QUERY = false;
            }
        });
    }
    public static void removeProductFromCart(final int index , final Context context , final TextView cartTotalAmount){
        final String removedProductId = cartList.get(index);
        cartList.remove(index);
        Map<String , Object> updateCartList = new HashMap<>();
        for (int i = 0 ; i < cartList.size() ; i++){
            updateCartList.put("product_ID_" + i , cartList.get(i));
        }
        updateCartList.put("list_size" , (long)cartList.size());
        UserDao.addUserCartList(FirebaseAuth.getInstance().getUid(), updateCartList, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    if (cartItemModelList.size() != 0) {
                        cartItemModelList.remove(index);
                        MyCartFragment.cartAdapter.notifyDataSetChanged();
                    }
                    if (cartList.size() == 0){
                        LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                        parent.setVisibility(View.GONE);
                        cartItemModelList.clear();
                    }
                    Toast.makeText(context, "Removed Successfully!", Toast.LENGTH_SHORT).show();
                }else {
//                    cartList.add(index , removedProductId);
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                RUNNING_CART_QUERY = false;
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
        averageRating.setText(documentSnapshot.get("average_rating").toString());
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.floatingActionButton) {
            if (currentUser == null) {
                startSignDialog(this);
            } else {
                if (!RUNNING_WISHLIST_QUERY) {
                    RUNNING_WISHLIST_QUERY = true;
                    if (ALREADY_ADDED_TO_WISHLIST) {
                        int index = wishList.indexOf(productId);
                        removeProductFromWishlist(index, ProductDetailsActivity.this);
                        if (addToWishListBtn != null) {
                            addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
                        }
                    } else {
                        addProductToWhishlistFirebase();
                    }
                RUNNING_WISHLIST_QUERY = false;
                }
            }
        }else if (view.getId() == R.id.coupen_redemption_btn) {
            showCoupenRedeemDialog();
        }else if (view.getId() == R.id.add_to_cart_btn){
            if ((boolean)documentSnapshot.get("in_stock")) {
                if (currentUser == null) {
                    startSignDialog(this);
                } else {
                    if (!RUNNING_CART_QUERY) {
                        RUNNING_CART_QUERY = true;
                        if (ALREADY_ADDED_TO_CARTLIST) {
                            RUNNING_CART_QUERY = false;
                            Toast.makeText(this, "Already added to cart!", Toast.LENGTH_SHORT).show();
                        } else {
                            addProductToCartListFirebase();
                        }
                    }
                }
            }
        }else if (view.getId() == R.id.buy_now_btn){
            if (currentUser == null){
                loadingDialog.dismiss();
                startSignDialog(ProductDetailsActivity.this);
            }else {
                DeliveryActivity.fromCart = false;
                showProgressDialog(ProductDetailsActivity.this);
                productDetailsActivity = ProductDetailsActivity.this;
                DeliveryActivity.cartItemModelList = new ArrayList<>();
//                        DeliveryActivity.cartItemModelList.clear();
                DeliveryActivity.cartItemModelList.add( new CartItemModel(CartItemModel.CART_ITEM , productId, documentSnapshot.get("product_image_1").toString()
                        , documentSnapshot.get("product_title").toString()
                        , (long) documentSnapshot.get("free_coupens")
                        , documentSnapshot.get("product_price").toString()
                        , documentSnapshot.get("cutted_price").toString()
                        , (long) 1
                        , (long) 0
                        , (long) 0
                        , (boolean) documentSnapshot.get("in_stock")
                        , (Long) documentSnapshot.get("max-quantity")
                        , (Long) documentSnapshot.get("stock_quantity")));
//                        DeliveryActivity.cartItemModelList.add(DeliveryActivity.cartItemModelList.size(),new CartItemModel(CartItemModel.CART_TOTAL_AMOUNT));
                DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.CART_TOTAL_AMOUNT));

                if (addressesModelList.size() == 0){
                    MyCartFragment.loadAddresses(ProductDetailsActivity.this);
                }else {
                    loadingDialog.dismiss();
                    startActivity(new Intent(ProductDetailsActivity.this , DeliveryActivity.class));
                }
            }
        }
    }

    private void addProductToWhishlistFirebase() {
        Map<String , Object> addProduct = new HashMap<>();
        addProduct.put("product_ID_" + wishList.size() , productId);
        addProduct.put("list_size" , wishList.size() + 1 );

        UserDao.updateUserWishList(currentUser.getUid(), addProduct, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    updateUserWishListSize();
                }else {
                    RUNNING_WISHLIST_QUERY = false;
                    addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
                    Toast.makeText(ProductDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUserWishListSize() {
        if (wishListModelList.size() != 0){
            addProductToWhishlistModelList();
        }
        ALREADY_ADDED_TO_WISHLIST = true;
        addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
        Toast.makeText(ProductDetailsActivity.this, "Added to WishList Successfully!", Toast.LENGTH_SHORT).show();
        wishList.add(productId);
        RUNNING_WISHLIST_QUERY = false;
    }

    private void addProductToWhishlistModelList() {
        wishListModelList.add(new WishListItemModel(productId
                , documentSnapshot.get("product_image_1").toString()
                , documentSnapshot.get("product_title").toString()
                , (long) documentSnapshot.get("free_coupens")
                , documentSnapshot.get("average_rating").toString()
                , (long) documentSnapshot.get("total_ratings")
                , documentSnapshot.get("product_price").toString()
                , documentSnapshot.get("cutted_price").toString()
                , (boolean) documentSnapshot.get("COD")
                , (boolean) documentSnapshot.get("in_stock")));
    }
    private void addProductToCartListFirebase() {
        Map<String , Object> addProduct = new HashMap<>();
        addProduct.put("product_ID_" + cartList.size() , productId);
        addProduct.put("list_size" , cartList.size() + 1 );
        UserDao.updateUserCartList(currentUser.getUid(), addProduct, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    updateUserCartListSize();
                }else {
                    RUNNING_CART_QUERY = false;
                    Toast.makeText(ProductDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateUserCartListSize() {
        if (cartItemModelList.size() != 0){
            addProductToCartListModelList();
        }
        ALREADY_ADDED_TO_CARTLIST = true;
        Toast.makeText(ProductDetailsActivity.this, "Added to Cart Successfully!", Toast.LENGTH_SHORT).show();
        invalidateOptionsMenu();
        cartList.add(productId);
        RUNNING_CART_QUERY = false;
    }


    private void addProductToCartListModelList() {
        ProductDetailsActivity.cartItemModelList.add(0,new CartItemModel(CartItemModel.CART_ITEM , productId, documentSnapshot.get("product_image_1").toString()
                , documentSnapshot.get("product_title").toString()
                , (long) documentSnapshot.get("free_coupens")
                , documentSnapshot.get("product_price").toString()
                , documentSnapshot.get("cutted_price").toString()
                , (long) 1
                , (long) 0
                , (long) 0
                , (boolean)documentSnapshot.get("in_stock")
                , (Long) documentSnapshot.get("max-quantity")
                , (Long) documentSnapshot.get("stock_quantity")));
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

    @Override
    public void onBackPressed() {
        productDetailsActivity = null;
        super.onBackPressed();
    }
}