package com.abhishek.android.habitnme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.abhishek.android.habitnme.adapters.CategoryListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseCategoryActivity extends AppCompatActivity {
    @BindView(R.id.choose_category_list)
    ListView chooseCategoryList;

    CategoryListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        ButterKnife.bind(this);
        adapter = new CategoryListAdapter();
        chooseCategoryList.setAdapter(adapter);

        chooseCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = (String)adapter.getItem(position);
                Intent i = new Intent(ChooseCategoryActivity.this, TypeSelectionActivity.class);
                i.putExtra("category", text);
                startActivity(i);
            }
        });
    }
}
