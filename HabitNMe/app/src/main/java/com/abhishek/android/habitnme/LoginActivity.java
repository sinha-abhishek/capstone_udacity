package com.abhishek.android.habitnme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.android.habitnme.presenters.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusActivity;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(LoginPresenter.class)
public class LoginActivity extends NucleusAppCompatActivity<LoginPresenter> {

    @BindView(R.id.input_email) EditText inputEmail;

    @BindView(R.id.input_password) EditText inputPassword;

    @BindView(R.id.btn_login) AppCompatButton loginButton;

    @BindView(R.id.forgot_password) TextView forgotPassword;

    @BindView(R.id.link_signup) TextView signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.setDebug(true);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }



    @OnClick(R.id.link_signup)
    public void goToSignup() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_login)
    public void onLoginClick() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        if (email.length() == 0 ) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() == 0) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        getPresenter().startLogin(email, password);
    }

    public void onLoginResult(boolean result) {
        if (result) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finishAffinity();
        }
    }

    @OnClick(R.id.forgot_password)
    public void onForgorPwdClick() {
        String email = inputEmail.getText().toString();
        if (email.length() == 0 ) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        getPresenter().forgotPasseord(email);
    }
}
