package com.abhishek.android.habitnme;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;

import com.abhishek.android.habitnme.adapters.GoalDataAdapter;
import com.abhishek.android.habitnme.adapters.HabitLogAdapter;
import com.abhishek.android.habitnme.presenters.HomePagePresenter;

import java.util.Arrays;
import java.util.List;

import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(HomePagePresenter.class)
public class HomeActivity extends NucleusAppCompatActivity<HomePagePresenter>
        implements NavigationView.OnNavigationItemSelectedListener {

    HabitLogAdapter habitLogAdapter;

    public static final String AUTHORITY = "com.abhishek.android.habitnme.provider";
    // An authenticator type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "dummy.com";
    // The authenticator name
    public static final String ACCOUNT = "dummyaccount";

    public static final long SYNC_INTERVAL = 30;
    // Instance fields
    Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ChooseCategoryActivity.class);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        List<String> list = Arrays.asList(new String[]{"1","2", "3", "4"});
//        GoalDataAdapter goalDataAdapter = new GoalDataAdapter(this, list);

        habitLogAdapter = new HabitLogAdapter(this, null, false);
        //View view = layoutInflater.inflate(R.layout.content_home, (ViewGroup) parent);
        ListView lv = (ListView)findViewById(R.id.home_page_list);
        lv.setAdapter(habitLogAdapter);
        getPresenter().initPresenter(this,  savedInstanceState);

        mAccount = CreateSyncAccount(this);

        ContentResolver.addPeriodicSync(
                mAccount,
                AUTHORITY,
                Bundle.EMPTY,
                SYNC_INTERVAL);

        // Pass the settings flags by inserting them in a bundle
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        /*
         * Request the sync for the default authenticator, authority, and
         * manual sync settings
         */
        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);

    }


    /**
     * Create a new dummy authenticator for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {

        // Create the authenticator type and default authenticator
        Account newAccount = new Account(
                context.getString(R.string.app_name), ACCOUNT_TYPE);
        // Get an instance of the Android authenticator manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the authenticator and authenticator type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         *
         */
        if (null == accountManager.getPassword(newAccount)) {
            if (accountManager.addAccountExplicitly(newAccount, "", null)) {

            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(authenticator, AUTHORITY, 1)
             * here.
             */
                ContentResolver.addPeriodicSync(newAccount,
                        AUTHORITY,
                        Bundle.EMPTY,
                        30);

                ContentResolver.setSyncAutomatically(newAccount, AUTHORITY, true);

            } else {
                return null;
            /*
             * The authenticator exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
            }
        }
        return newAccount;
    }


//    @Override
//    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
////        LayoutInflater layoutInflater = ( LayoutInflater )context.
////                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////        List<String> list = Arrays.asList(new String[]{"1"});
////        GoalDataAdapter goalDataAdapter = new GoalDataAdapter(this, list);
////        //View view = layoutInflater.inflate(R.layout.content_home, (ViewGroup) parent);
////        ListView lv = (ListView)parent.findViewById(R.id.home_page_list);
////        lv.setAdapter(goalDataAdapter);
//        return parent;
//    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_progress) {
            Intent intent = new Intent(this, ProgressActivity.class);
            startActivity(intent);
        }
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onDataLoaded(Cursor cursor) {
        habitLogAdapter.swapCursor(cursor);
    }

}
