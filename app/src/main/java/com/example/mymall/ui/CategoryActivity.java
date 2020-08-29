package com.example.mymall.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mymall.R;
import com.example.mymall.adapters.HomePageAdapter;
import com.example.mymall.models.HomePageModel;
import com.example.mymall.models.HorizontalProductScrollModel;
import com.example.mymall.models.SliderModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView categoryRecyclerView;

    private List<SliderModel> sliderModelList;
    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    //////////////Category Recycler View
    private LinearLayoutManager categoryLayoutManager;
    private HomePageAdapter categoryPageAdapter;
    private List<HomePageModel> categoryPageModelList;

    //////////////Category Recycler View
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        String categoryName = getIntent().getStringExtra("CategoryName");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(categoryName);

        initHomePageRecyclerView();
        setCategoryPageModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search_icon) {
            //todo: search

            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setCategoryPageModel() {
        setHorizontalProductModel();
        setSliderModel();
        categoryPageModelList = new ArrayList<>();
        /*categoryPageModelList.add(new HomePageModel(0,sliderModelList));
        categoryPageModelList.add(new HomePageModel(1,R.drawable.strip_ad,"#000000"));
        categoryPageModelList.add(new HomePageModel(0,sliderModelList));
        categoryPageModelList.add(new HomePageModel(0,sliderModelList));
        categoryPageModelList.add(new HomePageModel(1,R.drawable.strip_ad,"#000000"));
        categoryPageModelList.add(new HomePageModel(0,sliderModelList));
        categoryPageModelList.add(new HomePageModel(0,sliderModelList));
        categoryPageModelList.add(new HomePageModel(1,R.drawable.strip_ad,"#000000"));
        categoryPageModelList.add(new HomePageModel(0,sliderModelList));
        categoryPageModelList.add(new HomePageModel(1,R.drawable.strip_ad,"#000000"));
        categoryPageModelList.add(new HomePageModel(0,sliderModelList));
        categoryPageModelList.add(new HomePageModel(1,R.drawable.strip_ad,"#000000"));
        categoryPageModelList.add(new HomePageModel(2,"Deals of the Day!!!",horizontalProductScrollModelList));
        categoryPageModelList.add(new HomePageModel(2,"Deals of the Day!!!",horizontalProductScrollModelList));
        categoryPageModelList.add(new HomePageModel(2,"Deals of the Day!!!",horizontalProductScrollModelList));
        categoryPageModelList.add(new HomePageModel(2,"Deals of the Day!!!",horizontalProductScrollModelList));
        categoryPageModelList.add(new HomePageModel(3,"Monday Deals!!!",horizontalProductScrollModelList));
        categoryPageModelList.add(new HomePageModel(3,"Monday Deals!!!",horizontalProductScrollModelList));
        categoryPageModelList.add(new HomePageModel(3,"Monday Deals!!!",horizontalProductScrollModelList));
        categoryPageModelList.add(new HomePageModel(3,"Monday Deals!!!",horizontalProductScrollModelList));*/

        setCategoryPageAdapter();
    }

    private void setCategoryPageAdapter() {
        HomePageAdapter categoryPageAdapter = new HomePageAdapter(categoryPageModelList);
        categoryRecyclerView.setAdapter(categoryPageAdapter);
        categoryPageAdapter.notifyDataSetChanged();
    }

    private void initHomePageRecyclerView() {
        categoryRecyclerView = findViewById(R.id.category_activity_recycler_view);
        categoryLayoutManager = new LinearLayoutManager(this);
        categoryLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(categoryLayoutManager);
    }

    private void setHorizontalProductModel() {
        horizontalProductScrollModelList = new ArrayList<>();
        /*horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.profile_placeholder,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.forgot_password_image,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.banner,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.strip_ad,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_store_mall_directory,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_menu_share,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_mail_green,"Redmi A","SD 625 Processor","Rs.5999/-"));
*/
    }

    private void setSliderModel() {
        sliderModelList = new ArrayList<>();
        /*sliderModelList.add(new SliderModel(R.drawable.forgot_password_image,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.my_account,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.common_google_signin_btn_icon_dark_focused,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_menu_manage,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.banner,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.forgot_password_image,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.common_google_signin_btn_icon_disabled,"#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.my_wishlist,"#077AE4"));*/
    }
}