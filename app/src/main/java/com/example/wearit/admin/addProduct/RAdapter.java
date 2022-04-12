package com.example.wearit.admin.addProduct;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wearit.R;
import com.example.wearit.api.response.Category;

import java.util.List;

public class RAdapter extends RecyclerView.Adapter<RAdapter.ViewHolder> {

    LayoutInflater inflater;
    boolean isImage = false;
    List<Uri> uris;
    List<Category> cats;
    OnItemCLick onItemCLick;

    public RAdapter(boolean isImage, List<Uri> uris, List<Category> cats, Context context, OnItemCLick onItemCLick) {
        inflater = LayoutInflater.from(context);
        this.isImage = isImage;
        this.uris = uris;
        this.cats = cats;
        this.onItemCLick = onItemCLick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isImage)
            return new ViewHolder(inflater.inflate(R.layout.item_image, parent, false), isImage);
        else
            return new ViewHolder(inflater.inflate(R.layout.item_add_category, parent, false), isImage);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (isImage) {
            holder.imageView.setImageURI(uris.get(position));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemCLick.onCLick(holder.getAdapterPosition());
                }
            });
        } else{
            holder.textView.setText(cats.get(position).getName());
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemCLick.onCLick(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (isImage)
            return uris.size();
        else
            return cats.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        boolean isC;

        public ViewHolder(@NonNull View itemView, boolean isC) {
            super(itemView);
            if(isC)
                imageView = itemView.findViewById(R.id.cImg);
            else
                textView = itemView.findViewById(R.id.catNameTV);
        }
    }
    interface OnItemCLick {
        public void onCLick(int position);
    }
}
