package com.example.ryu.walkpast.model;

import android.content.Context;

import com.example.ryu.walkpast.R;

/**
 * STORY
 * consists of Pages and Choices that are placed into the database
 * strings are changed based on system language
 * Created by RYU on 9/18/2017.
 */

public class Story {

    private static Page[] pages;
    private static Choice[] choices;

    /*
     INSERT STORY INTO DATABASE ON INITIALIZATION
     //TODO: insert only once (after story is complete and doesn't need to be edited anymore)
     */
    public Story(Context context) {

        pages = new Page[7];

        //todo maybe give instructions in github on how to add extra stories

    /*
     CREATING YOUR OWN PAGES ARRAY:
     - Add a story string (preferably from res so it can be translated)
     - Add a reference to a choice or two in choices array
     - Give the page a background
     - Add required steps needed to take to get to the next page
     */
        pages[0] = new Page(context.getString(R.string.page0), 0, 1, "bg0", 10);
        pages[1] = new Page(context.getString(R.string.page1), 2, 3, "bg1", 20);
        pages[2] = new Page(context.getString(R.string.page2), 4, 5, "bg2", 30);
        pages[3] = new Page(context.getString(R.string.page3), 6, 7, "bg3", 40);
        pages[4] = new Page(context.getString(R.string.page4), 8, 9, "bg4", 50);
        pages[5] = new Page(context.getString(R.string.page5), 10, "bg6", 0);
        pages[6] = new Page(context.getString(R.string.page6), 11, "bg5", 0);

    /*
     CREATING YOUR OWN CHOICES ARRAY:
     - Add a button string (preferably from res so it can be translated)
     - Add a reference to a page in pages array that it will take you to
     */
        choices = new Choice[12];
        choices[0] = new Choice(context.getString(R.string.choice0), 1);
        choices[1] = new Choice(context.getString(R.string.choice1), 2);
        choices[2] = new Choice(context.getString(R.string.choice2), 3);
        choices[3] = new Choice(context.getString(R.string.choice3), 4);
        choices[4] = new Choice(context.getString(R.string.choice4), 4);
        choices[5] = new Choice(context.getString(R.string.choice5), 6);
        choices[6] = new Choice(context.getString(R.string.choice6), 5);
        choices[7] = new Choice(context.getString(R.string.choice7), 4);
        choices[8] = new Choice(context.getString(R.string.choice8), 5);
        choices[9] = new Choice(context.getString(R.string.choice9), 6);
        //last choices' buttons should be configured in MainActivity to react in a different way from the rest
        choices[10] = new Choice(context.getString(R.string.choice10), 0);
        choices[11] = new Choice(context.getString(R.string.choice11), 0);

    }

    public static Page[] getPages() {
        return pages;
    }

    public static Choice[] getChoices() {
        return choices;
    }
}