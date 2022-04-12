package com.example.wearit.UserAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.wearit.R;
import com.example.wearit.UserAccount.Fragments.LogInFragment;
import com.example.wearit.UserAccount.Fragments.RegisterFragment;

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener {

    TextView registerTV, signUpRegisterTV;
    boolean isRegister = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        registerTV = findViewById(R.id.register);
        signUpRegisterTV = findViewById(R.id.info);
        registerTV.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.frame, new LogInFragment()).commit();
    }

    public void onClick(View v) {
//
        if (v == registerTV) {
            if (!isRegister) {

                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new RegisterFragment()).commit();

            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new LogInFragment()).commit();
            }
            changeTexts();
            isRegister = !isRegister;
        }
    }

    void changeTexts() {
        if (!isRegister) {
            registerTV.setText("Login");
            signUpRegisterTV.setText("Already Have an Account? ");
        } else {
            registerTV.setText("Register");
            signUpRegisterTV.setText("Don't have an account? ");
        }
    }
}