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
public class AddNumberActivity extends NucleusAppCompatActivity<AddHabitPresenter> implements AddHabitPresenter.AddHabitListener {
    @BindView(R.id.habit_name_text)
    protected EditText habitName;
    @BindView(R.id.number_action_selector)
    protected Spinner numberActionSelectorSpinner;
    @BindView(R.id.number_habit_x_count)
    protected EditText numberHabitXCount;
    @BindView(R.id.action_verb_id)
    protected EditText actionVerbText;
    @BindView(R.id.number_habit_x_metric)
    protected EditText numberHabitXMetric;
    @BindView(R.id.btn_submit_habit)
    protected AppCompatButton saveHabit;

    protected String category;
    protected String numberAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_number);
        ButterKnife.bind(this);
        Intent i = getIntent();
        category = i.getStringExtra("category");

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.metric_selection_array,
                R.layout.spinner_text_layout);
        adapter3.setDropDownViewResource(R.layout.spinner_text_layout);
        numberActionSelectorSpinner.setAdapter(adapter3);

        numberActionSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numberAction = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @OnClick(R.id.btn_submit_habit)
    public void OnSubmit() {
        int type =  HabitModel.NUMBER_BASED;
        String name = habitName.getText().toString();
        long dateAdded = (new Date()).getTime();
        String description;
        int timesAWeekToDo = 7;
        int minAllowed;
        int maxAllowed;
        if (name.isEmpty()) {
            Toast.makeText(this, getString(R.string.please_enter_name), Toast.LENGTH_LONG).show();
            return;
        }
        if (actionVerbText.getText().toString().isEmpty() || numberHabitXCount.getText().toString().isEmpty()
                || numberHabitXMetric.getText().toString().isEmpty() || numberAction.isEmpty()) {
            Toast.makeText(this, getString(R.string.please_fill_fields), Toast.LENGTH_LONG).show();
            return;
        }
        description = actionVerbText.getText().toString() + " "+ numberAction+ " " +
                numberHabitXCount.getText().toString()  +
                numberHabitXMetric.getText().toString();
        if (numberAction.equals(getResources().getStringArray(R.array.metric_selection_array)[0])) {
            minAllowed = Integer.parseInt(numberHabitXCount.getText().toString());
            maxAllowed = (1+minAllowed)*1000;
        } else if(numberAction.equals(getResources().getStringArray(R.array.metric_selection_array)[2])) {
            minAllowed = maxAllowed = Integer.parseInt(numberHabitXCount.getText().toString());
        } else {
            maxAllowed = Integer.parseInt(numberHabitXCount.getText().toString());
            minAllowed = 0;
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
