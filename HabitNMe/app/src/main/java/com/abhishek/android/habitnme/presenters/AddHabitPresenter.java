package com.abhishek.android.habitnme.presenters;

import android.util.Log;

import com.abhishek.android.habitnme.BaseApplication;
import com.abhishek.android.habitnme.models.HabitDayLog;
import com.abhishek.android.habitnme.models.HabitModel;

import java.util.Calendar;
import java.util.Date;

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


public class AddHabitPresenter extends RxPresenter<AddHabitPresenter.AddHabitListener> {

    public interface AddHabitListener {
        void onHabitAdded(boolean isSuccess);
    }
    @Inject
    RealmConfiguration realmConfiguration;

    private static final int SAVE_DATA = 1;

    public AddHabitPresenter() {
        BaseApplication.getDataComponent().inject(this);
    }

    private void saveHabit(String name, long date, String category , String description, int max, int min,
                           int type, int timesAWeek) throws  Exception{
        Realm realm = Realm.getInstance(realmConfiguration);
        try {
            long count = realm.where(HabitModel.class).count();
            realm.beginTransaction();
            long id = count+1;
            HabitModel habitModel = new HabitModel(id, category, description, max, min, name, timesAWeek, type);
            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            int mm = calendar.get(Calendar.MONTH);
            int yy = calendar.get(Calendar.YEAR);
            int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

//            if (count <= habitModel.getMaxAllowed() && count >= habitModel.getMinAllowed()) {
//                habitDayLog.isSuccess() = true;
//            }
            realm.copyToRealmOrUpdate(habitModel);
            HabitDayLog habitDayLog = new HabitDayLog(0, dd, habitModel, mm, yy, dayOfYear);
            habitDayLog.setState(HabitDayLog.STATE_UNTRACKED);
            realm.copyToRealmOrUpdate(habitDayLog);
            for (int i = 1 ; i <= 7 ; i++) {
                calendar.add(Calendar.DATE, -1);
                int dd1 = calendar.get(Calendar.DAY_OF_MONTH);
                int mm1 = calendar.get(Calendar.MONTH);
                int yy1 = calendar.get(Calendar.YEAR);
                int dayOfYear1 = calendar.get(Calendar.DAY_OF_YEAR);
                HabitDayLog lastDay = new HabitDayLog(0, dd1, habitModel, mm1, yy1, dayOfYear1);
                lastDay.setState(HabitDayLog.STATE_UNTRACKED);
                realm.copyToRealmOrUpdate(lastDay);
            }

            realm.commitTransaction();
        } catch (Exception e ) {
            throw e;
        } finally {
            realm.close();
        }
    }

    public void createHabit(final String name, final long date, final String category , final String description, final int max, final int min,
                            final int type, final int timesAWeek) {
        final Observable<Boolean> observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    saveHabit(name, date, category, description, max, min, type, timesAWeek);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        restartableLatestCache(SAVE_DATA, new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                return observable;
            }
        }, new Action2<AddHabitListener, Boolean>() {
            @Override
            public void call(AddHabitListener addHabitListener, Boolean aBoolean) {
                addHabitListener.onHabitAdded(aBoolean);
            }
        }, new Action2<AddHabitListener, Throwable>() {
            @Override
            public void call(AddHabitListener addHabitListener, Throwable throwable) {
                addHabitListener.onHabitAdded(false);
                Log.e(AddHabitPresenter.class.getSimpleName(), throwable.getMessage());
            }
        });
        start(SAVE_DATA);
    }
}
