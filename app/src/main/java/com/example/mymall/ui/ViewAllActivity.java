package com.example.mymall.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.example.mymall.R;
import com.example.mymall.adapters.GridProductLayoutAdapter;
import com.example.mymall.adapters.WishListAdapter;
import com.example.mymall.models.HorizontalProductScrollModel;
import com.example.mymall.models.WishListItemModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.mymall.adapters.HomePageAdapter.GRID_VIEW_PRODUCT;
import static com.example.mymall.adapters.HomePageAdapter.RECYCLER_VIEW_PRODUCT;

public class ViewAllActivity extends AppCompatActivity {
    ///Very important static to get Data from HomePageAdapter
    public static List<HorizontalProductScrollModel> horizontalProductScrollModelList;
    public static List<WishListItemModel> wishListItemModelList;
    //ui
    protected Toolbar toolbar;
    protected RecyclerView recyclerView;
    protected GridView gridView;
    //Declare
    private WishListAdapter wishlistAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_view_all);
        initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        if (getLayoutCode() == RECYCLER_VIEW_PRODUCT) {
            setWishListItemModelList();
        } else if (getLayoutCode() == GRID_VIEW_PRODUCT) {
            setGridProductModel();
        }
    }

    private int getLayoutCode() {
        int layout_code = getIntent().getIntExtra("LAYOUT_CODE", -1);
        return layout_code;
    }


    void setWishListItemModelList() {
        /*wishListItemModelList = new ArrayList<>();
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google Pixel XL 2 (Mirror Black,128 GB)", 2 , "3.5" , 25,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google Pixel XL 2 (Mirror Black,128 GB)", 3 , "5" , 23,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google Pixel (Mirror Black,64 GB)", 0 , "2" , 15,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google Pixel XL 2 (Mirror Black,128 GB)", 2 , "4" , 25,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google Pixel XL 2 (Mirror Black,128 GB)", 2 , "4" , 25,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google (Mirror Black,32 GB)", 1 , "3.5" , 10,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google (Mirror Black,32 GB)", 1 , "3.5" , 10,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google (Mirror Black,32 GB)", 1 , "3.5" , 10,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google (Mirror Black,32 GB)", 1 , "3.5" , 10,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google Pixel XL 2 (Mirror Black,128 GB)", 2 , "2.5" , 25,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));
        wishListItemModelList.add(new WishListItemModel(R.drawable.product_image,"Google Pixel XL 2 (Mirror Black,128 GB)", 2 , "2.5" , 25,"RS.45,999/-" , "RS.49,999/-" , "Cash On Delivery Available"));
*/
        setWishListAdapter(wishListItemModelList);
    }

    void setWishListAdapter(List<WishListItemModel> wishListItemModelList) {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        wishlistAdapter = new WishListAdapter(wishListItemModelList, false);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(wishlistAdapter);
    }

    private void setGridProductModel() {
       /* horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.profile_placeholder,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.forgot_password_image,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.banner,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.strip_ad,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_store_mall_directory,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_menu_share,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_mail_green,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.forgot_password_image,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_store_mall_directory,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_mail_green,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.forgot_password_image,"Redmi A","SD 625 Processor","Rs.5999/-"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.ic_mail_green,"Redmi A","SD 625 Processor","Rs.5999/-"));
*/
        setGridViewAdapter(horizontalProductScrollModelList);
    }

    private void setGridViewAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(horizontalProductScrollModelList);
        recyclerView.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        gridView.setAdapter(gridProductLayoutAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        gridView = (GridView) findViewById(R.id.grid_view);
    }
}