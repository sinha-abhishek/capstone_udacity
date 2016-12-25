package com.abhishek.android.habitnme;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.abhishek.android.habitnme.models.HabitDayLog;
import com.abhishek.android.habitnme.models.HabitModel;
import com.abhishek.android.habitnme.presenters.ProgressPresenter;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(ProgressPresenter.class)
public class ProgressActivity extends NucleusAppCompatActivity<ProgressPresenter> {
    protected CaldroidFragment caldroidFragment;

    @BindView(R.id.goal_select_spinner)
    protected Spinner goalSelectSpinner;

    long habitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ButterKnife.bind(this);
        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar_fragment, caldroidFragment);
        t.commit();

        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {

            }

            @Override
            public void onChangeMonth(int month, int year) {
                super.onChangeMonth(month, year);
                if (habitId >= 0) {
                    getPresenter().startFetch(habitId, month-1, year);
                }
            }
        });
        Intent intent = getIntent();
        habitId = intent.getLongExtra("habitId", -1);
        if (habitId != -1) {
            goalSelectSpinner.setVisibility(View.GONE);
        } else {
            goalSelectSpinner.setVisibility(View.VISIBLE);


        }
        getPresenter().startFetch(habitId, cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));


    }

    public void onHabitsLoaded(List<HabitModel> habitModelList) {
        ArrayList<String> names = new ArrayList<>();
        final ArrayList<Long> ids = new ArrayList<>();
        for (HabitModel model : habitModelList) {
            names.add(model.getName());
            ids.add(model.getId());
        }
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_text_layout, names);
        adapter.setDropDownViewResource(R.layout.spinner_text_layout);
        goalSelectSpinner.setAdapter(adapter);
        goalSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                habitId = ids.get(position);
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                getPresenter().startFetch(habitId, cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onHabitLogLoaded(List<HabitDayLog> monthLogs) {
        for (HabitDayLog dayLog:
             monthLogs) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(dayLog.getYyyy(), dayLog.getMm(), dayLog.getDd());
            Date date = calendar.getTime();
            if (dayLog.getState() == HabitDayLog.STATE_SUCCESS) {
                caldroidFragment.setBackgroundDrawableForDate(getResources().getDrawable(R.drawable.bg_done), date);
            } else if (dayLog.getState() == HabitDayLog.STATE_MISSED) {
                caldroidFragment.setBackgroundDrawableForDate(getResources().getDrawable(R.drawable.bg_missed), date);
            } else {
                caldroidFragment.setBackgroundDrawableForDate(getResources().getDrawable(R.drawable.bg_untracked), date);
            }
        }
        caldroidFragment.refreshView();
    }

    public void onError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
