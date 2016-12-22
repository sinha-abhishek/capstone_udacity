package com.abhishek.android.habitnme.presenters;

import nucleus.presenter.RxPresenter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import com.abhishek.android.habitnme.BaseApplication;
import com.abhishek.android.habitnme.HabitDataProvider;
import com.abhishek.android.habitnme.HomeActivity;

import javax.inject.Inject;


public class HomePagePresenter extends RxPresenter<HomeActivity> implements LoaderManager.LoaderCallbacks<Cursor> {

    @Inject
    Context context;
    private static final int LOADER_ID = 1;
    private HomeActivity homeActivity;

    public HomePagePresenter() {
        BaseApplication.getDataComponent().inject(this);
    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

    }

    public void initPresenter(HomeActivity homeActivity, Bundle savedState) {
        this.homeActivity = homeActivity;
        homeActivity.getLoaderManager().initLoader(LOADER_ID, savedState, this);
    }

    @Override
    protected void onTakeView(HomeActivity homeActivity) {
        super.onTakeView(homeActivity);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                homeActivity, HabitDataProvider.CONTENT_URI.buildUpon().appendPath("habits").build(),
                null,
                null,
                null,
                null
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        homeActivity.onDataLoaded(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        homeActivity.onDataLoaded(null);
    }
}
