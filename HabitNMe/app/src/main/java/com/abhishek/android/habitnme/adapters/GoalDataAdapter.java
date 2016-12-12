package com.abhishek.android.habitnme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abhishek.android.habitnme.R;

import java.util.ArrayList;
import java.util.List;


public class GoalDataAdapter extends BaseAdapter {
    private static LayoutInflater inflater=null;
    List<String> goals = new ArrayList<>();
    public GoalDataAdapter(Context context, List<String> strings) {
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.goals = strings;
    }

    @Override
    public int getCount() {
        return goals.size();
    }

    @Override
    public Object getItem(int position) {
        return goals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.habit_list_item, null);
        TextView title = (TextView)view.findViewById(R.id.goal_name);
        title.setText(goals.get(position));
        return view;
    }
}
