package com.abhishek.android.habitnme.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.abhishek.android.habitnme.BaseApplication;
import com.abhishek.android.habitnme.models.HabitDayLog;
import com.abhishek.android.habitnme.models.HabitModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;


public class SyncAdapter extends AbstractThreadedSyncAdapter {
    ContentResolver contentResolver;

    @Inject
    RealmConfiguration realmConfiguration;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        contentResolver = context.getContentResolver();
    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i("SyncAdapter", "start sync");
        if (realmConfiguration == null) {
            BaseApplication.getDataComponent().inject(this);
        }
        Realm realm = Realm.getInstance(realmConfiguration);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        List<HabitModel> allHabits = realm.where(HabitModel.class).findAll();
        realm.beginTransaction();
        for (HabitModel habitModel:
             allHabits) {
            RealmResults<HabitDayLog> habitLog = realm.where(HabitDayLog.class).equalTo("habitModel.id", habitModel.getId())
                    .equalTo("yyyy", calendar.get(Calendar.YEAR)).equalTo("dayOfYear", calendar.get(Calendar.DAY_OF_YEAR)).findAll();
            if (habitLog.isEmpty()) {
                HabitDayLog dayLog = realm.where(HabitDayLog.class).equalTo("habitModel.id", habitModel.getId())
                        .findAllSorted("yyyy", Sort.DESCENDING).sort("dayOfYear", Sort.DESCENDING).first();
                Calendar last = Calendar.getInstance();
                last.set(dayLog.getYyyy(), dayLog.getMm(), dayLog.getDd());
                while (last.before(calendar)) {
                    last.add(Calendar.DATE, 1);
                    HabitDayLog habitDayLog = new HabitDayLog(0, last.get(Calendar.DATE), habitModel,
                            last.get(Calendar.MONTH), last.get(Calendar.YEAR), last.get(Calendar.DAY_OF_YEAR));
                    realm.copyToRealmOrUpdate(habitDayLog);
                }

            }
        }
        realm.commitTransaction();
        realm.close();

    }
}
