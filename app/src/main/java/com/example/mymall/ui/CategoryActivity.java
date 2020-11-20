package com.example.mymall.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mymall.R;
import com.example.mymall.adapters.HomePageAdapter;
import com.example.mymall.models.HomePageModel;
import com.example.mymall.models.HorizontalProductScrollModel;
import com.example.mymall.models.SliderModel;
import com.example.mymall.models.WishListItemModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.mymall.ui.HomeFragment.lists;
import static com.example.mymall.ui.HomeFragment.loadFragmentData;
import static com.example.mymall.ui.HomeFragment.loadedCategoriesNames;

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

    // Fake Lists
    private List<HomePageModel> homepageModelFakeList = new ArrayList<>();
    // Fake Lists

    private String categoryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryName = getIntent().getStringExtra("CategoryName");
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
        /*setHorizontalProductModel();
        setSliderModel();
        categoryPageModelList = new ArrayList<>();
        categoryPageModelList.add(new HomePageModel(0,sliderModelList));
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

        // homepageModelFakeList
        List<SliderModel> sliderModelFakeList = new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));

        List<HorizontalProductScrollModel>horizontalProductScrollFakeModel = new ArrayList<>();
        horizontalProductScrollFakeModel.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollFakeModel.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollFakeModel.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollFakeModel.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollFakeModel.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollFakeModel.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollFakeModel.add(new HorizontalProductScrollModel("","","","",""));

        homepageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homepageModelFakeList.add(new HomePageModel(1,"","#ffffff"));
        homepageModelFakeList.add(new HomePageModel(2,"","#ffffff",horizontalProductScrollFakeModel,new ArrayList<WishListItemModel>()));
        homepageModelFakeList.add(new HomePageModel(3,"","#ffffff",horizontalProductScrollFakeModel));
        // homepageModelFakeList

        categoryPageAdapter = new HomePageAdapter(homepageModelFakeList);

        loadSpecificFragmentData();
    }

    private void loadSpecificFragmentData() {
        int listPosition = 0;
        for (int i = 0 ; i < loadedCategoriesNames.size() ; i++){
            if (loadedCategoriesNames.get(i).equals(categoryName)){
                listPosition = i;
            }
        }
        if (listPosition == 0){
            loadedCategoriesNames.add(categoryName.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());
            //categoryPageAdapter = new HomePageAdapter(lists.get(loadedCategoriesNames.size() - 1));
            loadFragmentData(categoryRecyclerView,this,loadedCategoriesNames.size()-1,categoryName);
        }else {
            categoryPageAdapter = new HomePageAdapter(lists.get(listPosition));
        }

        categoryRecyclerView.setAdapter(categoryPageAdapter);
        categoryPageAdapter.notifyDataSetChanged();

        //setCategoryPageAdapter(listPosition);
    }

    private void setCategoryPageAdapter(int position) {
        //categoryPageAdapter = new HomePageAdapter(lists.get(position));
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