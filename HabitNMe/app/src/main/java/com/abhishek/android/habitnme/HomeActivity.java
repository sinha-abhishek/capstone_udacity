package com.abhishek.android.habitnme;

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
