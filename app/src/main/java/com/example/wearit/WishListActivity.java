package com.example.wearit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wearit.api.ApiClient;
import com.example.wearit.api.response.AllProductResponse;
import com.example.wearit.api.response.Product;
import com.example.wearit.api.response.RegisterResponse;
import com.example.wearit.home.Fragments.home.adapters.WishListAdapter;
import com.example.wearit.utils.SharedPrefUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListActivity extends AppCompatActivity {

    RecyclerView wishListRV;
    List<Product> products;
    TextView totalPriceTv,backTV;
    SwipeRefreshLayout swipeRefresh;
    LinearLayout addToCartLL;
    AllProductResponse allProductResponse;
    boolean isAdding=false;
    Button addTOCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        wishListRV = findViewById(R.id.wishListRV);
        addTOCart = findViewById(R.id.addToCartB);
        backTV=findViewById(R.id.backTV);
        getCartItems();

        backTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

       /* addTOCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getCartItems();
                if (!isAdding) {
                    isAdding = true;
                    //addingToggle(true);
                    String key = SharedPrefUtils.getString(WishListActivity.this, getString(R.string.api_key));
                    Call<RegisterResponse> cartCall = ApiClient.getClient().addToCart(key, products.getId(), 1);
                    cartCall.enqueue(new Callback<RegisterResponse>() {
                        @Override
                        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                           // addingToggle(false);
                            isAdding = false;

                        }

                        @Override
                        public void onFailure(Call<RegisterResponse> call, Throwable t) {
                            //addingToggle(false);
                            isAdding = false;
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Adding Already!!", Toast.LENGTH_SHORT).show();
                }

            }
        });
            }
*/
    }


    private void getCartItems() {

        String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
        Call<AllProductResponse> wishListCall = ApiClient.getClient().wishlist(key);
        wishListCall.enqueue(new Callback<AllProductResponse>() {
            @Override
            public void onResponse(Call<AllProductResponse> call, Response<AllProductResponse> response) {
               // swipeRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (!response.body().getError()) {
                        allProductResponse = response.body();
                        products = response.body().getProducts();
                        loadWishList();

                    }
                }
            }

            @Override
            public void onFailure(Call<AllProductResponse> call, Throwable t) {

            }
        });
    }


    private void loadWishList() {
        //wishListRV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        wishListRV.setLayoutManager(layoutManager);
        WishListAdapter wishListAdapter = new WishListAdapter(products, this);
        //wishListAdapter.setCartItemClick(new ShopAdapter.CartItemClick() {
          /*  @Override
            public void onRemoveCart(int position) {
                String key = SharedPrefUtils.getString(getActivity(), getString(R.string.api_key));
                Call<RegisterResponse> removeCartCall = ApiClient.getClient().deleteFromCart(key, products.get(position).getCartID());
                removeCartCall.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if (response.isSuccessful()) {
                            products.remove(products.get(position));
                            shopAdapter.notifyItemRemoved(position);
                            setPrice();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    }
                });
            }
        });*/
        wishListRV.setAdapter(wishListAdapter);
    }



    @Override
    public void onResume() {
        super.onResume();
        getCartItems();
    }



}