package com.abhishek.android.habitnme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.abhishek.android.habitnme.models.HabitDayLog;
import com.abhishek.android.habitnme.models.HabitModel;
import com.abhishek.android.habitnme.presenters.AddHabitPresenter;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(AddHabitPresenter.class)
public class AddHabitActivity extends NucleusAppCompatActivity<AddHabitPresenter> implements AddHabitPresenter.AddHabitListener{
    @BindView(R.id.categorySelector)
    Spinner categorySelector;
    @BindView(R.id.yesNoSelection)
    RadioButton yesNoHabitSelectionRadio;
    @BindView(R.id.number_based_selection)
    RadioButton numberBaseRadioSelection;
    @BindView(R.id.habit_name_text)
    EditText habitName;
    @BindView(R.id.yes_no_action_selector)
    Spinner yesNoActionSelectionSpinner;
    @BindView(R.id.habit_describe_text)
    EditText yesNoHabitDescription;
    @BindView(R.id.num_times_a_week_selector)
    Spinner numTimesAWeekSelectionSpinner;
    @BindView(R.id.action_verb_id)
    EditText actionVerbText;
    @BindView(R.id.number_action_selector)
    Spinner numberActionSelectorSpinner;
    @BindView(R.id.number_habit_x_count)
    EditText numberHabitXCount;
    @BindView(R.id.number_habit_x_description)
    EditText numberHabitDescriptionText;
    @BindView(R.id.number_habit_x_metric)
    EditText numberHabitXMetric;
    @BindView(R.id.btn_submit_habit)
    AppCompatButton saveHabit;

    String category;
    String yesNoAction;
    int numTimesAWeek;
    String numberAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
        ButterKnife.bind(this);
        //Spinner spinner = (Spinner) findViewById(R.id.categorySelector);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        categorySelector.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.yes_no_selector_array,
                android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yesNoActionSelectionSpinner.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.timesAWeek,
                android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numTimesAWeekSelectionSpinner.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.metric_selection_array,
                android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numberActionSelectorSpinner.setAdapter(adapter3);


        categorySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
    public void onSubmit() {
        int type = yesNoHabitSelectionRadio.isChecked() ? HabitModel.YES_NO:  HabitModel.NUMBER_BASED;
        String name = habitName.getText().toString();
        long dateAdded = (new Date()).getTime();
        String description;
        int timesAWeekToDo;
        int minAllowed;
        int maxAllowed;
        if(yesNoHabitSelectionRadio.isChecked()) {
            timesAWeekToDo = numTimesAWeek;
            description = yesNoAction + " " + yesNoHabitDescription.getText().toString();
            if (yesNoAction.equals("I want to")) {
                minAllowed = maxAllowed = 1;
            } else {
                minAllowed = maxAllowed = 0;
            }
        } else {
            timesAWeekToDo = 7;
            description = actionVerbText.getText().toString() + " "+ numberAction+ " " +
                    numberHabitXCount.getText().toString() + numberHabitDescriptionText.getText().toString() +
                    numberHabitXMetric.getText().toString();
            if (numberAction.equals("at least")) {
                minAllowed = Integer.parseInt(numberHabitXCount.getText().toString());
                maxAllowed = minAllowed*1000;
            } else if(numberAction.equals("exactly")) {
                minAllowed = maxAllowed = Integer.parseInt(numberHabitXCount.getText().toString());
            } else {
                maxAllowed = Integer.parseInt(numberHabitXCount.getText().toString());
                minAllowed = 0;
            }
        }
        getPresenter().createHabit(name, dateAdded, category, description, maxAllowed, minAllowed, type, timesAWeekToDo);

    }

    @Override
    public void onHabitAdded(boolean isSuccess) {
        Toast.makeText(this, R.string.add_success, Toast.LENGTH_SHORT).show();
    }
}
