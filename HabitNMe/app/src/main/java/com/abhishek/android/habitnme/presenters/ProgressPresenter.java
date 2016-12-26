package com.abhishek.android.habitnme.presenters;

import android.os.Bundle;

import com.abhishek.android.habitnme.BaseApplication;
import com.abhishek.android.habitnme.ProgressActivity;
import com.abhishek.android.habitnme.models.HabitDayLog;
import com.abhishek.android.habitnme.models.HabitModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import nucleus.presenter.RxPresenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.schedulers.Schedulers;


public class ProgressPresenter extends RxPresenter<ProgressActivity> {
    @Inject
    RealmConfiguration realmConfiguration;
    private static final int REQ_DATA = 1;
    private static final int REQ_ALL = 2;

    public ProgressPresenter() {
        BaseApplication.getDataComponent().inject(this);
    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        final Observable<List<HabitModel>> observable = Observable.create(new Observable.OnSubscribe<List<HabitModel>>() {
            @Override
            public void call(Subscriber<? super List<HabitModel>> subscriber) {
                try {
                    subscriber.onNext(getAllHabits());
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        restartableLatestCache(REQ_ALL, new Func0<Observable<List<HabitModel>>>() {
            @Override
            public Observable<List<HabitModel>> call() {
                return observable;
            }
        }, new Action2<ProgressActivity, List<HabitModel>>() {
            @Override
            public void call(ProgressActivity progressActivity, List<HabitModel> habitModels) {
                progressActivity.onHabitsLoaded(habitModels);
            }
        }, new Action2<ProgressActivity, Throwable>() {
            @Override
            public void call(ProgressActivity progressActivity, Throwable throwable) {
                progressActivity.onError(throwable);
            }
        });
        start(REQ_ALL);
    }

    public List<HabitDayLog> getProgress(long habitId, int month, int year) throws Exception{
        Realm realm = Realm.getInstance(realmConfiguration);
        List<HabitDayLog> habitDayLogs = realm.where(HabitDayLog.class).equalTo("habitModel.id", habitId)
                .equalTo("mm", month).equalTo("yyyy", year).findAll();
        realm.beginTransaction();
        for (HabitDayLog habitDayLog:
             habitDayLogs) {
            long count = habitDayLog.getCount();
            Calendar today = Calendar.getInstance();
            today.setTime(new Date());
            if (habitDayLog.getHabitModel().getType() == HabitModel.NUMBER_BASED) {
                Calendar cal = Calendar.getInstance();
                cal.set(habitDayLog.getYyyy(), habitDayLog.getMm(), habitDayLog.getDd());
                if (cal.before(today)) {
                    if (count <= habitDayLog.getHabitModel().getMaxAllowed() && count >= habitDayLog.getHabitModel().getMinAllowed()) {
                        habitDayLog.setState(HabitDayLog.STATE_SUCCESS);
                    } else {
                        habitDayLog.setState(HabitDayLog.STATE_MISSED);
                    }
                }
            }
        }
        try {

            realm.copyToRealmOrUpdate(habitDayLogs);
            realm.commitTransaction();
            List<HabitDayLog> list = realm.copyFromRealm(habitDayLogs);
            realm.close();
            return list;
        } catch (Exception e) {
            realm.cancelTransaction();
            realm.close();
            throw e;
        }

    }

    public void startFetch(final long habitId, final int month, final int year) {
        final Observable<List<HabitDayLog>> observable = Observable.create(new Observable.OnSubscribe<List<HabitDayLog>>() {
            @Override
            public void call(Subscriber<? super List<HabitDayLog>> subscriber) {
                try {
                    subscriber.onNext(getProgress(habitId, month, year));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
        restartableFirst(REQ_DATA, new Func0<Observable<List<HabitDayLog>>>() {
            @Override
            public Observable<List<HabitDayLog>> call() {
                return observable;
            }
        }, new Action2<ProgressActivity, List<HabitDayLog>>() {
            @Override
            public void call(ProgressActivity progressActivity, List<HabitDayLog> habitDayLogs) {
                progressActivity.onHabitLogLoaded(habitDayLogs);
            }
        }, new Action2<ProgressActivity, Throwable>() {
            @Override
            public void call(ProgressActivity progressActivity, Throwable throwable) {
                progressActivity.onError(throwable);
            }
        });
        start(REQ_DATA);
    }

    protected List<HabitModel> getAllHabits() {
        Realm realm = Realm.getInstance(realmConfiguration);
        List<HabitModel> realmModels = realm.where(HabitModel.class)
                .equalTo("uuid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .findAll();
        List<HabitModel> allHabits = realm.copyFromRealm(realmModels);
        realm.close();
        return  allHabits;
    }


}
