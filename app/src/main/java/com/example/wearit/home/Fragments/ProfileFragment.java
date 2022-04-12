package com.example.wearit.home.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wearit.OrderHistoryActivity;
import com.example.wearit.R;
import com.example.wearit.UserAccount.UserAccountActivity;
import com.example.wearit.WishListActivity;
import com.example.wearit.admin.AdminActivity;
import com.example.wearit.api.response.OrderHistory;
import com.example.wearit.checkout.address.AddressActivity;
import com.example.wearit.utils.Constants;
import com.example.wearit.utils.SharedPrefUtils;


public class ProfileFragment extends Fragment {
    TextView logOutTV, adminAreaTV, addressTV, wishlistTV, orderHistoryTV,name , email, contact;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logOutTV = view.findViewById(R.id.logOutTV);
        adminAreaTV = view.findViewById(R.id.adminAreaTV);
        addressTV = view.findViewById(R.id.addressTV);
        wishlistTV=view.findViewById(R.id.wishListTV);
        orderHistoryTV=view.findViewById(R.id.ordersTV);
        name= view.findViewById(R.id.setname);
        email= view.findViewById(R.id.setemail);
        contact = view.findViewById(R.id.setcontact);

        setProfileContent();
        checkAdmin();
        setClickListeners();
    }

    private void setProfileContent(){
        if(SharedPrefUtils.getString(getActivity(),"con").equals("0")){


            contact.setText("Phone Number not Provided");
        }
           name.setText(SharedPrefUtils.getString(getActivity(),"nk"));
           email.setText(SharedPrefUtils.getString(getActivity(),"ek"));
           contact.setText(SharedPrefUtils.getString(getActivity(),"con"));
        Toast.makeText(getActivity(), SharedPrefUtils.getString(getActivity(),"con"), Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity(), SharedPrefUtils.getString(getActivity(),"ek"), Toast.LENGTH_LONG).show();


    }

    private void checkAdmin() {
        boolean is_staff = SharedPrefUtils.getBool(getActivity(), getString(R.string.staff_key), false);
        if (is_staff)
            adminAreaTV.setVisibility(View.VISIBLE);
    }

    private void setClickListeners() {
        logOutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPrefUtils.clear(getContext());
                SharedPrefUtils.setBoolean(getActivity(), Constants.IS_LOGGED_IN_KEY,false);
                Intent userAccount = new Intent(getContext(), UserAccountActivity.class);
                startActivity(userAccount);
                getActivity().finish();
            }
        });
        adminAreaTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminActivity.class);
                startActivity(intent);
            }
        });
        addressTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddressActivity.class);
                startActivity(intent);
            }
        });

        wishlistTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WishListActivity.class);
                startActivity(intent);
            }
        });

        orderHistoryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

}