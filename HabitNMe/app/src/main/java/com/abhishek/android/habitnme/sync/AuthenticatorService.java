package com.abhishek.android.habitnme.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class AuthenticatorService extends Service {
    private DummyAuthenticator dummyAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        dummyAuthenticator = new DummyAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return dummyAuthenticator.getIBinder();
    }
}
