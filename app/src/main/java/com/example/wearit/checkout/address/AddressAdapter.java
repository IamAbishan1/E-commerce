package com.example.wearit.checkout.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wearit.R;
import com.example.wearit.api.response.Address;
import com.example.wearit.home.Fragments.home.adapters.OrderAdapter;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    List<Address> adressList;
    Context context;
    LayoutInflater inflater;

    OnAddressItemClickListener onAddressItemClickListener;

    public AddressAdapter(List<Address> adressList, Context context) {
        this.adressList = adressList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressViewHolder(inflater.inflate(R.layout.item_address, parent, false));
    }

    public void setOnAddressItemClickListener(OnAddressItemClickListener onAddressItemClickListener) {
        this.onAddressItemClickListener = onAddressItemClickListener;
    }


    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address adress = adressList.get(position);
        holder.cityStreetTV.setText(adress.getCity() + " " + adress.getStreet());
        holder.provinceTV.setText(adress.getProvince());
        holder.decTV.setText(adress.getDescription());
        holder.addressLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddressItemClickListener.onAddressClick(holder.getAdapterPosition(), adressList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return adressList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView cityStreetTV, provinceTV, decTV;
        LinearLayout addressLL;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            cityStreetTV = itemView.findViewById(R.id.cityStreetTV);
            provinceTV = itemView.findViewById(R.id.provinceTV);
            decTV = itemView.findViewById(R.id.decTV);
            addressLL = itemView.findViewById(R.id.addressLL);
        }
    }

    public interface OnAddressItemClickListener {
        public void onAddressClick(int position, Address adress);

    }
}


