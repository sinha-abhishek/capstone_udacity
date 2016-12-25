package com.abhishek.android.habitnme.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.AppCompatButton;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.abhishek.android.habitnme.BaseApplication;
import com.abhishek.android.habitnme.HabitDataProvider;
import com.abhishek.android.habitnme.ProgressActivity;
import com.abhishek.android.habitnme.R;
import com.abhishek.android.habitnme.models.HabitDayLog;
import com.abhishek.android.habitnme.models.HabitModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;

import static com.abhishek.android.habitnme.models.HabitDayLog.STATE_MISSED;


public class HabitLogAdapter extends CursorAdapter {
    @Inject
    protected RealmConfiguration realmConfiguration;

    public HabitLogAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        BaseApplication.getDataComponent().inject(this);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.habit_list_item, null);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ViewHolder holder = new ViewHolder(view);
        TextView title = holder.goalName;
        String titleText = cursor.getString(cursor.getColumnIndex(HabitDataProvider.NAME));
        title.setText(titleText);
        final Realm realm = Realm.getInstance(realmConfiguration);
        int id = cursor.getInt(cursor.getColumnIndex(HabitDataProvider.ID));
        final int max = cursor.getInt(cursor.getColumnIndex(HabitDataProvider.MAX_ALLOWED));
        final int min = cursor.getInt(cursor.getColumnIndex(HabitDataProvider.MIN_ALLOWED));
        int type = cursor.getInt(cursor.getColumnIndex(HabitDataProvider.TYPE));
        int timesAweek = cursor.getInt(cursor.getColumnIndex(HabitDataProvider.TIMESAWEEK));
        final List<HabitDayLog> habitDayLogs = realm.where(HabitDayLog.class).equalTo("habitModel.id", id)
                .findAllSorted("dayOfYear", Sort.DESCENDING);

        List<HabitDayLog> last7Days = new ArrayList<>();
        int successCount = 0;

        List<TextView> labels = holder.dayTexts;
        for (int i = 0 ; i < 7 ; i++) {
            final HabitDayLog dayLog = habitDayLogs.get(i);
            final TextView label = labels.get(i);
            label.setText(String.valueOf(habitDayLogs.get(i).getDd()));
            switch (dayLog.getState()) {
                case HabitDayLog.STATE_UNTRACKED:
                    label.setBackground(context.getResources().getDrawable(R.drawable.bg_untracked));
                    break;
                case HabitDayLog.STATE_MISSED :
                    label.setBackground(context.getResources().getDrawable(R.drawable.bg_missed));
                    break;
                case HabitDayLog.STATE_SUCCESS :
                    label.setBackground(context.getResources().getDrawable(R.drawable.bg_done));
                    break;
                default:
                    label.setBackground(context.getResources().getDrawable(R.drawable.bg_untracked));
                    break;

            }
            last7Days.add(habitDayLogs.get(i));
            label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dayLog.getHabitModel().getType() == HabitModel.YES_NO) {
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dialog_mark_done);
                        dialog.setTitle(dayLog.getHabitModel().getName());
                        final ToggleButton toggleButton = (ToggleButton) dialog.findViewById(R.id.toggle_done);
                        if (dayLog.getState() == HabitDayLog.STATE_SUCCESS) {
                            toggleButton.setChecked(true);
                        } else {
                            toggleButton.setChecked(false);
                        }
                        //TextView title = (TextView) dialog.findViewById(R.id.goal_title);
                        //title.setText(dayLog.getHabitModel().getName());
                        TextView desc = (TextView) dialog.findViewById(R.id.goal_description) ;
                        desc.setText(dayLog.getHabitModel().getDescription());
                        AppCompatButton button = (AppCompatButton) dialog.findViewById(R.id.btn_setcount);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                realm.beginTransaction();
                                if (toggleButton.isChecked()) {
                                    dayLog.setState(HabitDayLog.STATE_SUCCESS);
                                } else {
                                    dayLog.setState(HabitDayLog.STATE_MISSED);
                                }
                                realm.copyToRealmOrUpdate(dayLog);
                                realm.commitTransaction();
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                        int width = metrics.widthPixels;
                        int height = metrics.heightPixels;
                        dialog.getWindow().setLayout((5 * width)/7, (1 * height)/2);
                        dialog.show();

                    } else {
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.dialog_set_count);
                        dialog.setTitle(dayLog.getHabitModel().getName());
                        final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.goal_current_number);
                        numberPicker.setMinValue(0);
                        numberPicker.setMaxValue(10000);
                        numberPicker.setValue(dayLog.getCount());
                        TextView desc = (TextView) dialog.findViewById(R.id.goal_description) ;
                        desc.setText(dayLog.getHabitModel().getDescription());
                        AppCompatButton button = (AppCompatButton) dialog.findViewById(R.id.btn_setcount);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                realm.beginTransaction();
                                dayLog.setCount(numberPicker.getValue());
                                if (dayLog.getCount() > max) {
                                    dayLog.setState(HabitDayLog.STATE_MISSED);
                                } else if (min > 0 && dayLog.getCount() > min) {
                                    dayLog.setState(HabitDayLog.STATE_SUCCESS);
                                }
                                if (min == max) {
                                    if (dayLog.getCount() == min) {
                                        dayLog.setState(HabitDayLog.STATE_SUCCESS);
                                    } else if (dayLog.getCount() > max) {
                                        dayLog.setState(HabitDayLog.STATE_MISSED);
                                    }
                                }
                                realm.copyToRealmOrUpdate(dayLog);
                                realm.commitTransaction();
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                        int width = metrics.widthPixels;
                        int height = metrics.heightPixels;
                        dialog.getWindow().setLayout((5 * width)/7, (4 * height)/5);
                        dialog.show();
                    }
                }

            });
            holder.goalProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProgressActivity.class);
                    long id = dayLog.getHabitModel().getId();
                    intent.putExtra("habitId", id);
                    context.startActivity(intent);
                }
            });
            if (dayLog.getState() == HabitDayLog.STATE_SUCCESS) {
                successCount++;
                continue;
            }

//            if (type == HabitModel.NUMBER_BASED) {
//                if(dayLog.getCount() <= max && dayLog.getCount() >= min) {
//                    realm.beginTransaction();
//                    successCount++;
//                }
//            }

        }
        float percent = ((float)successCount/timesAweek)*100;
        TextView goalPercent = holder.goalPercent;
        goalPercent.setText(String.valueOf(percent)+ "%");

    }

    static class ViewHolder {
        @BindView(R.id.goal_name)
        TextView goalName;
        @BindView(R.id.goal_percent)
        TextView goalPercent;
        @BindView(R.id.day1)
        TextView day1;
        @BindView(R.id.day2)
        TextView day2;
        @BindView(R.id.day3)
        TextView day3;
        @BindView(R.id.day4)
        TextView day4;
        @BindView(R.id.day5)
        TextView day5;
        @BindView(R.id.day6)
        TextView day6;
        @BindView(R.id.day7)
        TextView day7;

        @BindView(R.id.goal_edit)
        TextView goalEdit;

        @BindView(R.id.goal_progress)
        TextView goalProgress;

        List<TextView> dayTexts = new ArrayList<>();

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            dayTexts = Arrays.asList(day7, day6, day5, day4, day3, day2, day1);
        }

    }
}
