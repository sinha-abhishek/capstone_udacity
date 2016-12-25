package com.abhishek.android.habitnme;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.abhishek.android.habitnme.models.HabitDayLog;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * Implementation of App Widget functionality.
 */
public class HabitAppWidget extends AppWidgetProvider {

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        final CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.habit_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, HabitAppWidget.class));
            for (int i=0; i< appWidgetIds.length ; i++) {

                int appWidgetId = appWidgetIds[i];
                // Tell the AppWidgetManager to perform an update on the current app widget
                RemoteViews views = updateRemoteView(context,appWidgetId, appWidgetManager);
                appWidgetManager.updateAppWidget(appWidgetId, views);

            }
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
//            // Create an Intent to launch MainActivity
//            Intent intent1 = new Intent(context, MyStocksActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
//            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
//
//            // Set up the collection
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//                setRemoteAdapter(context, views);
//            } else {
//                setRemoteAdapterV11(context, views);
//            }
//            boolean useDetailActivity = false;
//            Intent clickIntentTemplate = new Intent(context, MyStocksActivity.class);
//            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
//                    .addNextIntentWithParentStack(clickIntentTemplate)
//                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//            views.setPendingIntentTemplate(R.id.weather_list, clickPendingIntentTemplate);
//            views.setEmptyView(R.id.weather_list, R.id.empty_view);
//            appWidgetManager.updateAppWidget(appWidgetIds, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_text);
            //super.onReceive(context,intent);
        }
    };


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = updateRemoteView(context,appWidgetId, appWidgetManager);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_text);

        Realm realm = Realm.getInstance(Utils.getInstance().getRealmConfiguration());
        RealmChangeListener<Realm> realmListener = new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                ComponentName component=new ComponentName(context,HabitAppWidget.class);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_text);
                //appWidgetManager.updateAppWidget(component, remoteView);
                for (int appWidgetId : appWidgetIds) {
                    RemoteViews views = updateRemoteView(context,appWidgetId, appWidgetManager);
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }

                //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_text);
            }
        };
        realm.addChangeListener(realmListener);

    }

    private RemoteViews updateRemoteView(Context context, int appWidgetId, AppWidgetManager appWidgetManager) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.habit_app_widget);

        Intent intent = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.appwidget_text, pendingIntent);
        //views.setOnClickPendingIntent(R.layout.habit_app_widget, pendingIntent);

        Intent svcIntent = new Intent(context, WidgetRemoteViewService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(
                svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        views.setRemoteAdapter(appWidgetId, R.id.appwidget_text,  svcIntent);

        views.setEmptyView(R.id.appwidget_text, R.id.empty_view);

        return views;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

