package com.example.ryu.walkpast.Model;

import android.content.Context;

import com.example.ryu.walkpast.Database.DatabaseAdapter;
import com.example.ryu.walkpast.R;

/**
 * Story consists of pages and choices that will take to different pages.
 * Created by RYU on 9/18/2017.
 */

public class Story {

    private Page[] mPages;
    private Choice[] mChoices;
    private DatabaseAdapter dbAdapter;
    //TODO can the story be store as a database (instead of hardcoded here)?
    public Story(Context context) {

        dbAdapter = new DatabaseAdapter(context);
        mPages = new Page[7]; //Stories array

        mPages[0] = new Page(context.getString(R.string.page0), 0, 1, "bg0", 10);
        mPages[1] = new Page(context.getString(R.string.page1), 2, 3, "bg1", 10);
        mPages[2] = new Page(context.getString(R.string.page2), 4, 5, "bg2", 10);
        mPages[3] = new Page(context.getString(R.string.page3), 6, 7, "bg3", 10);
        mPages[4] = new Page(context.getString(R.string.page4), 8, 9, "bg4", 10);
        mPages[5] = new Page(context.getString(R.string.page5), 10, "bg6", 10);
        mPages[6] = new Page(context.getString(R.string.page6), 11, "bg5", 10);

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

        /*mPages[0] = new Page(
                "You wake up in the middle of the street by a lamp post, not remembering a thing about " +
                        "last night and feeling a bit unwell.",
                new Choice("You're sweating balls", 1),
                new Choice("You're shivering uncontrollably", 2),
                "bg0",
                10);

        mPages[1] = new Page(
                //Kylmä paikka
                "You decided to head towards the cool breeze to freshen up. Cool was a mistake, " +
                        "it is way too cold in here. You should probably keep moving to stay warm. " +
                        "There are two roads, one leads to the woods nearby and the other one seems to " +
                        "lead to the mountains afar.",
                new Choice("Let's hike to the mountains", 3),
                new Choice("You feel sick and go to the woods", 4),
                "bg1",
                20);

        mPages[2] = new Page(
                //Kuumotus paikka
                "You headed towards the warmth radiating from the savannah. Your nether regions " +
                        "start to burn because of the hot ground. You'd better move forward fast " +
                        "before you get roasted.",
                new Choice("Run around aimlessly", 4),
                new Choice("Head towards the shade", 6),
                "bg2",
                30);

        mPages[3] = new Page(
                //Illuminati paikka
                "Wait a minute, these aren't mountains at all. Your mind seems to be playing games " +
                        "with you. You decide to enter the pyramid despite the omnious feeling you have. " +
                        "Inside you find a dimly lit chamber with a half-full beer can.",
                new Choice("It's important to stay hydrated. Gladly accept the drink", 5),
                new Choice("Currently even thinking of beer is nauseating. Leave the eerie place", 4),
                "bg3",
                40);

        mPages[4] = new Page(
                //Metsä paikka
                "You have entered the disgustingly pleasant woods. You encounter a yellow bear " +
                        "wearing a red shirt who is kind enough to share some of his honey with you.",
                new Choice("Accept and eat the honey", 5),
                new Choice("Lol nope, let's get out of here", 6),
                "bg4",
                50);

        mPages[5] = new Page(
                //RIP paikka
                "Maybe consuming that was not such a great idea after all. You feel sick and start " +
                        "to lose consciousness. It was a mistake.",
                new Choice("Fug.", 0),
                "bg6",
                0);

        mPages[6] = new Page(
                //Koti paikka
                "Hey, this place looks familiar, you have found home at last! Time to drink a glass " +
                        "of water and get in a horizontal position. Finally you start to feel better.",
                new Choice("Start over", 0),
                "bg5",
                0);
    }*/


//    public Page getPage(int pageNumber) {
//        ArrayList<Page> pages = dbAdapter.retrievePages();
//        return pages.get(pageNumber); //called in MainActivity to get the appropriate page.
//    }
}