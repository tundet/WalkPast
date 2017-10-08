package com.example.ryu.walkpast.database;

/**
 * CONSTANTS FOR DATABASE HELPER
 * holds the SQL statements and names of tables, columns etc.
 * Created by RYU on 9/29/2017.
 */

class Constants {
    /*
     COLUMNS
     */
    static final String ROW_ID = "id";
    static final String STORY = "story";
    static final String CHOICE1 = "choice1";
    static final String CHOICE2 = "choice2";
    static final String BACKGROUND = "background";
    static final String STEPS = "steps";
    static final String PAGE = "page";
    static final String TEXT = "text";
    /*
     DB PROPERTIES
     */
    static final String DB_NAME = "StoriesDatabase";
    static final String TB1_NAME = "Pages";
    static final String TB2_NAME = "Choices";
    static final int DB_VERSION = 4;
    /*
     TABLE CREATION STATEMENT
     */
    static final String CREATE_TB1 = "CREATE TABLE Pages(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "story TEXT NOT NULL, choice1 INTEGER NOT NULL, choice2 INTEGER NULL, background TEXT NOT NULL,"
            + "steps INTEGER NOT NULL);";

    static final String CREATE_TB2 = "CREATE TABLE Choices(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "text TEXT NOT NULL, page INTEGER NOT NULL);";
    /*
     TABLE DELETION STATEMENT
     */
    static final String DROP_TB1 = "DROP TABLE IF EXISTS " + TB1_NAME;
    static final String DROP_TB2 = "DROP TABLE IF EXISTS " + TB2_NAME;
}
