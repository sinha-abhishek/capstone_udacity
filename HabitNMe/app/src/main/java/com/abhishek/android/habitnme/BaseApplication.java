package com.abhishek.android.habitnme;

import android.app.Application;

import com.abhishek.android.habitnme.modules.DaggerDataComponent;
import com.abhishek.android.habitnme.modules.DataComponent;
import com.abhishek.android.habitnme.modules.DataModules;

public class BaseApplication extends Application {

    public static DataComponent dataComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        dataComponent = DaggerDataComponent.builder()
                .dataModules(new DataModules(getApplicationContext())).build();
    }

    public static DataComponent getDataComponent() {
        return dataComponent;
    }


}
