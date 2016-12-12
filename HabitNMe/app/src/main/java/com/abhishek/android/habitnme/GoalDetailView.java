package com.abhishek.android.habitnme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class GoalDetailView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_detail_view);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        CalendarPickerView calendarView = (CalendarPickerView) findViewById(R.id.calendar_select);
        Date today = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(today);
        Calendar c2 = new GregorianCalendar();
        c2.setTime(today);
        c.add(Calendar.DATE , -10);
        c2.add(Calendar.DATE , 30);
        Date min = c.getTime();
        Date max = c2.getTime();
        calendarView.init(min, max);
        //calendarView.highlightDates(Arrays.asList(new Date[]{today}));
        //calendarView.setCustomDayView();

    }

}
