package com.abhishek.android.habitnme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    @BindView(R.id.habit_edit_view)
    LinearLayout habitEditView;

    String category;
    String yesNoAction;
    int numTimesAWeek;

    long intentId ;
    String intentName;
    int intentTimesAWeek;
    int intentMin;
    int intentMax;
    String intentDesc;

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

        Intent intent = getIntent();
        if (intent.getBooleanExtra("isEdit", false)) {
            intentId = intent.getLongExtra(HabitDataProvider.ID, 0);
            intentName = intent.getStringExtra(HabitDataProvider.NAME);
            intentTimesAWeek = intent.getIntExtra(HabitDataProvider.TIMESAWEEK, 0);
            intentMin = intent.getIntExtra(HabitDataProvider.MIN_ALLOWED, 0);
            intentMax = intent.getIntExtra(HabitDataProvider.MAX_ALLOWED, 0);
            intentDesc = intent.getStringExtra(HabitDataProvider.DESCRIPTION);
            habitEditView.setVisibility(View.VISIBLE);
            saveHabit.setVisibility(View.GONE);
            habitName.setText(intentName);
            if (intentMin == 0) {
                yesNoActionSelectionSpinner.setSelection(1);
            } else {
                yesNoActionSelectionSpinner.setSelection(0);
            }
            numTimesAWeekSelectionSpinner.setSelection(intentTimesAWeek - 1);
            String action = yesNoActionSelectionSpinner.getSelectedItem().toString();
            String description = intentDesc.substring(action.length());
            description = description.substring(0, description.indexOf(String.valueOf(intentTimesAWeek)));
            yesNoHabitDescription.setText(description);
        } else {
            habitEditView.setVisibility(View.GONE);
            saveHabit.setVisibility(View.VISIBLE);
        }

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
        description = yesNoAction + " " + yesNoHabitDescription.getText().toString() + " "+ numTimesAWeek + " "+getString(R.string.num_times_a_week_text);
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

    @OnClick(R.id.btn_delete_habit)
    public void deleteHabit() {
        getPresenter().delete(intentId);
    }

    @OnClick(R.id.btn_done_habit)
    public void onEditClick() {
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
        description = yesNoAction + " " + yesNoHabitDescription.getText().toString() + " "+ numTimesAWeek + " "+getString(R.string.num_times_a_week_text);
        if (yesNoAction.equals(action)) {
            minAllowed = maxAllowed = 1;
        } else {
            minAllowed = maxAllowed = 0;
        }
        getPresenter().updateHabit(intentId, name, dateAdded, category, description, maxAllowed, minAllowed, type, timesAWeekToDo);
    }
}
