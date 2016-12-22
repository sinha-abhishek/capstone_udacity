package com.abhishek.android.habitnme.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class HabitDayLog extends RealmObject {
    @PrimaryKey
    private String key;
    private int dd;
    private int mm;
    private int yyyy;
    private int dayOfYear;
    private HabitModel habitModel;
    private int count;
    private Integer state;

    public static final int STATE_UNTRACKED = 1;
    public static final int STATE_MISSED = 2;
    public static final int STATE_SUCCESS = 3;

    public HabitDayLog() {
    }

    public HabitDayLog(int count, int dd, HabitModel habitModel, int mm, int yyyy, int dayOfYear) {
        this.count = count;
        this.dd = dd;
        this.habitModel = habitModel;
        this.mm = mm;
        this.yyyy = yyyy;
        this.dayOfYear = dayOfYear;
        key = "key_"+habitModel.getId()+"_"+dayOfYear+"_"+yyyy;
        this.state = STATE_UNTRACKED;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDd() {
        return dd;
    }

    public void setDd(int dd) {
        this.dd = dd;
    }

    public HabitModel getHabitModel() {
        return habitModel;
    }

    public void setHabitModel(HabitModel habitModel) {
        this.habitModel = habitModel;
    }

    public int getMm() {
        return mm;
    }

    public void setMm(int mm) {
        this.mm = mm;
    }

    public int getYyyy() {
        return yyyy;
    }

    public void setYyyy(int yyyy) {
        this.yyyy = yyyy;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(int dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
