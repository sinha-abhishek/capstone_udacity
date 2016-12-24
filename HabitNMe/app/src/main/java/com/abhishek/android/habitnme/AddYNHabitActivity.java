package com.abhishek.android.habitnme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.abhishek.android.habitnme.models.HabitModel;
import com.abhishek.android.habitnme.presenters.AddHabitPresenter;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(AddHabitPresenter.class)
public class AddYNHabitActivity extends NucleusAppCompatActivity<AddHabitPresenter> implements AddHabitPresenter.AddHabitListener {
    @BindView(R.id.habit_name_text)
    EditText habitName;
    @BindView(R.id.yes_no_action_selector)
    Spinner yesNoActionSelectionSpinner;
    @BindView(R.id.habit_describe_text)
    EditText yesNoHabitDescription;
    @BindView(R.id.num_times_a_week_selector)
    Spinner numTimesAWeekSelectionSpinner;
    @BindView(R.id.btn_submit_habit)
    AppCompatButton saveHabit;

    String category;
    String yesNoAction;
    int numTimesAWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ynhabit);
        ButterKnife.bind(this);
        Intent i = getIntent();
        category = i.getStringExtra("category");

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.yes_no_selector_array,
                R.layout.spinner_text_layout);
        adapter1.setDropDownViewResource(R.layout.spinner_text_layout);
        yesNoActionSelectionSpinner.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.timesAWeek,
                R.layout.spinner_text_layout);
        adapter2.setDropDownViewResource(R.layout.spinner_text_layout);
        numTimesAWeekSelectionSpinner.setAdapter(adapter2);

        yesNoActionSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yesNoAction = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        numTimesAWeekSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numTimesAWeek = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @OnClick(R.id.btn_submit_habit)
    public void onSubmit() {
        if(habitName.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.please_enter_name), Toast.LENGTH_LONG).show();
            return;
        }
        if (yesNoAction.isEmpty() || numTimesAWeek <= 0) {
            Toast.makeText(this, getString(R.string.please_select), Toast.LENGTH_LONG).show();
            return;
        }
        String name = habitName.getText().toString();
        long dateAdded = (new Date()).getTime();
        int type = HabitModel.YES_NO;
        String description;
        int timesAWeekToDo;
        int minAllowed;
        int maxAllowed;
        String action = getResources().getStringArray(R.array.yes_no_selector_array)[0];
        timesAWeekToDo = numTimesAWeek;
        description = yesNoAction + " " + yesNoHabitDescription.getText().toString();
        if (yesNoAction.equals(action)) {
            minAllowed = maxAllowed = 1;
        } else {
            minAllowed = maxAllowed = 0;
        }
        getPresenter().createHabit(name, dateAdded, category, description, maxAllowed, minAllowed, type, timesAWeekToDo);
    }

    @Override
    public void onHabitAdded(boolean isSuccess) {
        Toast.makeText(this, R.string.add_success, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finishAffinity();
    }
}
