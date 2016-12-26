package com.abhishek.android.habitnme;

import android.content.Intent;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.abhishek.android.habitnme.models.HabitDayLog;
import com.abhishek.android.habitnme.models.HabitModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static android.R.style.Widget;


public class WidgetRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            List<HabitModel> allHabits;
            @Override
            public void onCreate() {
                Realm realm = Realm.getInstance(Utils.getInstance().getRealmConfiguration());
                List<HabitModel> habitModels = realm.where(HabitModel.class)
                        .equalTo("uuid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .findAllSorted("dataAdded");
                allHabits = realm.copyFromRealm(habitModels);
                realm.close();
            }

            @Override
            public void onDataSetChanged() {
                long identityToken = Binder.clearCallingIdentity();
                Binder.restoreCallingIdentity(identityToken);
                Realm realm = Realm.getInstance(Utils.getInstance().getRealmConfiguration());
                List<HabitModel> habitModels = realm.where(HabitModel.class)
                        .equalTo("uuid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .findAllSorted("dataAdded");
                allHabits = realm.copyFromRealm(habitModels);
                realm.close();
            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {

                return allHabits.size();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        allHabits == null || position >= allHabits.size()) {
                    return null;
                }
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_list_item);
                HabitModel habitModel = allHabits.get(position);
                String name = habitModel.getName();
                boolean isDone = false;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                Realm realm = Realm.getInstance(Utils.getInstance().getRealmConfiguration());
                List<HabitDayLog> todayLogs = realm.where(HabitDayLog.class)
                        .equalTo("habitModel.id", habitModel.getId())
                        .equalTo("yyyy", calendar.get(Calendar.YEAR))
                        .equalTo("dayOfYear", calendar.get(Calendar.DAY_OF_YEAR))
                        .findAll();
                if (todayLogs.isEmpty() || todayLogs.get(0).getState() == HabitDayLog.STATE_UNTRACKED) {
                    isDone = false;
                } else {
                    isDone = true;
                }
                remoteViews.setTextViewText(R.id.widget_item_text, name);
                if (isDone) {
                    if (todayLogs.get(0).getState() == HabitDayLog.STATE_MISSED) {
                        remoteViews.setTextViewText(R.id.widget_item_check, getString(R.string.missed_text));
                    } else {
                        remoteViews.setTextViewText(R.id.widget_item_check, getString(R.string.done_text));
                    }
                } else {
                    remoteViews.setTextViewText(R.id.widget_item_check, getString(R.string.notdone_text));
                }
                Intent fillInIntent = new Intent();
                //fillInIntent.putExtra(, position);
                remoteViews.setOnClickFillInIntent(R.id.widget_item_text, fillInIntent);
                //remoteViews.setTextColor();
                return remoteViews;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
