package com.example.wearit.admin.products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wearit.R;
import com.example.wearit.api.response.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShopAdapterAdmin extends RecyclerView.Adapter<ShopAdapterAdmin.ShopViewHolder> {

    List<Product> productDataList;
    LayoutInflater layoutInflater;
    Context context;

    public ShopAdapterAdmin(List<Product> productDataList,Context context) {
        this.productDataList = productDataList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShopViewHolder(layoutInflater.inflate(R.layout.item_product_admin, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {

        holder.nameTV.setText(productDataList.get(position).getName());
        if (productDataList.get(position).getDiscountPrice() == null || productDataList.get(position).getDiscountPrice() == 0) {
            holder.priceTv.setVisibility(View.GONE);
            holder.discountPrice.setText("Rs. " + productDataList.get(position).getPrice() + "");
        } else
            holder.discountPrice.setText("Rs. " + productDataList.get(position).getDiscountPrice() + "");
        holder.priceTv.setText(productDataList.get(position).getPrice() + "");
        holder.quantityTV.setText(productDataList.get(position).getQuantity()+ " Units");
        Picasso.get().load(productDataList.get(position).getImages().get(0)).into(holder.productIV);
        holder.mainLL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

    }

    @Override
    public int getItemCount() {
        return productDataList.size();
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder{
        ImageView productIV;
        LinearLayout mainLL;
        TextView nameTV, priceTv, discountPrice,  quantityTV;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            productIV = itemView.findViewById(R.id.productIV);
            nameTV = itemView.findViewById(R.id.productNameTV);
            mainLL = itemView.findViewById(R.id.mainLL);
            priceTv = itemView.findViewById(R.id.oldPriceTV);
            discountPrice = itemView.findViewById(R.id.discountPriceTV);
            quantityTV = itemView.findViewById(R.id.quantityTV);

        }
    }
}
