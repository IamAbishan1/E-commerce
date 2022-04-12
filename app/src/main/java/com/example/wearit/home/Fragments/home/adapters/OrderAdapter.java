package com.example.wearit.home.Fragments.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wearit.R;
import com.example.wearit.api.response.OrderHistory;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    List<OrderHistory> orders;
    LayoutInflater layoutInflater;
    Context context;
    OrderAdapter.OrderViewHolder orderViewHolder;

    public OrderAdapter(List<OrderHistory> orders,Context context) {
        this.orders = orders;
        this.context =context;
        this.layoutInflater=LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //OrderAdapter orderAdapter= new OrderAdapter();
        return new OrderViewHolder(layoutInflater.inflate(R.layout.order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        holder.orderIdTV.setText(orders.get(position).getId());
        //Double price= orders.get(position).getBag().get()
        holder.orderNoTV.setText(orders.get(position).getBag().size());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    public class OrderViewHolder extends RecyclerView.ViewHolder{

        ImageView productIV, removeCartIV;
        Button viewDetailsB;
        TextView orderIdTV, totalPriceTV, orderNoTV;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productIV = (ImageView)itemView.findViewById(R.id.productImage);
            orderIdTV = itemView.findViewById(R.id.orderId);
            orderNoTV = itemView.findViewById(R.id.noOfOrderedItemsTV);
            totalPriceTV = itemView.findViewById(R.id.totalPriceTV);
            removeCartIV = itemView.findViewById(R.id.removeCartIV);
            viewDetailsB= itemView.findViewById(R.id.viewDetailsB);
        }
    }
}
