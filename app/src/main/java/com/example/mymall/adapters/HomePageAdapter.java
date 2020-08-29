package com.example.mymall.adapters;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymall.R;
import com.example.mymall.models.HomePageModel;
import com.example.mymall.models.HorizontalProductScrollModel;
import com.example.mymall.models.SliderModel;
import com.example.mymall.models.WishListItemModel;
import com.example.mymall.ui.ProductDetailsActivity;
import com.example.mymall.ui.ViewAllActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class HomePageAdapter extends RecyclerView.Adapter {

    public static final int RECYCLER_VIEW_PRODUCT = 0;
    public static final int GRID_VIEW_PRODUCT = 1;

    private List<HomePageModel> homePageModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {
            case 0:
                return HomePageModel.BANNER_SLIDER;
            case 1:
                return HomePageModel.STRIP_AD_BANNER;
            case 2:
                return HomePageModel.HORIZONTAL_PRODUCT;
            case 3:
                return HomePageModel.Grid_PRODUCT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HomePageModel.BANNER_SLIDER:
                View bannerSliderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_ad_layout, parent, false);
                return new BannerSliderViewHolder(bannerSliderView);
            case HomePageModel.STRIP_AD_BANNER:
                View stripAdView = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_ad_layout, parent, false);
                return new StripAdViewHolder(stripAdView);
            case HomePageModel.HORIZONTAL_PRODUCT:
                View horizontalProduct = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_layout, parent, false);
                return new HorizontalProductViewHolder(horizontalProduct);
            case HomePageModel.Grid_PRODUCT:
                View gridProduct = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_layout, parent, false);
                return new GridProductViewHolder(gridProduct);
            default:
                return null;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.BANNER_SLIDER:
                List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
                ((BannerSliderViewHolder) holder).setBannerSliderViewPager(sliderModelList);
                break;
            case HomePageModel.STRIP_AD_BANNER:
                String resource = homePageModelList.get(position).getResourceImage();
                String stripAdColor = homePageModelList.get(position).getBackgroundColor();
                ((StripAdViewHolder) holder).setStripAdView(resource, stripAdColor);
                break;
            case HomePageModel.HORIZONTAL_PRODUCT:
                String horizontalProductTitle = homePageModelList.get(position).getTitle();
                String horizontalContainerBackground = homePageModelList.get(position).getBackgroundColor();
                List<WishListItemModel> viewAllProductList = homePageModelList.get(position).getViewAllProductList();
                List<HorizontalProductScrollModel> horizontalProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
                ((HorizontalProductViewHolder) holder).setHorizontalScrollTitleAndBackgroundColor(horizontalProductTitle, horizontalContainerBackground);
                ((HorizontalProductViewHolder) holder).setHorizontalProductAdapter(horizontalProductScrollModelList, viewAllProductList, horizontalProductTitle);
            case HomePageModel.Grid_PRODUCT:
                String gridProductTitle = homePageModelList.get(position).getTitle();
                String gridLayoutColor = homePageModelList.get(position).getBackgroundColor();
                List<HorizontalProductScrollModel> gridProductModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
                if (holder instanceof GridProductViewHolder) {
                    ((GridProductViewHolder) holder).setGridProductTitleAndLayoutBackground(gridProductTitle, gridLayoutColor);
                }
                if (holder instanceof GridProductViewHolder) {
                    ((GridProductViewHolder) holder).setGridProductAdapter(gridProductModelList, gridProductTitle);
                }
            default:
                return;
        }
    }


    @Override
    public int getItemCount() {
        return homePageModelList == null ? 0 : homePageModelList.size();
    }

    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {
        final private long DELAY_TIME = 3000;
        final private long PERIOD_TIME = 3000;
        private ViewPager bannerSliderViewPager;
        private int currentPage;
        private Timer timer;
        private List<SliderModel> arrangedList;

        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerSliderViewPager = itemView.findViewById(R.id.banner_slider_view_pager);
        }

        private void setBannerSliderViewPager(List<SliderModel> sliderModelList) {
            currentPage = 2;
            if (timer != null) {
                timer.cancel();
            }
            setArrangedList(sliderModelList);

            setSliderAdapter(arrangedList);
        }

        private void setArrangedList(List<SliderModel> sliderModelList) {
            arrangedList = new ArrayList<>();
            for (int i = 0; i < sliderModelList.size(); i++) {
                arrangedList.add(i, sliderModelList.get(i));
            }
            arrangedList.add(0, sliderModelList.get(sliderModelList.size() - 2));
            arrangedList.add(1, sliderModelList.get(sliderModelList.size() - 1));
            arrangedList.add(sliderModelList.get(0));
            arrangedList.add(sliderModelList.get(1));
        }

        private void setSliderAdapter(List<SliderModel> sliderModelList) {
            SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
            bannerSliderViewPager.setAdapter(sliderAdapter);
            bannerSliderViewPager.setClipToPadding(false);
            bannerSliderViewPager.setPageMargin(20);
            bannerSliderViewPager.setCurrentItem(currentPage);

            changeSliderImage(sliderModelList);
            setTouchSlider(sliderModelList);

        }

        private void changeSliderImage(final List<SliderModel> sliderModelList) {
            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        pageLooper(sliderModelList);
                    }
                }
            };
            bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);
            startBannerSlideShow(sliderModelList);
        }

        private void setTouchSlider(final List<SliderModel> sliderModelList) {
            bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pageLooper(sliderModelList);
                    stopBannerSlideShow();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startBannerSlideShow(sliderModelList);
                    }

                    return false;
                }
            });
        }

        private void pageLooper(List<SliderModel> sliderModelList) {
            if (sliderModelList != null) {
                if (currentPage == sliderModelList.size() - 2) {
                    currentPage = 2;
                    bannerSliderViewPager.setCurrentItem(currentPage, false);
                }
                if (currentPage == 1) {
                    currentPage = sliderModelList.size() - 3;
                    bannerSliderViewPager.setCurrentItem(currentPage, false);
                }
            }
        }

        private void startBannerSlideShow(final List<SliderModel> sliderModelList) {

            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (sliderModelList != null) {
                        if (currentPage >= sliderModelList.size()) {
                            currentPage = 1;
                        }
                    }
                    bannerSliderViewPager.setCurrentItem(currentPage++, true);
                }
            };

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, DELAY_TIME, PERIOD_TIME);
        }

        private void stopBannerSlideShow() {
            timer.cancel();
        }

    }

    public class StripAdViewHolder extends RecyclerView.ViewHolder {
        private ImageView stripAdImage;
        private ConstraintLayout stripAdContainer;

        public StripAdViewHolder(@NonNull View itemView) {
            super(itemView);
            stripAdImage = itemView.findViewById(R.id.strip_ad_image);
            stripAdContainer = itemView.findViewById(R.id.strip_ad_container);
        }

        private void setStripAdView(String resource, String backgroundColor) {
            //stripAdImage.setImageResource(resource);
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_home)).into(stripAdImage);
            stripAdContainer.setBackgroundColor(Color.parseColor(backgroundColor));
        }
    }

    public class HorizontalProductViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout container;
        private Button horizontalViewAllBtn;
        private TextView horizontalScrollTitle;
        private RecyclerView horizontalScrollRecyclerView;

        public HorizontalProductViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            horizontalViewAllBtn = itemView.findViewById(R.id.horizontal_product_layout_viewAll_btn);
            horizontalScrollTitle = itemView.findViewById(R.id.horizontal_product_layout_title);
            horizontalScrollRecyclerView = itemView.findViewById(R.id.horizontal_scroll_recycler_view);
            //used for nested recycler view
            horizontalScrollRecyclerView.setRecycledViewPool(recycledViewPool);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setHorizontalScrollTitleAndBackgroundColor(String title, String color) {
            horizontalScrollTitle.setText(title);
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
        }

        private void setHorizontalProductAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList, final List<WishListItemModel> viewAllProductList, final String title) {
            HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalScrollRecyclerView.setAdapter(horizontalProductScrollAdapter);
            horizontalScrollRecyclerView.setLayoutManager(linearLayoutManager);
            horizontalProductScrollAdapter.notifyDataSetChanged();

            if (horizontalProductScrollModelList.size() > 8) {
                horizontalViewAllBtn.setVisibility(View.VISIBLE);
                horizontalViewAllBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAllActivity.wishListItemModelList = viewAllProductList;

                        Intent intent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        intent.putExtra("LAYOUT_CODE", RECYCLER_VIEW_PRODUCT);
                        intent.putExtra("title", title);
                        itemView.getContext().startActivity(intent);
                    }
                });
            } else {
                horizontalViewAllBtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    public class GridProductViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout container;
        private Button gridViewAllBtn;
        private TextView gridLayoutTitle;
        private GridLayout gridProductLayout;

        public GridProductViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            gridViewAllBtn = itemView.findViewById(R.id.grid_product_layout_viewAll_btn);
            gridLayoutTitle = itemView.findViewById(R.id.grid_product_layout_title);
            gridProductLayout = itemView.findViewById(R.id.grid_layout);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setGridProductTitleAndLayoutBackground(String title, String color) {
            gridLayoutTitle.setText(title);
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
        }

        private void setGridProductAdapter(final List<HorizontalProductScrollModel> gridProductModelList, final String title) {

            //if you used grid view
            //GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(gridProductModelList);

            for (int i = 0; i < 4; i++) {
                ImageView productImage = gridProductLayout.getChildAt(i).findViewById(R.id.h_s_item_image);
                TextView productTitle = gridProductLayout.getChildAt(i).findViewById(R.id.h_s_item_title);
                TextView productDescription = gridProductLayout.getChildAt(i).findViewById(R.id.h_s_item_description);
                TextView productPrice = gridProductLayout.getChildAt(i).findViewById(R.id.h_s_item_price);

                Glide.with(itemView.getContext()).load(gridProductModelList.get(i).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.ic_home)).into(productImage);
                productTitle.setText(gridProductModelList.get(i).getProductTitle());
                productDescription.setText(gridProductModelList.get(i).getProductDescription());
                productPrice.setText("Rs." + gridProductModelList.get(i).getProductPrice() + "/-");

                gridProductLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
                gridProductLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemView.getContext().startActivity(new Intent(itemView.getContext(), ProductDetailsActivity.class));
                    }
                });
            }


            if (gridProductModelList.size() > 4) {
                gridViewAllBtn.setVisibility(View.VISIBLE);
                gridViewAllBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ///Very Important/// give List of data to horizontalProductScrollModelList
                        ViewAllActivity.horizontalProductScrollModelList = gridProductModelList;
                        Intent intent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        intent.putExtra("LAYOUT_CODE", GRID_VIEW_PRODUCT);
                        intent.putExtra("title", title);
                        itemView.getContext().startActivity(intent);
                    }
                });
            } else {
                gridViewAllBtn.setVisibility(View.INVISIBLE);

            }
        }
    }
}
