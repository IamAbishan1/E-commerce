package com.example.wearit.admin.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wearit.R;
import com.example.wearit.api.ApiClient;
import com.example.wearit.api.response.OrderHistory;
import com.example.wearit.api.response.OrderHistoryResponse;
import com.example.wearit.home.Fragments.home.adapters.OrderAdapter;
import com.example.wearit.utils.SharedPrefUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrderActivity extends AppCompatActivity {

    RecyclerView orderHistoryRV;
    List<OrderHistory> orders;
    TextView totalPriceTv,backTV;
    OrderHistoryResponse orderHistory;
    SwipeRefreshLayout swipeRefresh;
    LinearLayout addToCartLL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderHistoryRV = findViewById(R.id.orderHistoryRV);
        //addTOCart = findViewById(R.id.addToCartB);
        backTV=findViewById(R.id.backTV);
        getOrderHistory();

        backTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private void getOrderHistory(){
        String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
        Call<OrderHistoryResponse> orderHistory = ApiClient.getClient().allOrder(key);

        orderHistory.enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().getError()) {
                        //orderHistory = response.body();
                        orders = response.body().getOrderHistory();
                        loadWishList();

                    }
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {

            }
        });
    }
    private void loadWishList() {
        orderHistoryRV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        orderHistoryRV.setLayoutManager(layoutManager);
        OrderAdapter orderAdapter = new OrderAdapter(orders, this);

        orderHistoryRV.setAdapter(orderAdapter);
    }
}