package com.abhishek.android.habitnme.models;


import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class HabitModel extends RealmObject {
    @PrimaryKey
    private long id;
    private String name;
    private long dataAdded;
    private String category;
    private String description;
    private int type;
    private int timesAWeek;
    private int minAllowed;
    private int maxAllowed;

    public static final int YES_NO = 1;
    public static final int NUMBER_BASED = 2;

    public HabitModel() {
    }

    public HabitModel(long id, String category, String description, int maxAllowed,
                      int minAllowed, String name, int timesAWeek, int type) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.maxAllowed = maxAllowed;
        this.minAllowed = minAllowed;
        this.name = name;
        this.timesAWeek = timesAWeek;
        this.type = type;
    }

    public long getDataAdded() {
        return dataAdded;
    }

    public void setDataAdded(long dataAdded) {
        this.dataAdded = dataAdded;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxAllowed() {
        return maxAllowed;
    }

    public void setMaxAllowed(int maxAllowed) {
        this.maxAllowed = maxAllowed;
    }

    public int getMinAllowed() {
        return minAllowed;
    }

    public void setMinAllowed(int minAllowed) {
        this.minAllowed = minAllowed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimesAWeek() {
        return timesAWeek;
    }

    public void setTimesAWeek(int timesAWeek) {
        this.timesAWeek = timesAWeek;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
