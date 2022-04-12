package com.example.wearit.categoryActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wearit.R;
import com.example.wearit.api.ApiClient;
import com.example.wearit.api.response.AllProductResponse;
import com.example.wearit.api.response.Category;
import com.example.wearit.api.response.Product;
import com.example.wearit.home.Fragments.home.adapters.ShopAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    public static String CATEGORY_DATA_KEY = "cdk";
    public static String CAT_KEY = "ctk";
    Category category;
    RecyclerView allProductsRV;
    ImageView emptyIV;
    ProgressBar loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        if (getIntent().getSerializableExtra(CATEGORY_DATA_KEY) == null)
            finish();
        allProductsRV = findViewById(R.id.allProductRV);
        loadingProgress = findViewById(R.id.loadingProgress);
        emptyIV = findViewById(R.id.emptyIV);

        category = (Category) getIntent().getSerializableExtra(CATEGORY_DATA_KEY);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(category.getName());
        getCategoryOnline();
    }

    private void getCategoryOnline() {
        toggleLoading(true);
        Call<AllProductResponse> getProductsByCategory = ApiClient.getClient().getProductsByCategory(category.getId());
        getProductsByCategory.enqueue(new Callback<AllProductResponse>() {
            @Override
            public void onResponse(Call<AllProductResponse> call, Response<AllProductResponse> response) {
                if (response.isSuccessful()) {
                    toggleLoading(false);
                    if (!response.body().getError()) {
                        if (response.body().getProducts().size() == 0)
                            showEmptyLayout();
                        else
                            showCategoriesProducts(response.body().getProducts());
                    }
                }
            }

            @Override
            public void onFailure(Call<AllProductResponse> call, Throwable t) {
                toggleLoading(false);
                Toast.makeText(CategoryActivity.this, "An Unknown Error Occoured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEmptyLayout() {
        emptyIV.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showCategoriesProducts(List<Product> products) {
        allProductsRV.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        allProductsRV.setLayoutManager(layoutManager);
        ShopAdapter shopAdapter = new ShopAdapter(products, this, false);
        allProductsRV.setAdapter(shopAdapter);
    }

    void toggleLoading(boolean toggle) {
        if (toggle)
            loadingProgress.setVisibility(View.VISIBLE);
        else
            loadingProgress.setVisibility(View.GONE);
    }

}