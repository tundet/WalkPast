package com.example.ryu.walkpast.Model;

import android.content.Context;

import com.example.ryu.walkpast.Database.DatabaseAdapter;
import com.example.ryu.walkpast.R;

/**
 * STORY
 * consists of Pages and Choices that are placed into the database
 * strings are changed based on system language
 * Created by RYU on 9/18/2017.
 */

public class Story {

    private Page[] mPages;
    private Choice[] mChoices;
    private DatabaseAdapter dbAdapter;

    /*
     INSERT STORY INTO DATABASE ON INITIALIZATION
     //TODO: insert only once
     */
    public Story(Context context) {

        dbAdapter = new DatabaseAdapter(context);
        mPages = new Page[7]; //Stories array

        mPages[0] = new Page(context.getString(R.string.page0), 0, 1, "bg0", 0);
        mPages[1] = new Page(context.getString(R.string.page1), 2, 3, "bg1", 10);
        mPages[2] = new Page(context.getString(R.string.page2), 4, 5, "bg2", 20);
        mPages[3] = new Page(context.getString(R.string.page3), 6, 7, "bg3", 30);
        mPages[4] = new Page(context.getString(R.string.page4), 8, 9, "bg4", 40);
        mPages[5] = new Page(context.getString(R.string.page5), 10, "bg6", 50);
        mPages[6] = new Page(context.getString(R.string.page6), 11, "bg5", 60);

        mChoices = new Choice[12];
        mChoices[0] = new Choice(context.getString(R.string.choice0), 1);
        mChoices[1] = new Choice(context.getString(R.string.choice1), 2);
        mChoices[2] = new Choice(context.getString(R.string.choice2), 3);
        mChoices[3] = new Choice(context.getString(R.string.choice3), 4);
        mChoices[4] = new Choice(context.getString(R.string.choice4), 4);
        mChoices[5] = new Choice(context.getString(R.string.choice5), 6);
        mChoices[6] = new Choice(context.getString(R.string.choice6), 5);
        mChoices[7] = new Choice(context.getString(R.string.choice7), 4);
        mChoices[8] = new Choice(context.getString(R.string.choice8), 5);
        mChoices[9] = new Choice(context.getString(R.string.choice9), 6);
        mChoices[10] = new Choice(context.getString(R.string.choice10), 0);
        mChoices[11] = new Choice(context.getString(R.string.choice11), 0);

        for (Page page : mPages) {
            dbAdapter.savePage(page);
        }
        for (Choice choice : mChoices) {
            dbAdapter.saveChoice(choice);
        }

    }
}