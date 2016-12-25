package com.abhishek.android.habitnme.modules;

import com.abhishek.android.habitnme.HabitDataProvider;
import com.abhishek.android.habitnme.LoginActivity;
import com.abhishek.android.habitnme.Utils;
import com.abhishek.android.habitnme.adapters.CategoryListAdapter;
import com.abhishek.android.habitnme.adapters.HabitLogAdapter;
import com.abhishek.android.habitnme.presenters.AddHabitPresenter;
import com.abhishek.android.habitnme.presenters.HomePagePresenter;
import com.abhishek.android.habitnme.presenters.LoginPresenter;
import com.abhishek.android.habitnme.presenters.ProgressPresenter;
import com.abhishek.android.habitnme.presenters.RegisterPresenter;
import com.abhishek.android.habitnme.sync.SyncAdapter;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.RealmConfiguration;

@Singleton
@Component(modules = {DataModules.class})
public interface DataComponent {
    void inject(LoginActivity loginActivity);
    void inject(RegisterPresenter registerPresenter);
    void inject(LoginPresenter loginPresenter);
    void inject(HabitDataProvider habitDataProvider);
    void inject(AddHabitPresenter addHabitPresenter);
    void inject(HomePagePresenter homePagePresenter);
    void inject(HabitLogAdapter habitLogAdapter);
    void inject(CategoryListAdapter categoryListAdapter);
    void inject(ProgressPresenter progressPresenter);
    void inject(SyncAdapter syncAdapter);
    void inject(Utils utils);
}
