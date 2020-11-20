package com.example.mymall.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymall.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static com.example.mymall.ui.HomeFragment.clearData;
import static com.example.mymall.ui.RegisterActivity.setSignUpFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT = 1;
    private static final int ORDER_FRAGMENT = 2;
    private static final int WISHLIST_FRAGMENT = 3;
    private static final int REWARDS_FRAGMENT = 4;
    private static final int ACCOUNT_FRAGMENT = 5;

    public static Activity mainActivity;
    public static boolean resetMainActivity = false;
    public static MenuItem cartItem;
    public static Boolean showCart = false;
    private int scrollFlags;
    private AppBarLayout.LayoutParams params;

    private int currentFragment = -1;

    private Window window;
    public static DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView title;

    private FirebaseUser currentUser;
    private TextView badgeCount ;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setAppBarLayoutWindow();
        setScrollFlags();

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        if (showCart) {
            mainActivity = this;
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            startFragment("My Cart", new MyCartFragment(), -2);
        } else {
            setDrawer();
            setFragment(new HomeFragment(), HOME_FRAGMENT);
        }
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_my_cart, R.id.nav_my_orders, R.id.nav_my_rewards,R.id.nav_my_wishlist,R.id.nav_my_account)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
    }

    //////important : scroll to hide app bar or not
    private void setScrollFlags() {
        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags = params.getScrollFlags();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null){
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        }else {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);
        }
        if (resetMainActivity){
            resetMainActivity = false;
            setFragment(new HomeFragment(), HOME_FRAGMENT);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
        invalidateOptionsMenu();
    }

    //set abb Bar layout for Rewards Fragment
    private void setAppBarLayoutWindow() {
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        title = findViewById(R.id.my_mall_title);
        title.setVisibility(View.VISIBLE);
    }

    private void setDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);

            //search for cart icon in menuItem and replace it with badge layout
            cartItem = menu.findItem(R.id.main_cart_icon);

            cartItem.setActionView(R.layout.badge_layout);
            badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);

            if (currentUser != null){
                if (ProductDetailsActivity.cartList.size() == 0){
                    ProductDetailsActivity.loadUserCartList(MainActivity.this,false , badgeCount , new TextView(MainActivity.this));
                }else {
                    badgeCount.setVisibility(View.VISIBLE);
                    if (ProductDetailsActivity.cartList.size() < 99) {
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
//                        if (currentUser == null){
//                            startSignDialog(v.getContext());
//                        }else {
//                            startFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
//                        }
                    onOptionsItemSelected(cartItem);
                }
            });

        }
        return true;
    }

    private void setupBadge() {
        if (badgeCount != null) {
            if (ProductDetailsActivity.cartList.size() == 0) {
                if (badgeCount.getVisibility() != View.GONE) {
                    badgeCount.setVisibility(View.GONE);
                }
            } else {
                badgeCount.setText(String.valueOf(Math.min(ProductDetailsActivity.cartList.size(), 99)));
                if (badgeCount.getVisibility() != View.VISIBLE) {
                    badgeCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.main_search_icon) {
            //todo: search

            return true;
        } else if (id == R.id.main_notification_icon) {
            //todo: notification

            return true;

        } else if (id == R.id.main_cart_icon) {
            if (currentUser == null){
                startSignDialog(this);
            }else {
                startFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
            }
            return true;
        } else if (id == android.R.id.home) {
            if (showCart) {
                mainActivity = null;
                showCart = false;
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public static void startSignDialog(final Context context) {
        final Dialog signInDialog = new Dialog(context);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);

        final Intent registerIntent = new Intent(context, RegisterActivity.class);

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                context.startActivity(registerIntent);
            }
        });

        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                context.startActivity(registerIntent);
            }
        });
        signInDialog.show();
    }

    MenuItem menuItem;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawer.closeDrawers();
        drawer.closeDrawer(GravityCompat.START);
        menuItem = item;
        if (currentUser != null) {
            drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    int id = menuItem.getItemId();
                    if (id == R.id.nav_my_mall) {
                        invalidateOptionsMenu();

                        title.setVisibility(View.VISIBLE);
                        setFragment(new HomeFragment(), HOME_FRAGMENT);
                    } else if (id == R.id.nav_my_orders) {
                        startFragment("My Order", new MyOrdersFragment(), ORDER_FRAGMENT);
                    } else if (id == R.id.main_cart_icon) {
                        startFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                    } else if (id == R.id.nav_my_rewards) {
                        startFragment("My Rewards", new MyRewardsFragment(), REWARDS_FRAGMENT);
                    } else if (id == R.id.nav_my_wishlist) {
                        startFragment("My Wishlist", new MyWishListFragment(), WISHLIST_FRAGMENT);
                    } else if (id == R.id.nav_my_account) {
                        startFragment("My Account", new MyAccountFragment(), ACCOUNT_FRAGMENT);
                    } else if (id == R.id.nav_sign_out) {
                        FirebaseAuth.getInstance().signOut();
                        clearData();
                        startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                        finish();
                    }
                }
            });
            return true;
        }else {
            item.setChecked(false);
            startSignDialog(this);
            return false;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment == HOME_FRAGMENT) {
                currentFragment = -1;
                super.onBackPressed();
            } else {
                if (showCart) {
                    mainActivity = null;
                    showCart = false;
                    finish();
                } else {
                    invalidateOptionsMenu();
                    title.setVisibility(View.VISIBLE);
                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startFragment(String fragmentTitle, Fragment fragment, int fragmentNo) {
        invalidateOptionsMenu();
        setFragment(fragment, fragmentNo);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(fragmentTitle);
        title.setVisibility(View.GONE);
        if (fragmentNo == CART_FRAGMENT || showCart) {
            navigationView.getMenu().getItem(3).setChecked(true);
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL);
        }else {
            params.setScrollFlags(scrollFlags);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setFragment(Fragment fragment, int fragmentNo) {
        if (fragmentNo != currentFragment) {
            if (fragmentNo == REWARDS_FRAGMENT) {
                window.setStatusBarColor(Color.parseColor("#5B04B1"));
                toolbar.setBackgroundColor(Color.parseColor("#5B04B1"));
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.main_frame_layout, fragment);
            fragmentTransaction.commit();
        }
    }


    //
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }


}