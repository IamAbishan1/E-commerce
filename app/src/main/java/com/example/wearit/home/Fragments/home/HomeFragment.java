package com.example.wearit.home.Fragments.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wearit.R;
import com.example.wearit.api.ApiClient;
import com.example.wearit.api.response.AllProductResponse;
import com.example.wearit.api.response.Category;
import com.example.wearit.api.response.CategoryResponse;
import com.example.wearit.api.response.Product;
import com.example.wearit.api.response.Slider;
import com.example.wearit.api.response.SliderResponse;
import com.example.wearit.categoryActivity.CategoryActivity;
import com.example.wearit.home.Fragments.home.adapters.CategoryAdapter;
import com.example.wearit.home.Fragments.home.adapters.ShopAdapter;
import com.example.wearit.home.Fragments.home.adapters.SliderAdapter;
import com.example.wearit.singleProductPage.SingleProductActivity;
import com.example.wearit.utils.SharedPrefUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    RecyclerView allProductRV, categoryRV;
    ProgressBar loadingProgress;
    SliderView imageSlider;
    TextView viewAllTV,username;
    LinearLayout searchLL;
    BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allProductRV = view.findViewById(R.id.allProductRV);
        categoryRV = view.findViewById(R.id.categoryRV);
        loadingProgress = view.findViewById(R.id.loadingProgress);
        imageSlider = view.findViewById(R.id.imageSlider);
        viewAllTV = view.findViewById(R.id.viewAllTV);
        searchLL = view.findViewById(R.id.searchLL);
        username = view.findViewById(R.id.userNameTV);

        username.setText(SharedPrefUtils.getString(getActivity(),"nk"));
        serverCall();
        getCategoriesOnline();
        getSliders();
        setClickListeners();
    }




    private void setClickListeners() {
        viewAllTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.catMenu);
            }
        });
        searchLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void getSliders() {
        Call<SliderResponse> sliderResponseCall = ApiClient.getClient().getSliders();
        sliderResponseCall.enqueue(new Callback<SliderResponse>() {
            @Override
            public void onResponse(Call<SliderResponse> call, Response<SliderResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().getError()) {
                        setSliders(response.body().getSliders());
                    }
                }
            }

            @Override
            public void onFailure(Call<SliderResponse> call, Throwable t) {

            }
        });
    }

    private void setSliders(List<Slider> sliders) {
        SliderAdapter sliderAdapter = new SliderAdapter(sliders, getContext(), true);
        sliderAdapter.setClickLister(new SliderAdapter.OnSliderClickLister() {
            @Override
            public void onSliderClick(int position, Slider slider) {

                if (slider.getType() == 1) {
                    Intent intent = new Intent(getContext(), SingleProductActivity.class);
                    intent.putExtra(SingleProductActivity.SINGLE_DATA_KEY, slider.getRelatedId());
                    getContext().startActivity(intent);
                } else if (slider.getType() == 2) {
                    Intent cat = new Intent(getContext(), CategoryActivity.class);
                    Category category = new Category();
                    category.setId(slider.getRelatedId());
                    category.setName(slider.getDesc());
                    cat.putExtra(CategoryActivity.CATEGORY_DATA_KEY, category);
                    getContext().startActivity(cat);
                }
            }
        });
        imageSlider.setSliderAdapter(sliderAdapter);
        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
        imageSlider.startAutoCycle();

    }

    private void getCategoriesOnline() {
        Call<CategoryResponse> getCategories = ApiClient.getClient().getCategories();
        getCategories.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().getError()) {
                        //DataHolder.categories = response.body().getCategories();
                        showCategories(response.body().getCategories());

                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

            }
        });

    }

    private void showCategories(List<Category> categories) {
        List<Category> temp;
        if (categories.size() > 8) {
            temp = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                temp.add(categories.get(categories.size() - i - 1));
            }
        } else {
            temp = categories;
        }
        categoryRV.setHasFixedSize(true);
        categoryRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        CategoryAdapter categoryAdapter = new CategoryAdapter(temp, getContext(), false, false, null);
        categoryRV.setAdapter(categoryAdapter);

    }

    private void serverCall() {
        toggleLoading(true);
        Call<AllProductResponse> allProductResponseCall = ApiClient.getClient().getAllProducts();
        allProductResponseCall.enqueue(new Callback<AllProductResponse>() {
            @Override
            public void onResponse(Call<AllProductResponse> call, Response<AllProductResponse> response) {
                toggleLoading(false);
                setProdctRecyclerView(response.body().getProducts());

            }

            @Override
            public void onFailure(Call<AllProductResponse> call, Throwable t) {
                toggleLoading(false);
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setProdctRecyclerView(List<Product> products) {
        allProductRV.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        allProductRV.setLayoutManager(layoutManager);
        ShopAdapter shopAdapter = new ShopAdapter(products, getContext(), false);
        allProductRV.setAdapter(shopAdapter);
    }

    void toggleLoading(boolean toggle) {
        if (toggle)
            loadingProgress.setVisibility(View.VISIBLE);
        else
            loadingProgress.setVisibility(View.GONE);
    }


}