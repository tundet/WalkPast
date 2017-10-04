package com.example.ryu.walkpast.Model;

/**
 * CHOICE
 * has a text that will be shown on a button and integer to tell which Page the button will lead to.
 * Created by RYU on 9/18/2017.
 */

public class Choice {
    private String choiceText; //text that will show on the button
    private int nextPage; //store the next page number


    public Choice(String text, int page) {
        choiceText = text;
        nextPage = page + 1;
    }

    public String getText() {
        return choiceText;
    }

    public int getNextPage() {
        return nextPage;
    }
}