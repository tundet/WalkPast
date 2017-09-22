package com.example.ryu.walkpast.Model;

/**
 * Player's information.
 * Created by RYU on 9/21/2017.
 */

public class Player {

    private int totalSteps;
    private int steps;

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
}
