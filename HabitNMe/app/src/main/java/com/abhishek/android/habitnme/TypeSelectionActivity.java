package com.abhishek.android.habitnme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;

import com.abhishek.android.habitnme.models.HabitModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TypeSelectionActivity extends AppCompatActivity {


    private String category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_selection);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        category = intent.getStringExtra("category");
    }

    @OnClick(R.id.yes_no_button)
    public void OnYesNoClick() {
        Intent intent = new Intent(this, AddYNHabitActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("type", HabitModel.YES_NO);
        startActivity(intent);
    }

    @OnClick(R.id.number_button)
    public void OnNumberClick() {
        Intent intent = new Intent(this, AddNumberActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("type", HabitModel.NUMBER_BASED);
        startActivity(intent);
    }
}
