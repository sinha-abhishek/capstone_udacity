package com.abhishek.android.habitnme.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.abhishek.android.habitnme.BaseApplication;
import com.abhishek.android.habitnme.models.HabitDayLog;
import com.abhishek.android.habitnme.models.HabitModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
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
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }
        Realm realm = Realm.getInstance(realmConfiguration);
        if (realm.isEmpty()) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        List<HabitModel> allHabits = realm.where(HabitModel.class)
                .equalTo("uuid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .findAll();
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


        if (FirebaseAuth.getInstance().getCurrentUser() != null && realm.where(HabitModel.class)
                .equalTo("uuid", FirebaseAuth.getInstance().getCurrentUser().getUid()).count() > 0) {
            String path = realm.getPath();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            Uri file = Uri.fromFile(new File(path));
            String storagePath = "/data/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/"+(new File(path)).getName();
            StorageReference riversRef = storageRef.child(storagePath);
            Log.i("SyncAdapter", "Path1: " + path);
            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
            Log.i("SyncAdapter", "Path: "+ path);
        }
        realm.close();

    }
}
