package com.abhishek.android.habitnme;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.abhishek.android.habitnme.modules.DaggerDataComponent;
import com.abhishek.android.habitnme.modules.DataComponent;
import com.abhishek.android.habitnme.modules.DataModules;
import com.google.firebase.FirebaseApp;

import io.realm.Realm;

public class BaseApplication extends Application {

    public static DataComponent dataComponent;



    @Override
    public void onCreate() {
        super.onCreate();
        dataComponent = DaggerDataComponent.builder()
                .dataModules(new DataModules(getApplicationContext())).build();
        FirebaseApp.initializeApp(this);
        Realm.init(this);

    }

    public static DataComponent getDataComponent() {
        return dataComponent;
    }



}
