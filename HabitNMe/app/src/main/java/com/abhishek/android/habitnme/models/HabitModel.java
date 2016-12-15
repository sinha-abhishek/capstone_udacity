package com.abhishek.android.habitnme.models;


import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class HabitModel extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;
    private long dataAdded;
    private String category;
    private String description;
    private String type;
    private String timesAWeek;
    private String minAllowed;
    private String maxAllowed;

    public HabitModel(String id, String category, String description, String maxAllowed,
                      String minAllowed, String name, String timesAWeek, String type) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getMaxAllowed() {
        return maxAllowed;
    }

    public void setMaxAllowed(String maxAllowed) {
        this.maxAllowed = maxAllowed;
    }

    public String getMinAllowed() {
        return minAllowed;
    }

    public void setMinAllowed(String minAllowed) {
        this.minAllowed = minAllowed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimesAWeek() {
        return timesAWeek;
    }

    public void setTimesAWeek(String timesAWeek) {
        this.timesAWeek = timesAWeek;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
