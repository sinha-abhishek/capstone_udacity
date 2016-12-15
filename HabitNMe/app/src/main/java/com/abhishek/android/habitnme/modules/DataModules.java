package com.abhishek.android.habitnme.modules;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmConfiguration;

@Module
public class DataModules {
    Context context;
    public DataModules(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context getContext() {
        return context;
    }

    @Provides
    @Singleton
    public FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    public RealmConfiguration getRealmConfiguration() {
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("habitdb.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        return configuration;
    }
}
