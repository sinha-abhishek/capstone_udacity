package com.abhishek.android.habitnme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.android.habitnme.presenters.RegisterPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(RegisterPresenter.class)
public class RegisterActivity extends NucleusAppCompatActivity<RegisterPresenter> {

    @BindView(R.id.input_email_register)
    protected EditText inputEmail;

    @BindView(R.id.input_password_register) protected EditText inputPassword;

    @BindView(R.id.input_name)
    protected EditText inputName;

    @BindView(R.id.btn_signup)
    protected AppCompatButton signupButton;

//    @BindView(R.id.forgot_password)
//    protected TextView forgotPassword;

    @BindView(R.id.link_login) protected TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_signup)
    public void onRegisterClick() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String name = inputName.getText().toString();
        if (email.length() == 0 ) {
            Toast.makeText(this, getString(R.string.enter_email_text), Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() == 0) {
            Toast.makeText(this, getString(R.string.enter_pass_text), Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.length() == 0) {
            Toast.makeText(this, getString(R.string.enter_name_text), Toast.LENGTH_SHORT).show();
            return;
        }
        getPresenter().startRegister(name, email, password);

    }

    public void onRegisterSuccess() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    public void onRegisterFail() {

    }

    @OnClick(R.id.link_login)
    public void onLoginClick() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}
