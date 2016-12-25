package com.abhishek.android.habitnme;

import javax.inject.Inject;

import io.realm.RealmConfiguration;

public class Utils {

    @Inject
    protected RealmConfiguration realmConfiguration;

    private static Utils ourInstance = new Utils();

    public static Utils getInstance() {
        return ourInstance;
    }

    private Utils() {
        BaseApplication.getDataComponent().inject(this);
    }

    public RealmConfiguration getRealmConfiguration() {
        return realmConfiguration;
    }


}
