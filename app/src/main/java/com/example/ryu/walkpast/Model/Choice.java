package com.example.ryu.walkpast.Model;

/**
 * Choice has text that will show up on the choice button and a page number that will appear from
 * clicking it.
 * Created by RYU on 9/18/2017.
 */

public class Choice {
    private String choiceText; //text that will show on the button
    private int nextPage; //store the next page number

    Choice(String text, int page) {
        choiceText = text;
        nextPage = page;
    }

    public String getText() {
        return choiceText;
    }

    public int getNextPage() {
        return nextPage;
    }
}