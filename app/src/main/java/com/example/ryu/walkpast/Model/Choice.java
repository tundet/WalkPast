package com.example.ryu.walkpast.Model;

/**
 * Choice has text that will show up on the choice button and a page number that will appear from
 * clicking it.
 * Created by RYU on 9/18/2017.
 */

public class Choice {
    private String mText; //text that will show on the button
    private int mNextPage; //store the next page number

    Choice(String text, int nextPage) {
        mText = text;
        mNextPage = nextPage;
    }

    public String getText() {
        return mText;
    }

    public int getNextPage() {
        return mNextPage;
    }
}