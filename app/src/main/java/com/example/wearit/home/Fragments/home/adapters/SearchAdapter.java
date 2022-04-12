package com.example.wearit.home.Fragments.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wearit.R;
import com.example.wearit.api.response.Product;
import com.example.wearit.singleProductPage.SingleProductActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    List<Product> productListFull;
    List<Product> searchData;
    LayoutInflater layoutInflater;
    Context context;

    public SearchAdapter(List<Product> productListFull, Context context) {
        this.productListFull = productListFull;
        searchData = new ArrayList<>(productListFull);
        layoutInflater = LayoutInflater.from(context);
        this.context = context;


    }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(layoutInflater.inflate(R.layout.item_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.SearchProductNameTV.setText(productListFull.get(position).getName());
        holder.SearchDiscountPriceTV.setText("Rs. " + productListFull.get(position).getPrice() + "");
        Picasso.get().load(productListFull.get(position).getImages().get(0)).into(holder.SearchProductIV);

        holder.SearchAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleProductActivity.class);
                intent.putExtra(SingleProductActivity.DATA_KEY, productListFull.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (productListFull != null) {
            return productListFull.size();
        }
        return 0;
    }

    public Filter getFilter(){
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Product> suggestions = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                suggestions.addAll(searchData);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Product item : searchData) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);

                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = suggestions;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            productListFull.clear();
            productListFull.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView SearchProductIV;
        TextView SearchProductNameTV, SearchDiscountPriceTV;
        LinearLayout SearchAll;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            SearchProductIV = itemView.findViewById(R.id.SearchProductIV);
            SearchProductNameTV = itemView.findViewById(R.id.SearchProductNameTV);
            SearchDiscountPriceTV = itemView.findViewById(R.id.SearchDiscountPriceTV);
            SearchAll = itemView.findViewById(R.id.SearchAll);
        }
    }
}
