package com.abhishek.android.habitnme;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.abhishek.android.habitnme.models.HabitDayLog;
import com.abhishek.android.habitnme.models.HabitModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class HabitDataProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.abhishek.android.habitnme.provider";
    public static final String URL = "content://"+PROVIDER_NAME ;
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String NAME = "name";
    public static final String DATEADD = "dateAdded";
    public static final String ID = "_id";
    public static final String TYPE = "type";
    public static final String CATEGORY = "category";
    public static final String DESCRIPTION = "description";
    public static final String MAX_ALLOWED = "maxAllowed";
    public static final String TIMESAWEEK = "timesaweek";
    public static final String MIN_ALLOWED = "minAllowed";
    public static final String DD = "dd";
    public static final String MM = "mm";
    public static final String YYYY = "yyyy";
    public static final String COUNT = "count";
    public static final String ISSUCCESS = "isSuccess";

    static final int HABIT_ALL = 1;
    static final int HABIT_ONE_ID = 2;
    static final int HABIT_CATEGORY = 3;
    static final int HABIT_ONE_NAME = 4;
    static final int HABIT_ALL_LOG = 5;
    static final int HABIT_ONE_ID_LOG = 6;
    static final int HABIT_CATEGORY_LOG = 7;
    static final int HABIT_ONE_NAME_LOG = 8;

    static final UriMatcher uriMatcher;

    @Inject
    RealmConfiguration realmConfiguration;

    static {
            uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
            uriMatcher.addURI(PROVIDER_NAME,"habits",HABIT_ALL);
            uriMatcher.addURI(PROVIDER_NAME,"habit/*", HABIT_ONE_ID);
            uriMatcher.addURI(PROVIDER_NAME,"habits/category",HABIT_CATEGORY);
            uriMatcher.addURI(PROVIDER_NAME,"habit/name/*", HABIT_ONE_NAME);
            uriMatcher.addURI(PROVIDER_NAME,"habitslog",HABIT_ALL_LOG);
            uriMatcher.addURI(PROVIDER_NAME,"habitlog/*", HABIT_ONE_ID_LOG);
            uriMatcher.addURI(PROVIDER_NAME,"habitslog/category",HABIT_CATEGORY_LOG);
            uriMatcher.addURI(PROVIDER_NAME,"habitlog/name/*", HABIT_ONE_NAME_LOG);

            }

    public HabitDataProvider() {

    }
    @Override
    public boolean onCreate() {
        return true;
    }



    private Cursor toCursor(List<HabitModel> habitModels) {
        String[] columns = new String[] {ID, NAME, TYPE, DATEADD, CATEGORY, DESCRIPTION, MAX_ALLOWED, MIN_ALLOWED, TIMESAWEEK};
        MatrixCursor matrixCursor = new MatrixCursor(columns);
        for (HabitModel model:
             habitModels) {
            Object[] rowData = new Object[]{model.getId(), model.getName(), model.getType(),model.getDataAdded(), model.getCategory(),
            model.getDataAdded(), model.getMaxAllowed(), model.getMinAllowed(), model.getTimesAWeek()} ;
            matrixCursor.addRow(rowData);
        }
        return matrixCursor;
    }

    private Cursor logToCursor(List<HabitDayLog> habitDayLogs) {
        String[] columns = new String[] {ID, NAME, TYPE, DATEADD, CATEGORY, DESCRIPTION, MAX_ALLOWED,
                MIN_ALLOWED, TIMESAWEEK, DD, MM, YYYY, COUNT};
        MatrixCursor matrixCursor = new MatrixCursor(columns);
        for (HabitDayLog habitLog:
             habitDayLogs) {
            HabitModel model = habitLog.getHabitModel();
            Object[] rowData = new Object[]{model.getId(), model.getName(), model.getType(),model.getDataAdded(), model.getCategory(),
                    model.getDataAdded(), model.getMaxAllowed(), model.getMinAllowed(),
                    model.getTimesAWeek(), habitLog.getDd(), habitLog.getMm(), habitLog.getYyyy(), habitLog.getCount()} ;
            matrixCursor.addRow(rowData);
        }
        return matrixCursor;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (realmConfiguration == null) {
            BaseApplication.getDataComponent().inject(this);
        }
        Realm realm = Realm.getInstance(realmConfiguration);
        Cursor result = null;
        switch (uriMatcher.match(uri)) {
            case HABIT_ALL:
                List<HabitModel> allModels = realm.where(HabitModel.class).
                        equalTo("uuid", FirebaseAuth.getInstance().getCurrentUser().getUid()).findAll();
                result = toCursor(allModels);
                break;
            case HABIT_ONE_ID:
                List<HabitModel> modelsWithId = realm.where(HabitModel.class).equalTo("id",uri.getPathSegments().get(1)).findAll();
                result = toCursor(modelsWithId);
                break;
            case HABIT_CATEGORY:
                List<HabitModel> modelsWithCategory = realm.where(HabitModel.class).
                        equalTo("uuid", FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        equalTo("category",uri.getPathSegments().get(1)).findAll();
                result = toCursor(modelsWithCategory);
                break;
            case HABIT_ONE_NAME:
                List<HabitModel> modelsWithName = realm.where(HabitModel.class).equalTo("name",uri.getPathSegments().get(1)).findAll();
                result = toCursor(modelsWithName);
                break;
            case HABIT_ALL_LOG:
                List<HabitDayLog> allLogModels = realm.where(HabitDayLog.class).findAll();
                result = logToCursor(allLogModels);
                break;
            case HABIT_ONE_ID_LOG:
                List<HabitDayLog> modelsWithIdLog = realm.where(HabitDayLog.class).equalTo("habitModel.id",uri.getPathSegments().get(1)).findAll();
                result = logToCursor(modelsWithIdLog);
                break;
            case HABIT_CATEGORY_LOG:
                List<HabitDayLog> modelsWithCategoryLog = realm.where(HabitDayLog.class).equalTo("habitModel.category",uri.getPathSegments().get(1)).findAll();
                result = logToCursor(modelsWithCategoryLog);
                break;
            case HABIT_ONE_NAME_LOG:
                List<HabitDayLog> modelsWithNameLog = realm.where(HabitDayLog.class).equalTo("habitModel.name",uri.getPathSegments().get(1)).findAll();
                result = logToCursor(modelsWithNameLog);
                break;


        }
        return result;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
