package com.example.ryu.walkpast.Model;

/**
 * Story consists of pages and choices that will take to different pages.
 * Created by RYU on 9/18/2017.
 */

public class Story {

    private Page[] mPages;
    //TODO can the story be store as a database (instead of hardcoded here)?
    public Story() {
        mPages = new Page[7]; //Stories array

        mPages[0] = new Page(
                "Story 1",
                new Choice("Story 2", 1),
                new Choice("Story 3", 2),
                10);

        mPages[1] = new Page(
                "Story 2",
                new Choice("Story 4", 3),
                new Choice("Story 5", 4),
                20);

        mPages[2] = new Page(
                "Story 3",
                new Choice("Story 5", 4),
                new Choice("Story 7", 6),
                30);

        mPages[3] = new Page(
                "Story 4",
                new Choice("Story 5", 4),
                new Choice("Story 6", 5),
                40);

        mPages[4] = new Page(
                "Story 5",
                new Choice("Story 6", 5),
                new Choice("Story 7", 6),
                50);

        mPages[5] = new Page(
                "BAD END",
                new Choice("Start over", 0),
                0);

        mPages[6] = new Page(
                "GOOD END",
                new Choice("Start over", 0),
                0);
    }

    public Page getPage(int pageNumber) {
        return mPages[pageNumber]; //called in MainActivity to get the appropriate page.
    }
}