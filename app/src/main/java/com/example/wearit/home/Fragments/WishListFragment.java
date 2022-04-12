package com.example.wearit.home.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wearit.R;
import com.example.wearit.api.ApiClient;
import com.example.wearit.api.response.AllProductResponse;
import com.example.wearit.api.response.Product;
import com.example.wearit.api.response.RegisterResponse;
import com.example.wearit.home.Fragments.home.adapters.ShopAdapter;
import com.example.wearit.home.Fragments.home.adapters.WishListAdapter;
import com.example.wearit.utils.SharedPrefUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WishListFragment extends Fragment {

    RecyclerView wishListRV;
    List<Product> products;
    TextView totalPriceTv;
    SwipeRefreshLayout swipeRefresh;
    LinearLayout addToCartLL;
    AllProductResponse allProductResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wish_list, container, false);
    }

    private void getCartItems() {

        String key = SharedPrefUtils.getString(getActivity(), getString(R.string.api_key));
        Call<AllProductResponse> wishListCall = ApiClient.getClient().wishlist(key);
        wishListCall.enqueue(new Callback<AllProductResponse>() {
            @Override
            public void onResponse(Call<AllProductResponse> call, Response<AllProductResponse> response) {
                swipeRefresh.setRefreshing(false);
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
        wishListRV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        wishListRV.setLayoutManager(layoutManager);
        WishListAdapter wishListAdapter = new WishListAdapter(products, getContext());
       wishListAdapter.setWishlistItemClickListener(new WishListAdapter.WishlistItemClickListener() {
            @Override
            public void wishlistRemoveClick(int position) {
                String key = SharedPrefUtils.getString(getActivity(), getString(R.string.api_key));
                Call<RegisterResponse> removeWishlistCall = ApiClient.getClient().deleteFromWishlist(key, products.get(position).getCartID());
                removeWishlistCall.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if(response.isSuccessful()) {
                            products.remove(products.get(position));
                            wishListAdapter.notifyItemRemoved(position);
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    }
                });
            }

           @Override
           public void addToCart(int position) {

//               String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
//               Call<RegisterResponse> cartCall = ApiClient.getClient().addToCart(key, product.getId(), quantity);
//               cartCall.enqueue(new Callback<RegisterResponse>() {
//                   @Override
//                   public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
//                       if (response.isSuccessful()) {
//                           Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                       }
//                       addingToggle(false);
//                       isAdding = false;
//
//                   }
//
//                   @Override
//                   public void onFailure(Call<RegisterResponse> call, Throwable t) {
//                       addingToggle(false);
//                       isAdding = false;
//                   }
//               });
           //}

               String key = SharedPrefUtils.getString(getActivity(), getString(R.string.api_key));
               Call<RegisterResponse> cartCall = ApiClient.getClient().addToCart(key, products.get(position).getId(), Integer.parseInt(products.get(position).getQuantity()));
               cartCall.enqueue(new Callback<RegisterResponse>() {
                  @Override
                  public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                      if (response.isSuccessful()) {
                          Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                  }

                  @Override
                  public void onFailure(Call<RegisterResponse> call, Throwable t) {

                  }
              });



           }
       });


        wishListRV.setAdapter(wishListAdapter);
    }

        @Override
        public void onResume () {
            super.onResume();
            getCartItems();
        }


    }


