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
import com.example.wearit.api.response.Address;
import com.example.wearit.api.response.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishListViewHolder> {
    List<Product> productDataList;
    LayoutInflater layoutInflater;
    Context context;
    Boolean isCart = false;
    WishlistItemClickListener wishlistItemClickListener;
    //CartItemClick cartItemClick;
    Boolean removeEnabled = true;

    public WishListAdapter(List<Product> productDataList,Context context) {
        this.productDataList = productDataList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setWishlistItemClickListener(WishlistItemClickListener wishlistItemClickListener){
        this.wishlistItemClickListener=wishlistItemClickListener;
    }

    @NonNull
    @Override
    public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WishListViewHolder(layoutInflater.inflate(R.layout.wishlist_item,parent,false));
    }

    public void setRemoveEnabled(Boolean removeEnabled) {
        this.removeEnabled = removeEnabled;
    }

    @Override
    public void onBindViewHolder(@NonNull WishListViewHolder holder, int position) {
        holder.nameTV.setText(productDataList.get(position).getName());
        if (productDataList.get(position).getDiscountPrice() == null || productDataList.get(position).getDiscountPrice() == 0) {
            holder.priceTv.setVisibility(View.GONE);
            holder.discountPrice.setText("Rs. " + productDataList.get(position).getPrice() + "");
        } else
            holder.discountPrice.setText("Rs. " + productDataList.get(position).getDiscountPrice() + "");
        holder.priceTv.setText(productDataList.get(position).getPrice() + "");
        holder.removeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishlistItemClickListener.wishlistRemoveClick(holder.getAbsoluteAdapterPosition());
            }
        });
        holder.addToCartb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishlistItemClickListener.addToCart(holder.getAbsoluteAdapterPosition());
            }
        });

        Picasso.get().load(productDataList.get(position).getImages().get(0)).into(holder.productIV);
        holder.quantityTV.setText(productDataList.get(position).getCartQuantity() + "");

    }

    @Override
    public int getItemCount() {
        return productDataList.size();
    }

    public class WishListViewHolder extends RecyclerView.ViewHolder{

        ImageView productIV, removeIV;
        LinearLayout mainLL;
        Button addToCartb;
        TextView nameTV, priceTv, discountPrice, discountPercent, quantityTV;
        public WishListViewHolder(@NonNull View itemView) {
            super(itemView);
            productIV = (ImageView)itemView.findViewById(R.id.productImage);
            nameTV = itemView.findViewById(R.id.productNameTV);
            mainLL = itemView.findViewById(R.id.mainLL);
            addToCartb=itemView.findViewById(R.id.addToCartB);
            priceTv = itemView.findViewById(R.id.oldPriceTV);
            discountPrice = itemView.findViewById(R.id.discountPriceTV);
                removeIV = itemView.findViewById(R.id.removeCartIV);
                quantityTV = itemView.findViewById(R.id.quantityTV);
                //addToCart=itemView.findViewById(R.id.addToCartB);


        }
    }

    public interface WishlistItemClickListener {
        public void wishlistRemoveClick(int position);
        public void addToCart(int position);

    }
}
