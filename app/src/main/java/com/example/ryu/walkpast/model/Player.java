package com.example.ryu.walkpast.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * PLAYER
 * has two counted steps; how many steps have been taken in total and how many on current Page.
 * Created by RYU on 9/21/2017.
 */

public class Player {

    private String name;
    private String macAddress;
    private int totalSteps;
    private int steps;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private int currentPage;

    public Player(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
        if (prefs.getString("Name", null) == null) {
            editor.putString("Name", "");
            editor.apply();
        }
        if (prefs.getString("MetaWear", null) == null) {
            editor.putString("MetaWear", "");
            editor.apply();
        }
    }

    public void setName(String name) {
        this.name = name;
        editor.putString("Name", name);
        editor.apply();
    }

    public String getName() {
        name = prefs.getString("Name", "");
        return name;
    }

    public void setTotalSteps(int steps) {
        this.totalSteps += steps;
    }
    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public int getSteps() {
        return steps;
    }

    public void setAddress(String address) {
        this.macAddress = address;
        editor.putString("MetaWear", macAddress);
        editor.apply();
    }

    public String getAddress() {
        macAddress = prefs.getString("MetaWear", "");
        return macAddress;
    }
}
