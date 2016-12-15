package com.abhishek.android.habitnme.models;

import io.realm.RealmObject;


public class HabitDayLog extends RealmObject {
    private int dd;
    private int mm;
    private int yyyy;
    private HabitModel habitModel;
    private int count;
    private boolean isSuccess;

    public HabitDayLog(int count, int dd, HabitModel habitModel, int mm, int yyyy) {
        this.count = count;
        this.dd = dd;
        this.habitModel = habitModel;
        this.mm = mm;
        this.yyyy = yyyy;
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
}
