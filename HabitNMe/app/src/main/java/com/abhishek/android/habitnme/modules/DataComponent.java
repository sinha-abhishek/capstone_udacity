package com.abhishek.android.habitnme.modules;

import com.abhishek.android.habitnme.LoginActivity;
import com.abhishek.android.habitnme.presenters.LoginPresenter;
import com.abhishek.android.habitnme.presenters.RegisterPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DataModules.class})
public interface DataComponent {
    void inject(LoginActivity loginActivity);
    void inject(RegisterPresenter registerPresenter);
    void inject(LoginPresenter loginPresenter);
}
