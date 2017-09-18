package com.example.ryu.walkpast.Model;

/**
 * Page has the story and two possible choices.
 * Created by RYU on 9/18/2017.
 */

public class Page {

    private String mText;
    private Choice mChoice1;
    private Choice mChoice2;

    Page(String text, Choice choice1, Choice choice2) {
        mText = text;
        mChoice1 = choice1;
        mChoice2 = choice2;
    }

    Page(String text, Choice choice1){
        mText = text;
        mChoice1 = choice1;
    }

    public String getText() {
        return mText;
    }

    public Choice getChoice1() {
        return mChoice1;
    }

    public Choice getChoice2() {
        return mChoice2;
    }
}
