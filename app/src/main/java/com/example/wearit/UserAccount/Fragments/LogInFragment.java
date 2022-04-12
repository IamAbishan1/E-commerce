package com.example.wearit.UserAccount.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wearit.R;
import com.example.wearit.api.ApiClient;
import com.example.wearit.api.response.LoginResponse;
import com.example.wearit.home.MainActivity;
import com.example.wearit.utils.Constants;
import com.example.wearit.utils.SharedPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LogInFragment extends Fragment implements View.OnClickListener{
    EditText emailEt, passwordET;
    TextView loginBtn;
    ProgressBar circularProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        emailEt = view.findViewById(R.id.eid);
        passwordET = view.findViewById(R.id.pid);
        loginBtn = view.findViewById(R.id.bid);
        circularProgress = view.findViewById(R.id.circularProgress);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == loginBtn) {
            String email, password;
            email = emailEt.getText().toString();
            password = passwordET.getText().toString();
            if (email.isEmpty() && password.isEmpty())
                Toast.makeText(getContext(), "Email or Password is Empty!", Toast.LENGTH_LONG).show();
            else {
                ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                        "Logging In. Please wait...", false);
                Call<LoginResponse> loginResponseCall = ApiClient.getClient().login(email, password);
                loginResponseCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        progressDialog.dismiss();
                        LoginResponse loginResponse = response.body();
                        if (response.isSuccessful()) {
                            if (loginResponse.getError()) {
                                //System.out.println("222222221222222222222 my error  is: " + loginResponse.getError());
                                //Toast.makeText(getContext(), loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                //String phone=Integer.valueOf(loginResponse.getPhone_no());

                                Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_LONG).show();
                                SharedPrefUtils.setBoolean(getActivity(), getString(R.string.isLogged), true);
                                SharedPrefUtils.setString(getActivity(), getString(R.string.name_key), loginResponse.getName());
                                SharedPrefUtils.setString(getActivity(), getString(R.string.email_key), loginResponse.getEmail());
                                SharedPrefUtils.setString(getActivity(), getString(R.string.contact), loginResponse.getPhone_no().toString());

                                SharedPrefUtils.setString(getActivity(), getString(R.string.created_key), loginResponse.getCreatedAt());
                                SharedPrefUtils.setString(getActivity(), getString(R.string.api_key), loginResponse.getApiKey());
                                SharedPrefUtils.setBoolean(getActivity(), Constants.IS_LOGGED_IN_KEY,true);
                                SharedPrefUtils.setBoolean(getActivity(), getString(R.string.staff_key), loginResponse.getIs_staff());
                                getActivity().startActivity(new Intent(getContext(), MainActivity.class));
                                getActivity().finish();

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        progressDialog.dismiss();

                    }
                });
            }
               Toast.makeText(getContext(), "Wrong email or password", Toast.LENGTH_LONG).show();
        }

    }
}