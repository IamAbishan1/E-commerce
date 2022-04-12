package com.example.wearit.UserAccount.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.wearit.api.response.RegisterResponse;
import com.example.wearit.home.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterFragment extends Fragment {

    EditText emailET, passwordET, confirmPasswordET, nameET,phoneET;
    TextView registerTV;
    ProgressBar circularProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailET = view.findViewById(R.id.signupEmail);
        nameET = view.findViewById(R.id.uNameid);
        passwordET = view.findViewById(R.id.signupPassword);
        phoneET = view.findViewById(R.id.signupPhone);
        confirmPasswordET = view.findViewById(R.id.confirmPassword);
        circularProgress = view.findViewById(R.id.circularProgress);
        registerTV= view.findViewById(R.id.bid);
        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    toggleLoading(true);
                    Call<RegisterResponse> registerCall = ApiClient.getClient().register(nameET.getText().toString(), emailET.getText().toString(), passwordET.getText().toString(),Integer.parseInt(phoneET.getText().toString()));
                    registerCall.enqueue(new Callback<RegisterResponse>() {
                        @Override
                        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                            RegisterResponse registerResponse = response.body();
                            toggleLoading(false);
                            if (response.isSuccessful()) {
                               Toast.makeText(getActivity(), registerResponse.getMessage(), Toast.LENGTH_SHORT).show();


                                if (!response.body().getError()) {
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, new LogInFragment()).commit();
                                    //getActivity().startActivity(new Intent(getContext(), MainActivity.class));
                                    //getActivity().finish();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RegisterResponse> call, Throwable t) {
                            toggleLoading(false);
                            Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });

    }

    void toggleLoading(Boolean toogle) {
        if (toogle)
            circularProgress.setVisibility(View.VISIBLE);
        else
            circularProgress.setVisibility(View.GONE);
    }

    public boolean validate() {
        boolean validate = true;
        if (emailET.getText().toString().isEmpty()
                || passwordET.getText().toString().isEmpty()
                || confirmPasswordET.getText().toString().isEmpty()
                || nameET.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "None of the above fields can be empty", Toast.LENGTH_SHORT).show();
            validate = false;
        } else if (!passwordET.getText().toString().equals(confirmPasswordET.getText().toString())) {
            confirmPasswordET.setError("Password doesnot match please check!!");
            validate = false;

        }

        return validate;
    }

}
